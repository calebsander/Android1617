package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.velocity.single.ClutchClutch;
import org.gearticks.autonomous.velocity.components.velocity.single.EngageClutch;
import org.gearticks.autonomous.velocity.components.velocity.single.RunIntake;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class IntakeEngaged extends LinearStateMachine {
    /**
     *
     * @param time
     * @param opModeContext - the OpModeContext this is running in
     * @param id - descriptive name for logging
     */
    public IntakeEngaged(double time, OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(id);
        addComponent(new EngageClutch(opModeContext, "Engage clutch"));
        addComponent(new RunIntake(time, opModeContext, "Run intake"));

    }
}
