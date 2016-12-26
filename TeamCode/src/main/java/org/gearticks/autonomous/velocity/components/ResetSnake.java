package org.gearticks.autonomous.velocity.components;

import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * Created by irene on 12/26/2016.
 */

public class ResetSnake extends AutonomousComponentVelocityBase {
    public ResetSnake(VelocityConfiguration configuration, String id) {
        super(configuration, id);
    }

    @Override
    public void setup(int inputPort) {
        super.setup(inputPort);
        this.getConfiguration().snake.setPosition(VelocityConfiguration.MotorConstants.SNAKE_HOLDING);

    }

    @Override
    public int run() {
        int transition = super.run();

        if (this.getStageTimer().seconds() > 0.5) {
            transition = 1;
        }


        return transition;
    }

    @Override
    public void tearDown() {
        super.tearDown();
    }
}
