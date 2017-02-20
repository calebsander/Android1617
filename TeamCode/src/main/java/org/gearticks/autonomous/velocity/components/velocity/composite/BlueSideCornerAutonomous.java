package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.DebugPause;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallLine;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.components.velocity.single.BananaTurnNoGiro;
import org.gearticks.autonomous.velocity.components.velocity.single.RaiseSideRollers;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * Created by BenMorris on 2/20/2017.
 */

public class BlueSideCornerAutonomous extends LinearStateMachine {
	final double DISTANCE_FROM_WALL = 10.0;

	public BlueSideCornerAutonomous(OpModeContext<VelocityConfiguration> opModeContext) {
		super();

		//Get to far beacon
		//addComponent(new GiroDriveEncoder(225.0, -0.4, 4000, opModeContext, "Drive backwards"));
		//addComponent(new GiroTurn(180.0, opModeContext, "Turn parallel to wall"));
		addComponent(new GiroDriveAlongWallEncoder(DISTANCE_FROM_WALL, 180.0, -0.8, 4000, opModeContext, "Drive towards far beacon"));
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, -0.25, 2000, opModeContext, "Get to far beacon"));

		//Press beacon
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, 0.17, 500, opModeContext, "Adjust to white line"));
		addComponent(new GiroTurn(180.0, opModeContext, "Straighten out"));
		addComponent(new DebugPause(opModeContext));
		//addComponent(new SidePressBeaconButton(opModeContext, "Press Button"));

		//Get to near beacon
		addComponent(new GiroDriveAlongWallEncoder(DISTANCE_FROM_WALL, 180.0, 0.8, 3000, opModeContext, "Range sensor drive along wall"));
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL + 1, 180.0, 0.25, 2000, opModeContext, "Range sensor drive along wall to line"));

		//Press beacon
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, -0.17, 500, opModeContext, "Adjust to white line"));
		addComponent(new GiroTurn(180.0, opModeContext, "Straighten out"));
		addComponent(new DebugPause(opModeContext));
		//addComponent(new SidePressBeaconButton(opModeContext, "Press Button"));

		//Shoot and Cap ball
		addComponent(new BananaTurnNoGiro(-90.0, -0.4, 3500, opModeContext, "Banana turn to 90"));
		addComponent(new Wait(0.3, "Wait for 0.3 seconds"));
		addComponent(new GiroTurn(225.0, opModeContext, "Turn to shoot"));
		//addComponent(new Shoot3Balls(true, opModeContext, "Shoot"));
		addComponent(new GiroDriveEncoder(225.0, 0.4, 3000, opModeContext, "Drive to cap ball"));
	}
}
