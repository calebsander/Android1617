package org.gearticks.autonomous.velocity.components;

import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class LoadBall extends AutonomousComponentVelocityBase {

    /**
     * @param configuration - config file
     * @param id - descriptive name for logging
     */
    public LoadBall(VelocityConfiguration configuration, String id) {
        super(configuration, id);
    }

    @Override
    public void setup() {
        super.setup();
        this.configuration.snake.setPosition(VelocityConfiguration.MotorConstants.SNAKE_DUMPING);
    }

    @Override
    public int run() {
        final int superTransition = super.run();
        if (superTransition != NOT_DONE) return superTransition;

        this.getLogger().info("LoadBall - timer = " + this.stageTimer.seconds());
        if (this.stageTimer.seconds() > 0.7) return NEXT_STATE;
        else return NOT_DONE;
    }
}
