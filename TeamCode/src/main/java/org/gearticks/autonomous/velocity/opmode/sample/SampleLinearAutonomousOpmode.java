package org.gearticks.autonomous.velocity.opmode.sample;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.Stopped;
import org.gearticks.autonomous.velocity.components.velocity.composite.Shoot2Balls;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.vuforia.VuforiaConfiguration;

@Autonomous
@Disabled
public class SampleLinearAutonomousOpmode extends VelocityBaseOpMode {
    protected AutonomousComponent getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        final LinearStateMachine sm = new LinearStateMachine();
        //Shoot 2 balls

        sm.addComponent(new Shoot2Balls(false, opModeContext, "Load ball"));

        sm.addComponent(new Stopped(opModeContext));

        return sm;
    }
    protected boolean isV2() {
        return false;
    }
}
