package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
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

        sm.addComponent(new RunIntake(1.3, true, opModeContext, "Intake particle"));

        //Get to far beacon
        sm.addComponent(new GiroDriveEncoder(225.0, 0.4, 1000, opModeContext, "Drive forward"));
        sm.addComponent(new GiroTurn(45.0, opModeContext, "Straighten out"));
        sm.addComponent(new DeploySideRollers(opModeContext, "Deploy side rollers"));
        sm.addComponent(new GiroBananaTurnEncoder(45.0, 0.0, 0.4, 8000, opModeContext, "Banana turn to wall"));
        sm.addComponent(new DebugPause(opModeContext));
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 0.0, 0.25, 2000, opModeContext, "Get to far beacon"));

        //Press beacon
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 0.0, -0.17, 500, opModeContext, "Adjust to white line"));
        sm.addComponent(new GiroTurn(0.0, opModeContext, "Straighten out"));
        sm.addComponent(new SidePressBeaconButton(opModeContext, "Press Button"));

        //Get to near beacon
        sm.addComponent(new GiroDriveAlongWallEncoder(DISTANCE_FROM_WALL, 0.0, -0.8, 3000, opModeContext, "Range sensor drive along wall"));
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL + 1, 0.0, -0.25, 2000, opModeContext, "Range sensor drive along wall to line"));

        //Press beacon
        sm.addComponent(new GiroDriveAlongWallLine(DISTANCE_FROM_WALL, 0.0, 0.17, 500, opModeContext, "Adjust to white line"));
        sm.addComponent(new GiroTurn(0.0, opModeContext, "Straighten out"));
        sm.addComponent(new SidePressBeaconButton(opModeContext, "Press Button"));

        //Shoot and Cap ball
        sm.addComponent(new GiroBananaTurnEncoder(0.0, 240.0, -0.6, 3000, opModeContext, "Banana turn to 270"));
        sm.addComponent(new DebugPause(opModeContext));
        sm.addComponent(new Wait(0.3, "Wait for 0.3 seconds"));
        sm.addComponent(new Shoot3Balls(true, opModeContext, "Shoot"));
        sm.addComponent(new GiroTurn(270.0, opModeContext, "Straighten out"));
        sm.addComponent(new GiroDriveEncoder(225.0, 0.4, 3000, opModeContext, "Drive to cap ball"));


        sm.addComponent(new Stopped(opModeContext));

        return sm;
    }
    protected boolean isV2() {
        return true;
    }

    protected double targetHeading() {
        return 26.0;
    }
}
