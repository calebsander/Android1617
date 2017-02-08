package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.experimental.GiroBananaTurnEncoder;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveAlongWallLine;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveToLine;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.components.velocity.single.DeploySideRollers;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class BlueSideAutonomous extends LinearStateMachine {
    public BlueSideAutonomous(int distanceFromWall, OpModeContext<VelocityConfiguration> opModeContext) {
        super();

        //Drive to wall
        addComponent(new GiroBananaTurnEncoder(0.0, 30.0, 0.6, 1000, opModeContext, "BTR 30 - 1000"));
        addComponent(new GiroBananaTurnEncoder(30.0, 90.0, 0.7, 6000, opModeContext, "BTR 90 - 6000"));
        addComponent(new GiroTurn(180.0, 0.1, 20, opModeContext, "Straighten out"));
        addComponent(new DeploySideRollers(opModeContext, "Deploy rollers"));
        addComponent(new GiroDriveAlongWallEncoder(distanceFromWall, 180.0, -0.25, 2000, opModeContext, "Drive backwards"));
        addComponent(new GiroDriveAlongWallLine(distanceFromWall, 180.0, 0.25, 4000, opModeContext, "Drive to beacon"));

        //Press beacon
        addComponent(new GiroDriveAlongWallLine(distanceFromWall, 180.0, -0.17, 500, opModeContext, "Adjust to white line"));
        addComponent(new GiroDriveAlongWallEncoder(distanceFromWall, 180.0, -0.17, 25, opModeContext, "Align"));
        addComponent(new Wait(0.3, "Wait for 0.3 sec"));
        addComponent(new GiroTurn(180.0, 0.1, 20, opModeContext, "Straighten out"));
        addComponent(new SidePressBeaconButton(opModeContext, "Press Button"));

        //Go to second beacon
        addComponent(new GiroDriveAlongWallEncoder(distanceFromWall, 180.0, -0.5, 3000, opModeContext, "Range sensor drive along wall"));
        addComponent(new GiroDriveAlongWallLine(distanceFromWall, 180.0, -0.25, 2000, opModeContext, "Range sensor drive along wall to line"));
        //sm.addComponent(new GiroDriveToLine(180, 0.7, 8000, this.configuration, "Drive to white line"));

        //Press second beacon
        addComponent(new GiroDriveToLine(180, 0.05, 500, opModeContext, "Adjust to white line"));
        addComponent(new Wait(0.3, "Wait for 0.3 sec"));
        addComponent(new GiroTurn(180.0, opModeContext, "Straighten out"));
        addComponent(new SidePressBeaconButton(opModeContext, "Press Button"));
    }
}
