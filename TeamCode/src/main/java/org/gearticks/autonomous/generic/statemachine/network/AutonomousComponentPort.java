package org.gearticks.autonomous.generic.statemachine.network;

import org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl;

/**
 * Represents a in- or output port for use in a StateMachineFullImpl
 *
 */
public class AutonomousComponentPort extends AutonomousComponentAbstractImpl {
	private final int portNumber;
	private final boolean isInput;
	
	public AutonomousComponentPort(int portNumber, boolean isInput) {
		super();
		this.portNumber = portNumber;
		this.isInput = isInput;
	}

	
	@Override
	public int run() {
		int transition;
		super.run();
		
		if (this.isInput){
			transition  = 1;
		}
		else {
			transition = 0;
		}
		return transition;
	}


	public int getPortNumber() {
		return portNumber;
	}
	
	
	

}
