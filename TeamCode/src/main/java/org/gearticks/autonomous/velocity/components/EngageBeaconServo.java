package org.gearticks.autonomous.velocity.components;

import android.support.annotation.NonNull;
import android.util.Log;

import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.opmodes.utility.Utils;

public class EngageBeaconServo extends AutonomousComponentHardware<VelocityConfiguration> {
    /**
     * @param configuration - config file
     * @param id - descriptive name for logging
     */
    public EngageBeaconServo(@NonNull VelocityConfiguration configuration, String id) {
        super(configuration, id);
    }

    @Override
    public void setup() {
        super.setup();
        configuration.beaconPresserEngage();
    }

    @Override
    public int run() {
        final int superTransition = super.run();
        return NEXT_STATE;
    }
}
