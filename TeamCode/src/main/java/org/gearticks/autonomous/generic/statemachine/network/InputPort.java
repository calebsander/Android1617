package org.gearticks.autonomous.generic.statemachine.network;

/**
 * Input port to be used by StateMachineFullImpl
 *
 */
public class InputPort extends AutonomousComponentPort {

	public InputPort(int portNumber) {
		super(portNumber, true);
	}

}
