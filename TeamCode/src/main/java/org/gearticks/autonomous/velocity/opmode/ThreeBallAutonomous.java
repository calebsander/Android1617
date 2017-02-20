package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.experimental.GiroBananaTurnEncoder;
import org.gearticks.autonomous.velocity.components.generic.DebugPause;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.Stopped;
import org.gearticks.autonomous.velocity.components.velocity.composite.Shoot2Balls;
import org.gearticks.autonomous.velocity.components.velocity.composite.Shoot3Balls;
import org.gearticks.autonomous.velocity.components.velocity.single.BananaTurnNoGiro;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.RunIntake;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;

@Autonomous
public class ThreeBallAutonomous extends VelocityBaseOpMode {
    private static final int DISTANCE_FROM_WALL = 9;

    @Override
    protected void loopBeforeStart() {
        super.loopBeforeStart();
        this.configuration.safeShooterStopper(VelocityConfiguration.MotorConstants.SHOOTER_STOPPER_UP);
        this.configuration.clutch.setPosition(VelocityConfiguration.MotorConstants.CLUTCH_V2_CLUTCHED);
        this.configuration.advanceShooterToDown();
    }

    protected AutonomousComponent getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        final LinearStateMachine sm = new LinearStateMachine();

        int distanceFromWall = 10;

        //Shoot 3 balls
        //sm.addComponent(new Shoot3Balls(opModeContext, "Shoot 3 balls"));
        //sm.addComponent(new DebugPause(opModeContext));

        //sm.addComponent(new GiroDriveEncoder(25.0, 0.75, 2000, opModeContext, "Drive forward"));
        sm.addComponent(new BananaTurnNoGiro(40.0, 0.25, 3000, opModeContext, "Banana Turn right"));


        sm.addComponent(new DebugPause(opModeContext));
        sm.addComponent(new BananaTurnNoGiro(180.0, 0.25, 10000, opModeContext, "Banana Turn right"));




        sm.addComponent(new Stopped(opModeContext));

        return sm;
    }
    protected boolean isV2() {
        return true;
    }

    protected double targetHeading() {
        return 25.0;
    }
}
