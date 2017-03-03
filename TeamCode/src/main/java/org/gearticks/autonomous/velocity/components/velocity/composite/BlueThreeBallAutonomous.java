package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.DebugPause;
import org.gearticks.autonomous.velocity.components.generic.GiroBananaTurnEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallLine;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class BlueThreeBallAutonomous extends LinearStateMachine {
	private final double DISTANCE_FROM_WALL = 10.0;

	public BlueThreeBallAutonomous(OpModeContext<VelocityConfiguration> opModeContext) {
		super();

		//Get to far beacon
		addComponent(new GiroDriveEncoder(26.0, 0.4, 8000, opModeContext, "Drive forward"));
		addComponent(new GiroTurn(270.0, opModeContext, "Turn back to wall" ));
		addComponent (new GiroBananaTurnEncoder(270.0, 180, -0.4, 8000, opModeContext, "Banana turn to wall"));

		addComponent(new DebugPause(opModeContext));

		addComponent(new GiroTurn(180.0, opModeContext, "Straighten out"));


		//addComponent(new GiroTurn(180.0, opModeContext, "Turn parallel to wall"));
		//addComponent(new GiroDriveAlongWallEncoder(DISTANCE_FROM_WALL, 180.0, -0.4, 4000, opModeContext, "Drive towards far beacon"));
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, -0.25, 4000, opModeContext, "Get to far beacon"));

		//Press beacon
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, 0.15, 500, opModeContext, "Adjust to white line"));
		addComponent(new GiroTurn(180.0, opModeContext, "Straighten out"));
		addComponent(new DebugPause(opModeContext));
		//addComponent(new SidePressBeaconButton(opModeContext, "Press Button"));

		//Get to near beacon
		addComponent(new GiroDriveAlongWallEncoder(DISTANCE_FROM_WALL, 180.0, 0.4, 3000, opModeContext, "Range sensor drive along wall"));
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL + 1, 180.0, 0.25, 3000, opModeContext, "Range sensor drive along wall to line"));

		//Press beacon
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, -0.15, 500, opModeContext, "Adjust to white line"));
		addComponent(new GiroTurn(180.0, opModeContext, "Straighten out"));
		addComponent(new DebugPause(opModeContext));
		//addComponent(new SidePressBeaconButton(opModeContext, "Press Button"));

		//Cap ball
	}
}
