package org.gearticks.autonomous.velocity.components.velocity.single;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class ShootBall extends AutonomousComponentHardware<VelocityConfiguration> {
    /**
     * @param configuration - config file
     * @param id - descriptive name for logging
     */
    public ShootBall(@NonNull VelocityConfiguration configuration, String id) {
        super(configuration, id);
    }

    @Override
    public void setup() {
        super.setup();
        this.configuration.shootSlow();
    }

    @Override
    public Transition run() {
        final Transition superTransition = super.run();
        if (superTransition != null) return superTransition;

        if (this.configuration.hasShot()) return NEXT_STATE;
        else return null;
    }
}
