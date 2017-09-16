package org.gearticks.components.velocity.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent;
import org.gearticks.components.generic.statemachine.LinearStateMachine;
import org.gearticks.components.hardwareagnostic.component.GiroDriveEncoder;
import org.gearticks.components.hardwareagnostic.component.GiroTurn;
import org.gearticks.components.hardwareagnostic.component.Stopped;
import org.gearticks.components.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;

@Autonomous
@Disabled
public class StaticTest extends VelocityBaseOpMode {
    protected OpModeComponent<?> getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
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
