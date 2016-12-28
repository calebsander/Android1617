package org.gearticks.autonomous.generic.component;

import android.support.annotation.NonNull;

import org.gearticks.autonomous.generic.component.AutonomousComponentBase;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * Base class for Velocity AutonomousComponents.
 * Adds VelocityConfiguration
 */

public class AutonomousComponentVelocityBase extends AutonomousComponentBase {

    @NonNull
    private final VelocityConfiguration configuration;

    public AutonomousComponentVelocityBase(@NonNull VelocityConfiguration configuration) {
        super();
        this.configuration = configuration;
    }

    public AutonomousComponentVelocityBase(@NonNull VelocityConfiguration configuration, String id) {
        super(id);
        this.configuration = configuration;
    }

    public @NonNull VelocityConfiguration getConfiguration() {
        return configuration;
    }
}
