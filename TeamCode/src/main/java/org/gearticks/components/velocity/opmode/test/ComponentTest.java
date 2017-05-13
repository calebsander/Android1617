package org.gearticks.components.velocity.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.statemachine.LinearStateMachine;
import org.gearticks.components.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.components.generic.component.OpModeComponent;

@Autonomous
public class ComponentTest extends VelocityBaseOpMode {
    protected OpModeComponent<?> getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        final LinearStateMachine sm = new LinearStateMachine();

        //Add components here

        return sm;
    }
    protected boolean isV2() {
        return true;
    }
}
