package org.gearticks.autonomous.velocity.components.velocity.single;

import android.support.annotation.NonNull;

import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class RaiseSideRollers extends AutonomousComponentHardware<VelocityConfiguration> {
    /**
     * @param configuration - config file
     * @param id - descriptive name for logging
     */
    public RaiseSideRollers(@NonNull VelocityConfiguration configuration, String id) {
        super(configuration, id);
    }

    @Override
    public void setup() {
        super.setup();
        configuration.rollersUp();
    }

    @Override
    public Transition run() {
        final Transition superTransition = super.run();
        if (superTransition != null) return superTransition;

        if (this.stageTimer.seconds() > 0.5) return NEXT_STATE;
        else return null;
    }
}
