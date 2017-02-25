package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.ParallelComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.DebugPause;
import org.gearticks.autonomous.velocity.components.generic.GiroBananaTurnEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallLine;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.generic.Stopped;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.components.velocity.composite.Shoot3Balls;
import org.gearticks.autonomous.velocity.components.velocity.composite.SidePressBeaconButton;
import org.gearticks.autonomous.velocity.components.velocity.single.DeploySideRollers;
import org.gearticks.autonomous.velocity.components.velocity.single.RaiseSideRollers;
import org.gearticks.autonomous.velocity.components.velocity.single.RunIntake;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;

@Autonomous
public class RedBeaconFirstAutonomous extends VelocityBaseOpMode {
    private static final int DISTANCE_FROM_WALL = 9;

    @Override
    protected void loopBeforeStart() {
        super.loopBeforeStart();
        this.configuration.safeShooterStopper(VelocityConfiguration.MotorConstants.SHOOTER_STOPPER_UP);
        this.configuration.advanceShooterToDown();
        this.configuration.beaconPresserFrontIn();
        this.configuration.beaconPresserBackIn();
    }

    protected AutonomousComponent getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        final LinearStateMachine sm = new LinearStateMachine();

        final LinearStateMachine waitAndDrive = new LinearStateMachine();
        waitAndDrive.addComponent(new Wait(1.0, "Wait for 1"));
        waitAndDrive.addComponent(new GiroDriveEncoder(45.0, 0.2, 2000, opModeContext, "Drive forward"));
        final LinearStateMachine intakeAndRollers = new LinearStateMachine();
        intakeAndRollers.addComponent(new RunIntake(1.3, true, opModeContext, "Intake particle"));
        intakeAndRollers.addComponent(new DeploySideRollers(opModeContext, "Deploy side rollers"));
        final ParallelComponent intakeAndDrive = new ParallelComponent();
        intakeAndDrive.addComponent(intakeAndRollers);
        intakeAndDrive.addComponent(waitAndDrive);

        //Get to far beacon
        sm.addComponent(intakeAndDrive);
        sm.addComponent(new GiroBananaTurnEncoder(45.0, 0.0, 0.25, 8000, opModeContext, "Banana turn to wall"));
        sm.addComponent(new DebugPause(opModeContext));
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 0.0, 0.15, 2000, opModeContext, "Get to far beacon"));

        //Press beacon
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 0.0, -0.07, 500, opModeContext, "Adjust to white line"));
        sm.addComponent(new GiroTurn(0.0, opModeContext, "Straighten out"));
        //sm.addComponent(new SidePressBeaconButton(opModeContext, "Press Button"));

        //Get to near beacon
        sm.addComponent(new GiroDriveAlongWallEncoder(DISTANCE_FROM_WALL, 0.0, -0.4, 3000, opModeContext, "Range sensor drive along wall"));
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL + 1, 0.0, -0.15, 2000, opModeContext, "Range sensor drive along wall to line"));

        //Press beacon
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 0.0, 0.07, 500, opModeContext, "Adjust to white line"));
        sm.addComponent(new GiroTurn(0.0, opModeContext, "Straighten out"));
        //sm.addComponent(new SidePressBeaconButton(opModeContext, "Press Button"));
        sm.addComponent(new DebugPause(opModeContext));

        //Shoot
        sm.addComponent(new RaiseSideRollers(opModeContext, "Raise rollers"));
        sm.addComponent(new GiroBananaTurnEncoder(0.0, -7.0, -0.4, 400, opModeContext, "Banana turn to -7"));
        sm.addComponent(new GiroBananaTurnEncoder(-7, 90.0, 0.5, 500, 0.1, 0.2, 1.0, opModeContext, "banana turn to shoot"));
        sm.addComponent(new Shoot3Balls(true, opModeContext, "Shoot"));

        //Cap ball
        sm.addComponent(new GiroTurn(70.0, 0.1, 3, 2.0, opModeContext, "turn to cap ball"));
        sm.addComponent(new GiroBananaTurnEncoder(60.0, 130.0, 1.0, 5000, 0.25, 0.3, 3.0, opModeContext, "Hit cap ball"));


        sm.addComponent(new Stopped(opModeContext));

        return sm;
    }
    protected boolean isV2() {
        return false;
    }

    protected double targetHeading() {
        return 45.0;
    }
}
