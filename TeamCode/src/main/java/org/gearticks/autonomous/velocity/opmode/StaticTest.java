package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.generic.Stopped;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;

@Autonomous
@Disabled
public class StaticTest extends VelocityBaseOpMode {
    protected AutonomousComponent getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        final LinearStateMachine sm = new LinearStateMachine();

        for (int i = 0; i < 6; i++) {
            sm.addComponent(new GiroDriveEncoder(0.0, 0.7, 1000, opModeContext, "Drive forward"));
            sm.addComponent(new GiroTurn(180.0, opModeContext, "Turn backward"));
            sm.addComponent(new GiroDriveEncoder(1800.0, 0.7, 1000, opModeContext, "Drive backward"));
            sm.addComponent(new GiroTurn(0.0, opModeContext, "Turn forward"));
        }
        sm.addComponent(new Stopped(opModeContext));

        return sm;
    }
    protected boolean isV2() {
        return false;
    }
}
