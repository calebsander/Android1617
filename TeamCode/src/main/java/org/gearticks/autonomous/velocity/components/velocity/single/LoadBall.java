package org.gearticks.autonomous.velocity.components.velocity.single;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;

public class LoadBall extends AutonomousComponentHardware<VelocityConfiguration> {
    /**
     * @param opModeContext - the OpModeContext this is running in
     * @param id - descriptive name for logging
     */
    public LoadBall(OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(opModeContext.configuration, id);
    }

    @Override
    public void setup() {
        super.setup();
        this.configuration.snake.setPosition(MotorConstants.SNAKE_V2_DUMPING);
    }

    @Override
    public int run() {
        final int superTransition = super.run();
        if (superTransition != NOT_DONE) return superTransition;

        if (this.stageTimer.seconds() > MotorConstants.SNAKE_V2_TIME_TO_MOVE) return NEXT_STATE;
        else return NOT_DONE;
    }
}
