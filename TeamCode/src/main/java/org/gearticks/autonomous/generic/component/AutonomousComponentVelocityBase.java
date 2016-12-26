package org.gearticks.autonomous.generic.component;

import org.gearticks.autonomous.generic.component.AutonomousComponentBase;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * Created by vterpstra on 11/23/2016.
 */

public class AutonomousComponentVelocityBase extends AutonomousComponentBase {

    private final VelocityConfiguration configuration;

    public AutonomousComponentVelocityBase(VelocityConfiguration configuration) {
        super();
        this.configuration = configuration;
    }

    public AutonomousComponentVelocityBase(VelocityConfiguration configuration, String id) {
        super(id);
        this.configuration = configuration;
    }

    public VelocityConfiguration getConfiguration() {
        return configuration;
    }
}
