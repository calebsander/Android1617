package org.gearticks.autonomous.statemachine;

import java.util.List;

import org.gearticks.autonomous.component.AutonomousComponent;

/**
 * Assumes all internal components form a linear/sequential chain.
 * Simplifies initialization of a sate-machine
 */
public class LinearStateMachine extends StateMachine {
	//private final List<AutonomousComponent> components = new ArrayList<>();

	public LinearStateMachine(List<AutonomousComponent> components) {
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
