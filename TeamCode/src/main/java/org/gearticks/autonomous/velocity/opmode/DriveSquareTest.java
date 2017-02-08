package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.deprecated.GyroDriveEncoder;
import org.gearticks.autonomous.velocity.components.deprecated.GyroTurn;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;

@Autonomous
@Disabled
public class DriveSquareTest extends VelocityBaseOpMode {
    protected AutonomousComponent getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        final LinearStateMachine sm = new LinearStateMachine();
        for (int side = 0; side < 4; side++) {
            sm.addComponent(new GyroDriveEncoder(90.0 * side, 0.2, 4000, opModeContext, "Drive for 2000 ticks heading forward"));
            sm.addComponent(new Wait(0.5, "Wait for 0.5 sec"));
            sm.addComponent(new GyroTurn(90.0 * (side + 1), opModeContext, "Turn right 90"));
        }
        return sm;
    }
    protected boolean isV2() {
        return false;
    }
}
