package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.velocity.composite.Shoot3Balls;
import org.gearticks.autonomous.velocity.components.velocity.single.MoveShooterDown;
import org.gearticks.autonomous.velocity.components.velocity.single.RunIntake;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.autonomous.generic.component.AutonomousComponent;

@Autonomous
public class ComponentTest extends VelocityBaseOpMode {
    protected AutonomousComponent getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        final LinearStateMachine sm = new LinearStateMachine();

        sm.addComponent(new MoveShooterDown(opModeContext, "Move shooter down"));
        sm.addComponent(new Shoot3Balls(opModeContext, "Shoot 3 balls"));

        return sm;
    }
    protected boolean isV2() {
        return true;
    }
}
