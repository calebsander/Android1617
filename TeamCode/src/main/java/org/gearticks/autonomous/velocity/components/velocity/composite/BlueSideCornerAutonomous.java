package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.ParallelComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.GiroBananaTurnEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallLine;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.velocity.single.DeploySideRollers;
import org.gearticks.autonomous.velocity.components.velocity.single.RaiseSideRollers;
import org.gearticks.autonomous.velocity.components.velocity.single.RunIntake;
import org.gearticks.hardware.configurations.VelocityConfiguration;

@Deprecated
public class BlueSideCornerAutonomous extends LinearStateMachine {
	final double DISTANCE_FROM_WALL = 10.0;

	public BlueSideCornerAutonomous(OpModeContext<VelocityConfiguration> opModeContext) {
		super();

		//Get to far beacon
		addComponent(new RunIntake(1.3, true, opModeContext, "Intake particle"));
		final ParallelComponent driveAndDeployRollers = new ParallelComponent();
		driveAndDeployRollers.addComponent(new GiroBananaTurnEncoder(225.0, 180.0, -0.7, 8000, opModeContext, "Banana turn to 180.0"));
		driveAndDeployRollers.addComponent(new DeploySideRollers(opModeContext, "Deploy side rollers"));
		addComponent(driveAndDeployRollers);
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, -0.3, 4000, opModeContext, "Get to far beacon"));

		//Press beacon
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, 0.15, 500, opModeContext, "Adjust to white line"));
		addComponent(new GiroTurn(180.0, 0.1, 5, opModeContext, "Straighten out"));
		addComponent(new SidePressBeaconButton(opModeContext, "Press Button"));

		//Get to near beacon
		addComponent(new GiroDriveAlongWallEncoder(DISTANCE_FROM_WALL, 180.0, 1.0, 3000, opModeContext, "Range sensor drive along wall"));
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, 0.3, 3000, opModeContext, "Range sensor drive along wall to line"));

		//Press beacon
		addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, -0.15, 500, opModeContext, "Adjust to white line"));
		addComponent(new GiroTurn(180.0, 0.1, 5, opModeContext, "Straighten out"));
		addComponent(new SidePressBeaconButton(opModeContext, "Press Button"));

		//Shoot
		addComponent(new RaiseSideRollers(opModeContext, "Raise rollers"));
		addComponent(new GiroBananaTurnEncoder(180.0, 187.0, -0.4, 400, opModeContext, "Banana turn to 187"));
		addComponent(new GiroBananaTurnEncoder(187, 270.0, 0.5, 500, 0.1, 0.2, 1.0, opModeContext, "banana turn to shoot"));
		addComponent(new Shoot3Balls(true, opModeContext, "Shoot"));

		//Cap ball
		addComponent(new GiroTurn(290.0, 0.1, 3, 2.0, opModeContext, "turn to cap ball"));
		addComponent(new GiroBananaTurnEncoder(300.0, 230.0, 1.0, 5000, 0.25, 0.3, 3.0, opModeContext, "Hit cap ball"));
	}
}
