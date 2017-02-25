package org.gearticks.autonomous.velocity.components.velocity.single;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class MoveShooterDown extends AutonomousComponentHardware<VelocityConfiguration> {
    /**
     * @param opModeContext - the OpModeContext this is running in
     * @param id - descriptive name for logging
     */
    public MoveShooterDown(OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(opModeContext, id);
    }

    @Override
    public void setup() {
        super.setup();
        this.configuration.resetAutoShooter();
    }

    @Override
    public Transition run() {
        final Transition superTransition = super.run();
        if (superTransition != null) return superTransition;

        this.configuration.advanceShooterToDownWithEncoder(true);
        if (this.configuration.isShooterDown()) return NEXT_STATE;
        else return null;
    }
}