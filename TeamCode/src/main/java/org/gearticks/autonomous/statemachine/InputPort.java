package org.gearticks.autonomous.statemachine;

/**
 * Input port to be used by StateMachine
 *
 */
public class InputPort extends AutonomousComponentPort {

	public InputPort(int portNumber) {
		super(portNumber, true);
	}

}
