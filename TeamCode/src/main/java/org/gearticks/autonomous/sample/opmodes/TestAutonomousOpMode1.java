package org.gearticks.autonomous.sample.opmodes;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.deprecated.GyroTurn;
import org.gearticks.autonomous.velocity.components.deprecated.GyroDriveEncoder;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class TestAutonomousOpMode1 extends VelocityBaseOpMode {
    private static final double NEW_TARGET = 100.0;

    protected AutonomousComponent getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        final LinearStateMachine sm = new LinearStateMachine();
        sm.addComponent(new GyroDriveEncoder(0.0, 1.0, 2000, opModeContext, "drive1"));
        sm.addComponent(new GyroTurn(NEW_TARGET, opModeContext, "turn"));
        sm.addComponent(new GyroDriveEncoder(NEW_TARGET, 1.0, 2000, opModeContext, "drive2"));
        return sm;
    }
    protected boolean isV2() {
        return false;
    }
}
