package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.Stopped;
import org.gearticks.autonomous.velocity.components.velocity.composite.SidePressBeaconButton;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;

@Autonomous
public class BlueBeaconTest extends VelocityBaseOpMode {
    private static final int DISTANCE_FROM_WALL = 9;

    @Override
    protected void loopBeforeStart() {
        super.loopBeforeStart();
    }

    @Override
    protected void matchStart() {
        super.matchStart();
    }

    protected AutonomousComponent getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        final LinearStateMachine sm = new LinearStateMachine();

        sm.addComponent(new SidePressBeaconButton(true, opModeContext, "Press Button"));
        sm.addComponent(new Stopped(opModeContext));

        return sm;
    }
    protected boolean isV2() {
        return true;
    }
}
