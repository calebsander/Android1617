package org.gearticks.autonomous.velocity.components.velocity.single;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent.DefaultTransition;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class MoveShooterDown extends AutonomousComponentHardware<VelocityConfiguration, DefaultTransition> {
    /**
     * @param opModeContext - the OpModeContext this is running in
     * @param id - descriptive name for logging
     */
    public MoveShooterDown(OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(opModeContext, DefaultTransition.class, id);
    }

    @Override
    public void setup() {
        super.setup();
        this.configuration.resetAutoShooter();
    }

    @Override
    public DefaultTransition run() {
        final DefaultTransition superTransition = super.run();
        if (superTransition != null) return superTransition;

        this.configuration.advanceShooterToDownAutonomous();
        if (this.configuration.isShooterDown()) return DefaultTransition.DEFAULT;
        else return null;
    }
}