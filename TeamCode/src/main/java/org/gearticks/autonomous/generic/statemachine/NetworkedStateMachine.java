package org.gearticks.autonomous.generic.statemachine;

import android.util.Log;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
	private Map<AutonomousComponent, Map<Integer, AutonomousComponent>> connections;
	private Map<AutonomousComponent, Map<Integer, Integer>> exitConnections;

	public NetworkedStateMachine() {
		super(new HashSet<AutonomousComponent>());
		this.connections = new HashMap<>();
		this.exitConnections = new HashMap<>();
	}
	public NetworkedStateMachine(String id) {
		super(new HashSet<AutonomousComponent>(), id);
		this.connections = new HashMap<>();
		this.exitConnections = new HashMap<>();
	}

	public void setInitialComponent(AutonomousComponent initialComponent) {
		this.currentState = initialComponent;
		this.components.add(initialComponent); //in case this is the only state
	}
	/**
	 * Indicates that an output port on one component should switch to a specified state
	 * @param originComponent the component being switched from
	 * @param originPortNumber the output port on originComponent this applies to
	 * @param destinationComponent the component to switch to when the output port is triggered
	 */
	public void addConnection(AutonomousComponent originComponent,
			int originPortNumber, AutonomousComponent destinationComponent) {
		this.components.add(originComponent);
		this.components.add(destinationComponent);
		Map<Integer, AutonomousComponent> componentConnections = this.connections.get(originComponent);
		if (componentConnections == null) {
			componentConnections = new HashMap<>();
			this.connections.put(originComponent, componentConnections);
		}
		componentConnections.put(originPortNumber, destinationComponent);
	}
	/**
	 * Indicates that an output port on one component should terminate the state machine
	 * @param component the final component
	 * @param portNumber the output port for which component is the final component
	 * @param exitPort the output port of the state machine to trigger in this case
	 */
	public void addExitConnection(AutonomousComponent component, int portNumber, int exitPort) {
		this.components.add(component);
		Map<Integer, Integer> componentConnections = this.exitConnections.get(component);
		if (componentConnections == null) {
			componentConnections = new HashMap<>();
			this.exitConnections.put(component, componentConnections);
		}
		componentConnections.put(portNumber, exitPort);
	}
	/**
	 * Shorthand for addExitConnection(component, portNumber, NEXT_STATE).
	 * Use if you don't care what output port of the state machine is triggered upon exit.
	 * @param component the final component
	 * @param portNumber the output port for which component is the final component
	 */
	public void addExitConnection(AutonomousComponent component, int portNumber) {
		this.addExitConnection(component, portNumber, NEXT_STATE);
	}

	@Override
	public void setup() {
		super.setup();
		this.currentState.setup();
	}

	@Override
	public int run() {
		final int superTransition = super.run();
		if (superTransition != NOT_DONE) return superTransition;

		if (this.currentState == null) return NEXT_STATE;

		final int transition = this.currentState.run();
		if (transition == NOT_DONE) return transition;

		this.currentState.tearDown();

		final Map<Integer, Integer> exitPorts = this.exitConnections.get(this.currentState);
		if (exitPorts != null) {
			final Integer exitPort = exitPorts.get(transition);
			if (exitPort != null) {
				Log.i(Utils.TAG, "Exiting from " + this.currentState + " on port " + transition);
				this.currentState = null;
				return exitPort;
			}
		}

		final Map<Integer, AutonomousComponent> componentConnections = this.connections.get(this.currentState);
		Utils.assertNotNull(componentConnections, "No transitions defined for " + this.currentState);
		final AutonomousComponent nextState = componentConnections.get(transition);
		Utils.assertNotNull(nextState, "No transition defined for " + this.currentState + " on port " + transition);
		Log.i(Utils.TAG, "Transition from " + this.currentState + " => " + nextState);
		this.currentState = nextState;
		nextState.setup();
		return NOT_DONE;
	}
}
