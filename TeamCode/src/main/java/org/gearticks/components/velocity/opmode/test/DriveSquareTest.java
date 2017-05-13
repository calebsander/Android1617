package org.gearticks.components.velocity.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent;
import org.gearticks.components.generic.statemachine.LinearStateMachine;
import org.gearticks.components.hardwareagnostic.component.GiroDriveEncoder;
import org.gearticks.components.hardwareagnostic.component.GiroTurn;
import org.gearticks.components.hardwareagnostic.component.Wait;
import org.gearticks.components.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;

@SuppressWarnings("deprecation")
@Autonomous
@Disabled
public class DriveSquareTest extends VelocityBaseOpMode {
    protected OpModeComponent<?> getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        final LinearStateMachine sm = new LinearStateMachine();
        for (int side = 0; side < 4; side++) {
            sm.addComponent(new GiroDriveEncoder(90.0 * side, 0.2, 4000, opModeContext, "Drive for 2000 ticks heading forward"));
            sm.addComponent(new Wait(0.5, "Wait for 0.5 sec"));
            sm.addComponent(new GiroTurn(90.0 * (side + 1), opModeContext, "Turn right 90"));
        }
        return sm;
    }
    protected boolean isV2() {
        return false;
    }
}
