package org.gearticks.autonomous.generic.component;

import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;

/**
 * Allows for running a component repeatedly forever.
 * Whenever the component transitions, it will start over.
 */
public class RepeatedComponent extends NetworkedStateMachine {
	/**
	 * @param component the component to run repeatedly
	 */
	public RepeatedComponent(AutonomousComponent<DefaultTransition> component) {
		super();
		this.setInitialComponent(component);
		this.addConnection(component, DefaultTransition.DEFAULT, component);
	}
}
