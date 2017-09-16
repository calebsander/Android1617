package org.gearticks.components.velocity.component.composite;

import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.statemachine.LinearStateMachine;
import org.gearticks.components.hardwareagnostic.component.GiroBananaTurnEncoder;
import org.gearticks.components.velocity.component.drive.GiroDriveAlongWallEncoder;
import org.gearticks.components.velocity.component.drive.GiroDriveAlongWallLine;
import org.gearticks.components.hardwareagnostic.component.GiroDriveEncoder;
import org.gearticks.components.velocity.component.drive.GiroDriveToLine;
import org.gearticks.components.hardwareagnostic.component.GiroTurn;
import org.gearticks.components.velocity.component.servo.RaiseSideRollers;
import org.gearticks.components.velocity.component.servo.DeploySideRollers;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class RedSideAutonomous extends LinearStateMachine {
    public RedSideAutonomous(int distanceFromWall, OpModeContext<VelocityConfiguration> opModeContext) {
        super();

        //Drive to wall
        addComponent(new DeploySideRollers(opModeContext, "Deploy rollers"));
        addComponent(new GiroBananaTurnEncoder(0.0, -70.0, 0.7, 500, opModeContext, "BTL"));
        addComponent(new GiroBananaTurnEncoder(-70.0, 0.0, 0.6, 7000, opModeContext, "BTR"));
        addComponent(new GiroTurn(0.0, opModeContext, "Straighten out"));
        addComponent(new GiroDriveAlongWallLine(distanceFromWall + 1, 0.0, 0.4, 6000, opModeContext, "Range sensor drive along wall"));

        //Press beacon
        addComponent(new GiroDriveEncoder(0.0, -0.1, 300, opModeContext, "Center on beacon"));
        addComponent(new GiroTurn(0.0, opModeContext, "Straighten out"));
        addComponent(new SidePressBeaconButton(false, opModeContext, "Press Button"));

        //Go to second beacon
        addComponent(new GiroDriveAlongWallEncoder(distanceFromWall+1, 0.0, -0.7, 3000, opModeContext, "Range sensor drive along wall"));
        addComponent(new GiroDriveAlongWallLine(distanceFromWall+1, 0.0, -0.25, 4000, opModeContext, "Range sensor drive along wall to line"));

        //Press beacon
        addComponent(new GiroDriveToLine(0, 0.05, 500, opModeContext, "Adjust to white line"));
        addComponent(new GiroTurn(0.0, opModeContext, "Straighten out"));
        addComponent(new SidePressBeaconButton(false, opModeContext, "Press Button"));

        //Cap ball
        addComponent(new RaiseSideRollers(opModeContext, "Raise rollers"));
        addComponent(new GiroBananaTurnEncoder(0.0, 9.0, -0.4, 400, opModeContext, "Banana turn to 9"));
        addComponent(new GiroBananaTurnEncoder(9, 90.0, 0.5, 500, 0.15, 0.2, 1.0, opModeContext, "banana turn to shoot"));
        addComponent(new GiroTurn(70.0, 0.1, 3, 2.0, opModeContext, "turn to cap ball"));
        addComponent(new GiroBananaTurnEncoder(70.0, 130.0, 1.0, 4000, 0.25, 0.3, 3.0, opModeContext, "Hit cap ball"));
    }
}
