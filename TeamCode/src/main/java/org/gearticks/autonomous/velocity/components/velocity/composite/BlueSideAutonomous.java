package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.GiroBananaTurnEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallLine;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveToLine;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.velocity.single.DeploySideRollers;
import org.gearticks.autonomous.velocity.components.velocity.single.RaiseSideRollers;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class BlueSideAutonomous extends LinearStateMachine {
    public BlueSideAutonomous(int distanceFromWall, OpModeContext<VelocityConfiguration> opModeContext) {
        super();

        //Drive to wall
        addComponent(new GiroBananaTurnEncoder(0.0, 18.0, 0.7, 500, opModeContext, "BTR 18 - 1000"));
        addComponent(new GiroBananaTurnEncoder(18, 90.0, 0.8, 7700, opModeContext, "BTR 90 - 7700"));
        addComponent(new GiroTurn(180.0, 0.1, 20, opModeContext, "Straighten out"));
        addComponent(new DeploySideRollers(opModeContext, "Deploy rollers"));
        //addComponent(new GiroDriveAlongWallEncoder(distanceFromWall, 180.0, -0.4, 2000, configuration, "Drive backwards"));
        addComponent(new GiroDriveAlongWallLine(distanceFromWall - 1, 180.0, 0.2, 6000, opModeContext, "Drive to beacon"));

        //Press beacon
        addComponent(new GiroDriveAlongWallLine(distanceFromWall, 180.0, -0.17, 500, opModeContext, "Adjust to white line"));
        addComponent(new GiroDriveAlongWallEncoder(distanceFromWall, 180.0, -0.17, 25, opModeContext, "Align"));
        addComponent(new GiroTurn(180.0, opModeContext, "Straighten out"));
        addComponent(new SidePressBeaconButton(true, opModeContext, "Press Button"));

        //Go to second beacon
        addComponent(new GiroDriveAlongWallEncoder(distanceFromWall, 180.0, -0.8, 3000, opModeContext, "Range sensor drive along wall"));
        addComponent(new GiroDriveAlongWallLine(distanceFromWall + 1, 180.0, -0.25, 2000, opModeContext, "Range sensor drive along wall to line"));
        //sm.addComponent(new GiroDriveToLine(180, 0.7, 8000, this.configuration, "Drive to white line"));

        //Press second beacon
        addComponent(new GiroDriveToLine(180, 0.05, 500, opModeContext, "Adjust to white line"));
        addComponent(new GiroTurn(180.0, opModeContext, "Straighten out"));
        addComponent(new SidePressBeaconButton(true, opModeContext, "Press Button"));

        //Cap ball
        addComponent(new RaiseSideRollers(opModeContext, "Raise rollers"));
        //addComponent(new GiroTurn(225.0, configuration, "Turn to cap ball"));
        addComponent(new GiroBananaTurnEncoder(180.0, 235.0, 0.6, 400, opModeContext, "BTR 235 - 400"));
        addComponent(new GiroDriveEncoder(235.0, 1.0, 6000, opModeContext, "Hit cap ball and park"));
    }
}
