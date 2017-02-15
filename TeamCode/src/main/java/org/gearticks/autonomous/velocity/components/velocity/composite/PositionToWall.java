package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * Created by BenMorris on 2/11/2017.
 */

public class PositionToWall extends NetworkedStateMachine {
	public PositionToWall(int pos, VelocityConfiguration configuration, String id) {
		switch (pos) {
			case 1:
				position1(configuration);
				break;
			case 2:
				position2(configuration);
				break;
			default: throw new RuntimeException("Position is not 1 or 2: PositionToWall");
		}
	}

	public void position1(VelocityConfiguration configuration) {
		final AutonomousComponent shoot = new Shoot2Balls(true, configuration, "Shoot 2 balls");
		final AutonomousComponent parkCenter = new ParkCenter(1, configuration, "Park in center");
		final AutonomousComponent parkCorner = new ParkCorner(1, configuration, "Park in corner");
		final AutonomousComponent goToWall = new GoToWall(1, configuration, "Go to wall");

		setInitialComponent(shoot);
		addConnection(shoot, 1/*Ask how to: beacons*/, goToWall);
		addConnection(shoot, 1/*Ask how to: noBeacons, center*/, parkCenter);
		addConnection(shoot, 1/*Ask how to: noBeacons, corner*/, parkCorner);

		/* TODO: code positions */
	}
	public void position2(VelocityConfiguration configuration) {
		/* TODO: code positions */
	}
}
