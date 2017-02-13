package org.gearticks.autonomous.velocity.components.velocity.single;

import android.support.annotation.NonNull;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;

public class LoadBall extends AutonomousComponentHardware<VelocityConfiguration> {
    /**
     * @param configuration - config file
     * @param id - descriptive name for logging
     */
    public LoadBall(@NonNull VelocityConfiguration configuration, String id) {
        super(configuration, id);
    }

    @Override
    public void setup() {
        super.setup();
        this.configuration.snake.setPosition(MotorConstants.SNAKE_V2_DUMPING);
    }

    @Override
    public Transition run() {
        final Transition superTransition = super.run();
        if (superTransition != null) return superTransition;

        if (this.stageTimer.seconds() > MotorConstants.SNAKE_V2_TIME_TO_MOVE * 1.5) return NEXT_STATE;
        else return null;
    }
}
