package org.gearticks.autonomous.generic.statemachine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.opmodes.utility.Utils;

/**
 * A StateMachineFullImpl itself implements an AutonomousComponent, i.e. this supports hierarchical state-machines.
 * Internally, a StateMachineFullImpl contains InputPorts, AutonomousComponents, StateMachineConnections and OutputPorts
 * (where Input- and OutputPorts are AutonomousComponents)
 * StateMachineConnections connect between an output-port of one AutonomousComponent to the input-port of another AutonomousComponent.
 *
 * A StateMachineFullImpl uses the specialized Input- and OutputPort AutonomousComponents to implement the ports,
 * but in general for a custom AutonomousComponent this is not necessary.
 *
 * The use of Input- and OutputPort AutonomousComponents allows the StateMachineFullImpl to have an internal network of AutonomousComponents.
 *
 */
public class NonLinearStateMachine extends StateMachineAbstractImpl {
	private Map<AutonomousComponent, Map<Integer, AutonomousComponent>> connections;
	private Map<AutonomousComponent, Map<Integer, Integer>> exitConnections;

	public NonLinearStateMachine(AutonomousComponent initialComponent) {
		super();
		this.connections = new HashMap<>();
		this.exitConnections = new HashMap<>();
		this.currentState = initialComponent;
	}

	public void addConnection(AutonomousComponent originComponent,
			int originPortNumber, AutonomousComponent destinationComponent) {
		Map<Integer, AutonomousComponent> componentConnections = this.connections.get(originComponent);
		if (componentConnections == null) {
			componentConnections = new HashMap<>();
			this.connections.put(originComponent, componentConnections);
		}
		componentConnections.put(originPortNumber, destinationComponent);
	}
	public void addExitConnection(AutonomousComponent component, int portNumber, int exitPort) {
		Map<Integer, Integer> componentConnections = this.exitConnections.get(component);
		if (componentConnections == null) {
			componentConnections = new HashMap<>();
			this.exitConnections.put(component, componentConnections);
		}
		componentConnections.put(portNumber, exitPort);
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

		final int transition = this.currentState.run();
		if (transition == NOT_DONE) return transition;

		this.currentState.tearDown();

		final Integer exitPort = this.exitConnections.get(this.currentState).get(transition);
		if (exitPort != null) return exitPort;

		final AutonomousComponent nextState = this.connections.get(this.currentState).get(transition);
		if (nextState == null) {
			this.getLogger().warning("ERROR: Transition from " + this.currentState + " to transition # " + transition + ". No connection found.");
		}
		else {
			this.currentState = nextState;
			nextState.setup();
		}
		return NOT_DONE;
	}
}
