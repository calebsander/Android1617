package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.velocity.single.ClutchClutch;
import org.gearticks.autonomous.velocity.components.velocity.single.LoadBall;
import org.gearticks.autonomous.velocity.components.velocity.single.MoveShooterDown;
import org.gearticks.autonomous.velocity.components.velocity.single.ResetSnake;
import org.gearticks.autonomous.velocity.components.velocity.single.RunIntake;
import org.gearticks.autonomous.velocity.components.velocity.single.ShootBall;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class IntakeClutched extends LinearStateMachine {
    /**
     *
     * @param time
     * @param opModeContext - the OpModeContext this is running in
     * @param id - descriptive name for logging
     */
    public IntakeClutched(double time, OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(id);
        addComponent(new ClutchClutch(opModeContext, "Clutch clutch"));
        addComponent(new RunIntake(time, opModeContext, "Run intake"));

    }
}
