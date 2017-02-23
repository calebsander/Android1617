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
import org.gearticks.autonomous.velocity.components.velocity.single.RaiseSideRollers;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class BlueSideCornerAutonomous extends LinearStateMachine {
	final double DISTANCE_FROM_WALL = 10.0;

	public BlueSideCornerAutonomous(OpModeContext<VelocityConfiguration> opModeContext) {
		super();

		//Get to far beacon
		addComponent(new GiroDriveEncoder(225.0, -0.6, 700, opModeContext, "Drive backwards"));
		//addComponent(new BananaTurnNoGiro(180.0, -0.4, 8000, opModeContext, "Banana turn to 180"));
		addComponent(new DeploySideRollers(opModeContext, "Deploy side rollers")); //Todo: fix deployment
		addComponent(new GiroBananaTurnEncoder(225.0, 180.0, -0.6, 8000, opModeContext, "Banana turn to 200"));
		addComponent(new GiroTurn(180.0, opModeContext, "Straighten out"));

		//addComponent(new GiroTurn(180.0, opModeContext, "Turn parallel to wall"));
		//addComponent(new GiroDriveAlongWallEncoder(DISTANCE_FROM_WALL, 180.0, -0.4, 4000, opModeContext, "Drive towards far beacon"));
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, -0.5, 4000, opModeContext, "Get to far beacon"));

		//Press beacon
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, 0.15, 500, opModeContext, "Adjust to white line"));
		addComponent(new GiroTurn(180.0, opModeContext, "Straighten out"));
		addComponent(new SidePressBeaconButton(opModeContext, "Press Button"));

		//Get to near beacon
		addComponent(new GiroDriveAlongWallEncoder(DISTANCE_FROM_WALL, 180.0, 0.5, 3000, opModeContext, "Range sensor drive along wall"));
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL + 1, 180.0, 0.3, 3000, opModeContext, "Range sensor drive along wall to line"));

		//Press beacon
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, -0.15, 500, opModeContext, "Adjust to white line"));
		addComponent(new GiroTurn(180.0, opModeContext, "Straighten out"));
		addComponent(new SidePressBeaconButton(opModeContext, "Press Button"));

		//Shoot
		addComponent(new RaiseSideRollers(opModeContext, "Raise rollers"));
		addComponent(new GiroBananaTurnEncoder(180.0, 187.0, 0.6, 250, opModeContext, "Banana turn to 187"));
		addComponent(new GiroTurn(165.0, opModeContext, "Turn to 165"));
		addComponent(new GiroDriveEncoder(165.0, -0.6, 750, opModeContext, "Drive backwards"));
		addComponent(new GiroTurn(275.0, opModeContext, "Turn to shoot"));
		addComponent(new Shoot3Balls(true, opModeContext, "Shoot"));

		//Cap ball
		addComponent(new GiroTurn(270.0, opModeContext, "Turn to cap ball"));
		addComponent(new GiroDriveEncoder(270.0, 0.4, 3500, opModeContext, "Drive to cap ball"));
		addComponent(new GiroTurn(260.0, opModeContext, "Hit cap ball"));
		addComponent(new Wait(0.3, "Wait for 0.3"));
		addComponent(new GiroTurn(270.0, opModeContext, "Straighten out"));
		addComponent(new GiroDriveEncoder(270.0, 0.4, 1000, opModeContext, "Park"));
	}
}
