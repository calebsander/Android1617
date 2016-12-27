package org.gearticks.autonomous.velocity.components;

import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * Created by irene on 12/26/2016.
 */

public class MoveShooterDown extends AutonomousComponentVelocityBase {
    public MoveShooterDown(VelocityConfiguration configuration, String id) {
        super(configuration, id);
        this.getConfiguration().resetAutoShooter();
    }

    @Override
    public void setup(int inputPort) {
        super.setup(inputPort);
    }

    @Override
    public int run() {
        int transition = super.run();

        this.getConfiguration().advanceShooterToDown();
        if (this.getConfiguration().isShooterDown()) {
            transition = 1;
        }

        return transition;
    }

    @Override
    public void tearDown() {
        super.tearDown();
        this.getConfiguration().resetAutoShooter();
    }
}