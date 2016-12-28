package org.gearticks.autonomous.velocity.components;

import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class MoveShooterDown extends AutonomousComponentVelocityBase {

    /**
     * @param configuration - config file
     * @param id - descriptive name for logging
     */
    public MoveShooterDown(VelocityConfiguration configuration, String id) {
        super(configuration, id);
    }

    @Override
    public void setup() {
        super.setup();
        this.configuration.resetAutoShooter();
    }

    @Override
    public int run() {
        final int superTransition = super.run();
        if (superTransition != NOT_DONE) return superTransition;

        this.configuration.advanceShooterToDown();
        if (this.configuration.isShooterDown()) return NEXT_STATE;
        else return NOT_DONE;
    }

    @Override
    public void tearDown() {
        super.tearDown();
        this.configuration.resetAutoShooter();
    }
}