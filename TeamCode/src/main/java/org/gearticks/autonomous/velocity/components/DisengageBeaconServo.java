package org.gearticks.autonomous.velocity.components;

import android.support.annotation.NonNull;

import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class DisengageBeaconServo extends AutonomousComponentHardware<VelocityConfiguration> {
    /**
     * @param configuration - config file
     * @param id - descriptive name for logging
     */
    public DisengageBeaconServo(@NonNull VelocityConfiguration configuration, String id) {
        super(configuration, id);
    }

    @Override
    public void setup() {
        super.setup();
        configuration.beaconPresserDisengage();
    }

    @Override
    public int run() {
        final int superTransition = super.run();
        return NEXT_STATE;
    }
}
