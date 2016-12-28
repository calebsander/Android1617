package org.gearticks.autonomous.generic.statemachine.network;

import android.support.annotation.NonNull;

import java.util.List;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.linear.LinearStateMachine;

/**
 * Assumes all internal components form a linear/sequential chain.
 * Simplifies initialization of a sate-machine
 */
public class LinearStateMachineFullImpl extends StateMachineFullImpl implements LinearStateMachine {
	//private final List<AutonomousComponent> components = new ArrayList<>();

	public LinearStateMachineFullImpl(@NonNull List<AutonomousComponent> components) {
		super(1,1);
		this.addComponents(components);
		
		AutonomousComponent originComponent = this.inputPorts.get(1);
		for (AutonomousComponent ac : components){
			this.addConnection(originComponent, 1, ac, 1);
			originComponent = ac;
		}
		//make output connection:
		this.addConnection(originComponent, 1, this.outputPorts.get(1), 1);
	}
}
