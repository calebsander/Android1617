package org.gearticks.autonomous.velocity.components.velocity.single;

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
    public Transition run() {
        final Transition superTransition = super.run();
        if (superTransition != null) return superTransition;

        if (this.stageTimer.seconds() > VelocityConfiguration.MotorConstants.PRESSER_V2_TIME_TO_MOVE) return NEXT_STATE;
        else return null;
    }
}
