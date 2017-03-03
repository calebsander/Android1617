package org.gearticks.autonomous.generic.statemachine;

import android.util.Log;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.velocity.components.generic.Stopped;
import org.gearticks.opmodes.utility.Utils;

/**
 * A state machine with a number of component states that don't necessarily proceed in a linear fashion.
 * This is useful for looping and conditionally branching state machines.
 *
 * Each output port emitted by each component should be connected to the component to execute next.
 * Output ports not connected to another component must cause the state machine to end.
 * You can alternatively add a {@link Stopped} component and connect the terminal states to it.
 *
 * Keep in mind that no state is aware of the prior states, specifically the state that transitioned to it.
 */
public class NetworkedStateMachine extends StateMachineBase {
	private Map<AutonomousComponent<?>, Map<Enum<?>, AutonomousComponent<?>>> connections;
	private Map<AutonomousComponent<?>, Set<Enum<?>>> exitConnections;

	public NetworkedStateMachine() {
		super(new HashSet<>());
		this.connections = new HashMap<>();
		this.exitConnections = new HashMap<>();
	}
	public NetworkedStateMachine(String id) {
		super(new HashSet<>(), id);
		this.connections = new HashMap<>();
		this.exitConnections = new HashMap<>();
	}

	public void setInitialComponent(AutonomousComponent<?> initialComponent) {
		this.currentState = initialComponent;
		this.components.add(initialComponent); //in case this is the only state
	}
	/**
	 * Indicates that an output port on one component should switch to a specified state
	 * @param originComponent the component being switched from
	 * @param originPort the output port on originComponent this applies to
	 * @param destinationComponent the component to switch to when the output port is triggered
	 */
	public <T extends Enum<?>> void addConnection(AutonomousComponent<T> originComponent, T originPort, AutonomousComponent<?> destinationComponent) {
		this.components.add(originComponent);
		this.components.add(destinationComponent);
		Map<Enum<?>, AutonomousComponent<?>> componentConnections = this.connections.get(originComponent);
		if (componentConnections == null) {
			componentConnections = new HashMap<>();
			this.connections.put(originComponent, componentConnections);
		}
		componentConnections.put(originPort, destinationComponent);
	}
	/**
	 * Indicates that an output port on one component should terminate the state machine
	 * @param component the final component
	 * @param port the output port for which component is the final component
	 */
	public <T extends Enum<?>> void addExitConnection(AutonomousComponent<T> component, T port) {
		this.components.add(component);
		Set<Enum<?>> componentConnections = this.exitConnections.get(component);
		if (componentConnections == null) {
			componentConnections = new HashSet<>();
			this.exitConnections.put(component, componentConnections);
		}
		componentConnections.add(port);
	}

	@Override
	public void setup() {
		super.setup();
		this.currentState.setup();
	}

	@Override
	public DefaultTransition run() {
		final DefaultTransition superTransition = super.run();
		if (superTransition != null) return superTransition;

		if (this.currentState == null) return DefaultTransition.DEFAULT;

		//Delegate run() to current state
		final Enum transition = this.currentState.run();
		//If no transition, no need to advance state
		if (transition == null) return null;

		this.currentState.tearDown();

		final Set<Enum<?>> exitPorts = this.exitConnections.get(this.currentState);
		if (exitPorts != null) {
			if (exitPorts.contains(transition)) {
				Log.i(Utils.TAG, "Exiting from \"" + this.currentState + "\" on port \"" + transition + "\"");
				this.currentState = null;
				return DefaultTransition.DEFAULT;
			}
		}

		final Map<Enum<?>, AutonomousComponent<?>> componentConnections = this.connections.get(this.currentState);
		Utils.assertNotNull(componentConnections, "No transitions defined for \"" + this.currentState + "\"");
		final AutonomousComponent<?> nextState = componentConnections.get(transition);
		Utils.assertNotNull(nextState, "No transition defined for \"" + this.currentState + "\" on port \"" + transition + "\"");
		Log.i(Utils.TAG, "Transition from \"" + this.currentState + "\" => \"" + nextState + "\" on port \"" + transition + "\"");
		this.currentState = nextState;
		nextState.setup();
		return null;
	}
}
