package org.gearticks.autonomous.velocity.components;

import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class ResetSnake extends AutonomousComponentHardware<VelocityConfiguration> {
    /**
     * @param configuration - config file
     * @param id - descriptive name for logging
     */
    public ResetSnake(VelocityConfiguration configuration, String id) {
        super(configuration, id);
    }

    @Override
    public void setup() {
        super.setup();
        this.configuration.snake.setPosition(VelocityConfiguration.MotorConstants.SNAKE_HOLDING);
    }

    @Override
    public int run() {
        final int superTransition = super.run();
        if (superTransition != NOT_DONE) return superTransition;

        this.getLogger().info("ResetSnake - timer = " + this.stageTimer.seconds());
        if (this.stageTimer.seconds() > 0.5) return NEXT_STATE;
        else return NOT_DONE;
    }
}
