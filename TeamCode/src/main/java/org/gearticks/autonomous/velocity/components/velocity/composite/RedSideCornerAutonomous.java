package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.DebugPause;
import org.gearticks.autonomous.velocity.components.generic.GiroBananaTurnEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallLine;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.generic.BananaTurnNoGiro;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.components.velocity.single.DeploySideRollers;
import org.gearticks.hardware.configurations.VelocityConfiguration;

@Deprecated
public class RedSideCornerAutonomous extends LinearStateMachine {
	final double DISTANCE_FROM_WALL = 10.0;

	public RedSideCornerAutonomous(OpModeContext<VelocityConfiguration> opModeContext) {
		super();

		//Get to far beacon
		addComponent(new GiroDriveEncoder(225.0, 0.4, 1000, opModeContext, "Drive forward"));
		addComponent(new GiroTurn(45.0, opModeContext, "Straighten out"));
		addComponent(new DeploySideRollers(opModeContext, "Deploy side rollers"));
		addComponent(new GiroBananaTurnEncoder(45.0, 0.0, 0.4, 8000, opModeContext, "Banana turn to wall"));
		addComponent(new DebugPause(opModeContext));
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 0.0, 0.25, 2000, opModeContext, "Get to far beacon"));

		//Press beacon
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 0.0, -0.17, 500, opModeContext, "Adjust to white line"));
		addComponent(new GiroTurn(0.0, opModeContext, "Straighten out"));
		addComponent(new SidePressBeaconButton(opModeContext, "Press Button"));

		//Get to near beacon
		addComponent(new GiroDriveAlongWallEncoder(DISTANCE_FROM_WALL, 0.0, -0.8, 3000, opModeContext, "Range sensor drive along wall"));
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL + 1, 0.0, -0.25, 2000, opModeContext, "Range sensor drive along wall to line"));

		//Press beacon
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 0.0, 0.17, 500, opModeContext, "Adjust to white line"));
		addComponent(new GiroTurn(0.0, opModeContext, "Straighten out"));
		addComponent(new SidePressBeaconButton(opModeContext, "Press Button"));

		//Shoot and Cap ball
		addComponent(new GiroBananaTurnEncoder(0.0, 240.0, -0.6, 3000, opModeContext, "Banana turn to 270"));
		addComponent(new DebugPause(opModeContext));
		addComponent(new Wait(0.3, "Wait for 0.3 seconds"));
		addComponent(new Shoot3Balls(true, opModeContext, "Shoot"));
		addComponent(new GiroTurn(270.0, opModeContext, "Straighten out"));
		addComponent(new GiroDriveEncoder(225.0, 0.4, 3000, opModeContext, "Drive to cap ball"));
	}
}
