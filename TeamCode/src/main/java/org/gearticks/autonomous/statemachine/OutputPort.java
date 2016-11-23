package org.gearticks.autonomous.statemachine;

/**
 * Output port to be used by StateMachine
 *
 */
public class OutputPort extends AutonomousComponentPort {

	public OutputPort(int portNumber) {
		super(portNumber, false);
	}

}
