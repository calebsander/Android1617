package org.gearticks.autonomous.velocity.components;

import android.support.annotation.NonNull;
import android.util.Log;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.opmodes.utility.Utils;

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
        this.configuration.snake.setPosition(VelocityConfiguration.MotorConstants.SNAKE_DUMPING);
    }

    @Override
    public int run() {
        final int superTransition = super.run();
        if (superTransition != NOT_DONE) return superTransition;

        Log.i(Utils.TAG, "LoadBall - timer = " + this.stageTimer.seconds());
        if (this.stageTimer.seconds() > 0.7) return NEXT_STATE;
        else return NOT_DONE;
    }
}
