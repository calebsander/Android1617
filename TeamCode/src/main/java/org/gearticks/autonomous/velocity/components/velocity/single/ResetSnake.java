package org.gearticks.autonomous.velocity.components.velocity.single;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;

public class ResetSnake extends AutonomousComponentHardware<VelocityConfiguration> {
    private final boolean waitAfter;

    /**
     * @param waitAfter - whether to wait after the reset
     * @param opModeContext - the OpModeContext this is running in
     * @param id - descriptive name for logging
     */
    public ResetSnake(boolean waitAfter, OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(opModeContext.configuration, id);
        this.waitAfter = waitAfter;
    }

    @Override
    public void setup() {
        super.setup();
        this.configuration.snake.setPosition(MotorConstants.SNAKE_V2_HOLDING);
    }

    @Override
    public int run() {
        final int superTransition = super.run();
        if (superTransition != NOT_DONE) return superTransition;

        if (!this.waitAfter || this.stageTimer.seconds() > MotorConstants.SNAKE_V2_TIME_TO_MOVE) return NEXT_STATE;
        else return NOT_DONE;
    }
}
