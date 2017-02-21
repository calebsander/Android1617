package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.GiroBananaTurnEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallLine;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveToLine;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.velocity.single.RaiseSideRollers;
import org.gearticks.autonomous.velocity.components.velocity.single.DeploySideRollers;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class RedSideAutonomous extends LinearStateMachine {
    public RedSideAutonomous(int distanceFromWall, OpModeContext<VelocityConfiguration> opModeContext) {
        super();

        //Drive to wall
        addComponent(new DeploySideRollers(opModeContext, "Deploy rollers"));
        addComponent(new GiroBananaTurnEncoder(0.0, 70.0, 0.7, 500, opModeContext, "BTL"));
        addComponent(new GiroBananaTurnEncoder(70.0, 0.0, 0.6, 7000, opModeContext, "BTR"));
        addComponent(new GiroTurn(0.0, opModeContext, "Straighten out"));
        addComponent(new GiroDriveAlongWallLine(distanceFromWall + 1, 0.0, 0.4, 6000, opModeContext, "Range sensor drive along wall"));

        //Press beacon
        addComponent(new GiroDriveEncoder(0.0, -0.1, 300, opModeContext, "Center on beacon"));
        addComponent(new GiroTurn(0.0, opModeContext, "Straighten out"));
        addComponent(new SidePressBeaconButton(opModeContext, "Press Button"));

        //Go to second beacon
        addComponent(new GiroDriveAlongWallEncoder(distanceFromWall+1, 0.0, -0.7, 3000, opModeContext, "Range sensor drive along wall"));
        addComponent(new GiroDriveAlongWallLine(distanceFromWall+1, 0.0, -0.25, 4000, opModeContext, "Range sensor drive along wall to line"));
        //sm.addComponent(new GiroDriveToLine(180, 0.7, 8000, this.configuration, "Drive to white line"));

        //Press beacon
        addComponent(new GiroDriveToLine(0, 0.05, 500, opModeContext, "Adjust to white line"));
        addComponent(new GiroTurn(0.0, opModeContext, "Straighten out"));
        addComponent(new SidePressBeaconButton(opModeContext, "Press Button"));

        //Cap ball
        addComponent(new RaiseSideRollers(opModeContext, "Disengage rollers"));
        addComponent(new GiroBananaTurnEncoder(0.0, -135.0, 0.7, 3500, opModeContext, "Banana to face Cap Ball"));
        addComponent(new GiroDriveEncoder(-135.0, 1.0, 5500, opModeContext, "Hit and park"));
    }
}
