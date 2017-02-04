package org.gearticks.autonomous.velocity.components.velocity.single;

import android.support.annotation.NonNull;

import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class LeftPressBeaconServo extends AutonomousComponentHardware<VelocityConfiguration> {
    /**
     * @param configuration - config file
     * @param id - descriptive name for logging
     */
    public LeftPressBeaconServo(@NonNull VelocityConfiguration configuration, String id) {
        super(configuration, id);
    }

    @Override
    public void setup() {
        super.setup();
        configuration.beaconPresserEngageLeft();
    }

    @Override
    public int run() {
        final int superTransition = super.run();
        if (superTransition != NOT_DONE) return superTransition;

        if (this.stageTimer.seconds() > 1) return NEXT_STATE;
        else return NOT_DONE;
    }
}
