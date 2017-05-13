package org.gearticks.components.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent;
import org.gearticks.components.generic.component.ParallelComponent;
import org.gearticks.components.generic.statemachine.LinearStateMachine;
import org.gearticks.components.hardwareagnostic.component.GiroBananaTurnEncoder;
import org.gearticks.components.velocity.component.drive.GiroDriveAlongWallEncoder;
import org.gearticks.components.velocity.component.drive.GiroDriveAlongWallLine;
import org.gearticks.components.hardwareagnostic.component.GiroDriveEncoderNoStop;
import org.gearticks.components.hardwareagnostic.component.GiroTurn;
import org.gearticks.components.hardwareagnostic.component.Stopped;
import org.gearticks.components.hardwareagnostic.component.Wait;
import org.gearticks.components.velocity.component.composite.Shoot3Balls;
import org.gearticks.components.velocity.component.composite.SidePressBeaconButton;
import org.gearticks.components.velocity.component.servo.DeploySideRollers;
import org.gearticks.components.velocity.component.servo.RaiseSideRollers;
import org.gearticks.components.velocity.component.motor.RunIntake;
import org.gearticks.components.velocity.opmode.generic.InitializedAutonomous;
import org.gearticks.hardware.configurations.VelocityConfiguration;

@Autonomous
public class RedBeaconFirstAutonomous extends InitializedAutonomous {
    private static final int DISTANCE_FROM_WALL = 9;

    protected OpModeComponent<?> getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        final LinearStateMachine sm = new LinearStateMachine();

        final LinearStateMachine driveAndBTurn = new LinearStateMachine();
        driveAndBTurn.addComponent(new GiroDriveEncoderNoStop(315.0, 0.5, 5000, opModeContext, "Drive forward"));
        //driveAndBTurn.addComponent(new BananaTurnNoGiro(0.0, 0.6, 5000, opModeContext, "Banana turn to wall"));
        driveAndBTurn.addComponent(new GiroBananaTurnEncoder(-45.0, 0.0, 0.9, 6000, 0.18, 0.25, 3.0, opModeContext, "Banana turn to 0.0"));
        final LinearStateMachine rollers = new LinearStateMachine();
        rollers.addComponent(new Wait(2.0, "Wait for 2 secs"));
        rollers.addComponent(new DeploySideRollers(opModeContext, "Deploy side rollers"));
        final ParallelComponent intakeAndDrive = new ParallelComponent();
        intakeAndDrive.addComponent(new RunIntake(4.5, false, opModeContext, "Intake particle"));
        intakeAndDrive.addComponent(rollers);
        intakeAndDrive.addComponent(driveAndBTurn);

        //Get to far beacon
        sm.addComponent(new RunIntake(0.25, false, opModeContext, "Wagglers out"));
        sm.addComponent(new RunIntake(0.25, false, true, opModeContext, "Reverse to make sure wags are out"));
        sm.addComponent(intakeAndDrive);
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 0.0, 0.3, 4000, opModeContext, "Get to far beacon"));

        //Press beacon
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 0.0, -0.175, 500, opModeContext, "Adjust to white line"));
        sm.addComponent(new GiroTurn(0.0, opModeContext, "Straighten out"));
        sm.addComponent(new SidePressBeaconButton(false, opModeContext, "Press Button"));

        //Get to near beacon
        sm.addComponent(new GiroDriveAlongWallEncoder(DISTANCE_FROM_WALL, 0.0, -0.6, 3000, opModeContext, "Range sensor drive along wall"));
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 0.0, -0.3, 3000, opModeContext, "Get to near beacon"));

        //Press beacon
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 0.0, 0.10, 500, opModeContext, "Adjust to white line"));
        sm.addComponent(new GiroTurn(0.0, opModeContext, "Straighten out"));
        sm.addComponent(new SidePressBeaconButton(false, opModeContext, "Press Button"));

        //Shoot
        sm.addComponent(new RaiseSideRollers(opModeContext, "Raise rollers"));
        sm.addComponent(new GiroBananaTurnEncoder(0.0, 9.0, -0.4, 400, opModeContext, "Banana turn to 9"));
        sm.addComponent(new GiroBananaTurnEncoder(9, 90.0, 0.5, 500, 0.1, 0.2, 1.0, opModeContext, "banana turn to shoot"));
        sm.addComponent(new Shoot3Balls(true, opModeContext, "Shoot"));

        //Cap ball
        sm.addComponent(new GiroTurn(70.0, 0.1, 3, 2.0, opModeContext, "turn to cap ball"));
        sm.addComponent(new GiroBananaTurnEncoder(70.0, 130.0, 1.0, 4000, 0.25, 0.3, 3.0, opModeContext, "Hit cap ball"));


        sm.addComponent(new Stopped(opModeContext));

        return sm;
    }

    protected double targetHeading() {
        return 315.0;
    }
}
