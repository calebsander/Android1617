package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.ParallelComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.BananaTurnNoGiro;
import org.gearticks.autonomous.velocity.components.generic.DebugPause;
import org.gearticks.autonomous.velocity.components.generic.GiroBananaTurnEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallLine;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoderNoStop;
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
        this.configuration.advanceShooterToDownSlowly();
        this.configuration.beaconPresserFrontIn();
        this.configuration.beaconPresserBackIn();
        this.configuration.engageTopLatch();
    }

    @Override
    protected void matchStart() {
        super.matchStart();
        this.configuration.disengageTopLatch();
    }

    protected AutonomousComponent getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        final LinearStateMachine sm = new LinearStateMachine();

        final LinearStateMachine driveAndBTurn = new LinearStateMachine();
        //waitAndDrive.addComponent(new Wait(0.5, "Wait for 0.5"));
        driveAndBTurn.addComponent(new GiroDriveEncoderNoStop(315.0, 0.5, 4000, opModeContext, "Drive forward"));
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
        sm.addComponent(intakeAndDrive);
	    //sm.addComponent(new DebugPause(opModeContext));
	      sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 0.0, 0.2, 4000, opModeContext, "Get to far beacon"));

        //Press beacon
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 0.0, -0.17, 500, opModeContext, "Adjust to white line"));
        sm.addComponent(new GiroTurn(0.0, opModeContext, "Straighten out"));
        sm.addComponent(new SidePressBeaconButton(false, opModeContext, "Press Button"));

        //Get to near beacon
        sm.addComponent(new GiroDriveAlongWallEncoder(DISTANCE_FROM_WALL, 0.0, -0.8, 3000, opModeContext, "Range sensor drive along wall"));
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 0.0, -0.2, 3000, opModeContext, "Get to near beacon"));

        //Press beacon
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 0.0, 0.075, 500, opModeContext, "Adjust to white line"));
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
    protected boolean isV2() {
        return true;
    }

    protected double targetHeading() {
        return 315.0;
    }
}
