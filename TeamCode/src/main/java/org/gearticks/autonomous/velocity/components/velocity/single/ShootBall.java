package org.gearticks.autonomous.velocity.components.velocity.single;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class ShootBall extends AutonomousComponentHardware<VelocityConfiguration> {
    /**
     * @param opModeContext - the OpModeContext this is running in
     * @param id - descriptive name for logging
     */
    public ShootBall(OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(opModeContext.configuration, id);
    }

    @Override
    public void setup() {
        super.setup();
        this.configuration.advanceShooterToShooting();
    }

    @Override
    public int run() {
        final int superTransition = super.run();
        if (superTransition != NOT_DONE) return superTransition;

        if (this.configuration.isShooterAtTarget()) return NEXT_STATE;
        else return NOT_DONE;
    }
}
