package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * Created by BenMorris on 2/11/2017.
 */

public class GoToWall extends LinearStateMachine {
	public GoToWall(int pos, VelocityConfiguration configuration, String id) {
		switch (pos) {
			case 1:
				position1(configuration);
				break;
			case 2:
				position2(configuration);
				break;
			default: throw new RuntimeException("Position is not 1 or 2: GoToWall");
		}
	}

	public void position1(VelocityConfiguration configuration) {
		/* TODO: code positions */
	}
	public void position2(VelocityConfiguration configuration) {
		/* TODO: code positions */
	}
}
