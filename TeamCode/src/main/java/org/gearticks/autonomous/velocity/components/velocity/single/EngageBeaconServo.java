package org.gearticks.autonomous.velocity.components.velocity.single;

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
        if (this.stageTimer.seconds() > 0.5) return NEXT_STATE;
        else return NOT_DONE;
    }
}
