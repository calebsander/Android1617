package org.gearticks.components.velocity.component.servo;

import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent.DefaultTransition;
import org.gearticks.components.generic.component.OpModeComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;

public class ResetSnake extends OpModeComponentHardware<VelocityConfiguration, DefaultTransition> {
    private final boolean waitAfter;

    /**
     * @param waitAfter - whether to wait after the reset
     * @param opModeContext - the OpModeContext this is running in
     * @param id - descriptive name for logging
     */
    public ResetSnake(boolean waitAfter, OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(opModeContext, DefaultTransition.class, id);
        this.waitAfter = waitAfter;
    }

    @Override
    public void setup() {
        super.setup();
        this.configuration.snake.setPosition(MotorConstants.SNAKE_V2_HOLDING);
    }

    @Override
    public DefaultTransition run() {
        final DefaultTransition superTransition = super.run();
        if (superTransition != null) return superTransition;

        if (!this.waitAfter || this.stageTimer.seconds() > MotorConstants.SNAKE_V2_TIME_TO_MOVE) return DefaultTransition.DEFAULT;
        else return null;
    }
}
