package org.gearticks.autonomous.velocity.components;

import android.support.annotation.NonNull;

import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * Created by irene on 12/26/2016.
 */

public class LoadBall extends AutonomousComponentVelocityBase {

    /**
     *
     * @param configuration - config file
     * @param id - descriptive name for logging
     */
    public LoadBall(@NonNull VelocityConfiguration configuration, String id) {
        super(configuration, id);
    }

    @Override
    public void setup(int inputPort) {
        super.setup(inputPort);
        this.getConfiguration().snake.setPosition(VelocityConfiguration.MotorConstants.SNAKE_DUMPING);
    }

    @Override
    public int run() {
        int transition = super.run();

        this.getLogger().info("LoadBall - timer = " + this.getStageTimer().seconds());

        if (this.getStageTimer().seconds() > 0.7) {
            transition = 1;
        }


        return transition;
    }

    @Override
    public void tearDown() {
        super.tearDown();
    }
}
