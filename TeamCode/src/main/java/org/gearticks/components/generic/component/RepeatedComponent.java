package org.gearticks.components.generic.component;

import org.gearticks.components.generic.statemachine.NetworkedStateMachine;

/**
 * Allows for running a component repeatedly forever.
 * Whenever the component transitions, it will start over.
 */
public class RepeatedComponent extends NetworkedStateMachine {
	/**
	 * @param component the component to run repeatedly
	 */
	public RepeatedComponent(OpModeComponent<DefaultTransition> component) {
		super();
		this.setInitialComponent(component);
		this.addConnection(component, DefaultTransition.DEFAULT, component);
	}
}
