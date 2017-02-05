package org.gearticks.autonomous.velocity.components.velocity.single;

import android.support.annotation.NonNull;
import android.util.Log;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;
import org.gearticks.opmodes.utility.Utils;

public class ResetSnake extends AutonomousComponentHardware<VelocityConfiguration> {
    private final boolean waitAfter;

    /**
     * @param configuration - config file
     * @param id - descriptive name for logging
     */
    public ResetSnake(@NonNull VelocityConfiguration configuration, boolean waitAfter, String id) {
        super(configuration, id);
        this.waitAfter = waitAfter;
    }

    @Override
    public void setup() {
        super.setup();
        this.configuration.snake.setPosition(MotorConstants.SNAKE_V2_HOLDING);
    }

    @Override
    public Transition run() {
        final Transition superTransition = super.run();
        if (superTransition != null) return superTransition;

        if (!this.waitAfter || this.stageTimer.seconds() > MotorConstants.SNAKE_V2_TIME_TO_MOVE) return NEXT_STATE;
        else return null;
    }
}
