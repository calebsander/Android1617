package org.gearticks.components.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent;
import org.gearticks.components.generic.component.ParallelComponent;
import org.gearticks.components.generic.statemachine.LinearStateMachine;
import org.gearticks.components.hardwareagnostic.component.GiroBananaTurnEncoder;
import org.gearticks.components.velocity.component.drive.GiroDriveAlongWallEncoder;
import org.gearticks.components.velocity.component.drive.GiroDriveAlongWallLine;
import org.gearticks.components.hardwareagnostic.component.GiroTurn;
import org.gearticks.components.hardwareagnostic.component.Stopped;
import org.gearticks.components.velocity.component.composite.Shoot3Balls;
import org.gearticks.components.velocity.component.composite.SidePressBeaconButton;
import org.gearticks.components.velocity.component.servo.DeploySideRollers;
import org.gearticks.components.velocity.component.servo.RaiseSideRollers;
import org.gearticks.components.velocity.component.motor.RunIntake;
import org.gearticks.components.velocity.opmode.generic.InitializedAutonomous;
import org.gearticks.hardware.configurations.VelocityConfiguration;

@Autonomous
public class BlueBeaconFirstAutonomous extends InitializedAutonomous {
    private static final int DISTANCE_FROM_WALL = 10;

    protected OpModeComponent<?> getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        final LinearStateMachine sm = new LinearStateMachine();

        //Get to far beacon
        sm.addComponent(new RunIntake(1.3, false, opModeContext, "Intake particle"));
        final ParallelComponent driveAndDeployRollers = new ParallelComponent();
        driveAndDeployRollers.addComponent(new GiroBananaTurnEncoder(225.0, 180.0, -0.7, 8000, opModeContext, "Banana turn to 180.0"));
        driveAndDeployRollers.addComponent(new DeploySideRollers(opModeContext, "Deploy side rollers"));
        driveAndDeployRollers.addComponent(new RunIntake(3, false, opModeContext, "Intake particle"));
        sm.addComponent(driveAndDeployRollers);
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, -0.3, 4000, opModeContext, "Get to far beacon"));

        //Press beacon
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, 0.15, 500, opModeContext, "Adjust to white line"));
        sm.addComponent(new GiroTurn(180.0, 0.1, 5, opModeContext, "Straighten out"));
        sm.addComponent(new SidePressBeaconButton(true, opModeContext, "Press Button"));

        //Get to near beacon
        sm.addComponent(new GiroDriveAlongWallEncoder(DISTANCE_FROM_WALL, 180.0, 1.0, 3000, opModeContext, "Range sensor drive along wall"));
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, 0.3, 3000, opModeContext, "Range sensor drive along wall to line"));

        //Press beacon
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 180.0, -0.15, 500, opModeContext, "Adjust to white line"));
        sm.addComponent(new GiroTurn(180.0, 0.1, 5, opModeContext, "Straighten out"));
        sm.addComponent(new SidePressBeaconButton(true, opModeContext, "Press Button"));

        //Shoot
        sm.addComponent(new RaiseSideRollers(opModeContext, "Raise rollers"));
        sm.addComponent(new GiroBananaTurnEncoder(180.0, 187.0, -0.4, 400, opModeContext, "Banana turn to 187"));
        sm.addComponent(new GiroBananaTurnEncoder(187, 270.0, 0.5, 500, 0.1, 0.2, 1.0, opModeContext, "banana turn to shoot"));
        sm.addComponent(new GiroTurn(277.0, opModeContext, "Align to vortex"));
        sm.addComponent(new Shoot3Balls(true, opModeContext, "Shoot"));

        //Cap ball
        sm.addComponent(new GiroTurn(290.0, 0.1, 3, 2.0, opModeContext, "turn to cap ball"));
        sm.addComponent(new GiroBananaTurnEncoder(300.0, 230.0, 1.0, 4500, 0.25, 0.3, 3.0, opModeContext, "Hit cap ball"));


        sm.addComponent(new Stopped(opModeContext));

        return sm;
    }

    protected double targetHeading() {
        return 225.0;
    }
}
