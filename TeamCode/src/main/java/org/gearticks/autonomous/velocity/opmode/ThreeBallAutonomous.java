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
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.RunIntake;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;

@Autonomous
@Disabled
public class ThreeBallAutonomous extends VelocityBaseOpMode {
    protected AutonomousComponent getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        final LinearStateMachine sm = new LinearStateMachine();

        int distanceFromWall = 10;
        sm.addComponent(new DisengageBeaconServo(opModeContext, "Disengage beacon button"));

        //Pickup ball
        sm.addComponent(new RunIntake(3.0, false, opModeContext, "Run intake"));

        sm.addComponent(new DebugPause(opModeContext));


        //Shoot balls
        sm.addComponent(new GiroDriveEncoder(18.5, 0.5, 1000, opModeContext, "Drive Forward"));
        sm.addComponent(new DebugPause(opModeContext));

        sm.addComponent(new Shoot2Balls(true, opModeContext, "MoveShooterDown"));


        sm.addComponent(new GiroBananaTurnEncoder(0.0, 20.0, 0.25, 1000, opModeContext, "Banana Turn right"));




        sm.addComponent(new Stopped(opModeContext));

        return sm;
    }
    protected boolean isV2() {
        return true;
    }
}
