package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallLine;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.velocity.single.BananaTurnNoGiro;
import org.gearticks.autonomous.velocity.components.velocity.single.RaiseSideRollers;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * Created by BenMorris on 2/20/2017.
 */

public class RedSideCornerAutonomous extends LinearStateMachine {
	final double DISTANCE_FROM_WALL = 10.0;

	public RedSideCornerAutonomous(OpModeContext<VelocityConfiguration> opModeContext) {
		super();

		//Get to far beacon
		addComponent(new GiroDriveEncoder(0.0, 0.2, 100, opModeContext, "Drive forwards"));
		addComponent(new GiroTurn(45.0, opModeContext, "Turn to 45 degrees"));
		addComponent(new GiroDriveEncoder(45.0, 0.4, 3000, opModeContext, "Drive to wall"));
		addComponent(new BananaTurnNoGiro(false, 0.0, 0.4, 1500, opModeContext, "Banana turn parallel to wall"));
		addComponent(new GiroDriveEncoder(3.0, 0.8, 4000, opModeContext, "Drive towards far beacon"));
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 0.0, 0.25, 2000, opModeContext, "Get to far beacon"));

		//Press beacon
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 0.0, -0.17, 500, opModeContext, "Adjust to white line"));
		addComponent(new GiroTurn(0.0, opModeContext, "Straighten out"));
		//addComponent(new SidePressBeaconButton(opModeContext, "Press Button"));

		//Get to near beacon
		addComponent(new GiroDriveAlongWallEncoder(DISTANCE_FROM_WALL, 0.0, -0.8, 3000, opModeContext, "Range sensor drive along wall"));
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL + 1, 0.0, -0.25, 2000, opModeContext, "Range sensor drive along wall to line"));

		//Press beacon
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 0.0, 0.17, 500, opModeContext, "Adjust to white line"));
		addComponent(new GiroTurn(0.0, opModeContext, "Straighten out"));
		//addComponent(new SidePressBeaconButton(opModeContext, "Press Button"));
	}
}
