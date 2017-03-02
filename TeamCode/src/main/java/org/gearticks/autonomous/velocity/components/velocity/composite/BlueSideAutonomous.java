package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.ParallelComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.GiroBananaTurnEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallLine;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveToLine;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.velocity.single.RaiseSideRollers;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class BlueSideAutonomous extends LinearStateMachine {
    private static final int DISTANCE_FROM_WALL = 9;
    private static final double TURN_POWER = 0.1;
    private static final double TURN_ACCEL = 0.1;
    private static final double TURN_RANGE = 1.0;

    public BlueSideAutonomous(OpModeContext<VelocityConfiguration> opModeContext) {
        super();

        //Drive to wall
        addComponent(new GiroBananaTurnEncoder(0.0, 18.0, 0.7, 500, TURN_POWER, TURN_ACCEL, TURN_RANGE, opModeContext, "BTR 18"));
        addComponent(new GiroBananaTurnEncoder(18.0, 90.0, 0.8, 7500, TURN_POWER, TURN_ACCEL, TURN_RANGE, opModeContext, "BTR 90"));
        addComponent(new GiroTurn(180.0, 0.1, 10, 2.0, opModeContext, "Straighten out"));
        addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL - 1, 180.0, 0.5, 6000, opModeContext, "Drive to beacon"));

        //Press beacon
        addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, -0.17, 500, opModeContext, "Adjust to white line"));
        addComponent(new GiroDriveAlongWallEncoder(DISTANCE_FROM_WALL, 180.0, -0.17, 25, opModeContext, "Align"));
        addComponent(new GiroTurn(180.0, opModeContext, "Straighten out"));
        addComponent(new SidePressBeaconButton(true, opModeContext, "Press Button"));

        //Go to second beacon
        addComponent(new GiroDriveAlongWallEncoder(DISTANCE_FROM_WALL, 180.0, -0.8, 3000, opModeContext, "Range sensor drive along wall"));
        addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL + 1, 180.0, -0.25, 2000, opModeContext, "Range sensor drive along wall to line"));

        //Press second beacon
        addComponent(new GiroDriveToLine(180, 0.05, 500, opModeContext, "Adjust to white line"));
        addComponent(new GiroTurn(180.0, opModeContext, "Straighten out"));
        addComponent(new SidePressBeaconButton(true, opModeContext, "Press Button"));

        //Cap ball
        final ParallelComponent driveOffWall = new ParallelComponent("Drive off wall");
        driveOffWall.addComponent(new RaiseSideRollers(opModeContext, "Raise rollers"));
        driveOffWall.addComponent(new GiroBananaTurnEncoder(180.0, 235.0, 0.6, 700, opModeContext, "BTR 235")); //using lower power to avoid getting stuck on wall
        addComponent(driveOffWall);
        addComponent(new GiroDriveEncoder(235.0, 1.0, 6000, opModeContext, "Hit cap ball and park"));
    }
}
