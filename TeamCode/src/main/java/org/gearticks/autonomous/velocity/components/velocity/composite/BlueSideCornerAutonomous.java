package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.DebugPause;
import org.gearticks.autonomous.velocity.components.generic.GiroBananaTurnEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallLine;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.components.generic.BananaTurnNoGiro;
import org.gearticks.autonomous.velocity.components.velocity.single.DeploySideRollers;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class BlueSideCornerAutonomous extends LinearStateMachine {
	final double DISTANCE_FROM_WALL = 12.0;

	public BlueSideCornerAutonomous(OpModeContext<VelocityConfiguration> opModeContext) {
		super();

		//Get to far beacon
		addComponent(new GiroDriveEncoder(225.0, -0.4, 1000, opModeContext, "Drive forward"));
		addComponent(new BananaTurnNoGiro(180.0, -0.4, 8000, opModeContext, "Banana turn to 180"));
		addComponent(new DeploySideRollers(opModeContext, "Deploy side rollers")); //Todo: fix deployment
		//addComponent(new GiroBananaTurnEncoder(225.0, 200.0, -0.4, 8000, opModeContext, "Banana turn to 200"));
		addComponent(new GiroTurn(180.0, opModeContext, "Straighten out"));

		addComponent(new DebugPause(opModeContext));

		//addComponent(new GiroTurn(180.0, opModeContext, "Turn parallel to wall"));
		//addComponent(new GiroDriveAlongWallEncoder(DISTANCE_FROM_WALL, 180.0, -0.4, 4000, opModeContext, "Drive towards far beacon"));
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, -0.15, 4000, opModeContext, "Get to far beacon"));

		//Press beacon
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, 0.05, 500, opModeContext, "Adjust to white line"));
		addComponent(new GiroTurn(180.0, opModeContext, "Straighten out"));
		addComponent(new DebugPause(opModeContext));
		//addComponent(new SidePressBeaconButton(opModeContext, "Press Button"));

		//Get to near beacon
		addComponent(new GiroDriveAlongWallEncoder(DISTANCE_FROM_WALL, 180.0, 0.2, 3000, opModeContext, "Range sensor drive along wall"));
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL + 1, 180.0, 0.15, 3000, opModeContext, "Range sensor drive along wall to line"));

		//Press beacon
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, -0.05, 500, opModeContext, "Adjust to white line"));
		addComponent(new GiroTurn(180.0, opModeContext, "Straighten out"));
		addComponent(new DebugPause(opModeContext));
		//addComponent(new SidePressBeaconButton(opModeContext, "Press Button"));

		//Shoot and Cap ball
		addComponent(new BananaTurnNoGiro(-45.0, 0.4, 3500, opModeContext, "Banana turn to 45"));
		addComponent(new DebugPause(opModeContext));
		addComponent(new Wait(0.3, "Wait for 0.3 seconds"));
		addComponent(new GiroTurn(225.0, opModeContext, "Turn to shoot"));
		//addComponent(new Shoot3Balls(true, opModeContext, "Shoot"));
		addComponent(new GiroDriveEncoder(225.0, 0.4, 3000, opModeContext, "Drive to cap ball"));
	}
}
