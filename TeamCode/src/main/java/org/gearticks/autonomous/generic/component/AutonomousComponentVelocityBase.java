package org.gearticks.autonomous.generic.component;

import org.gearticks.hardware.configurations.VelocityConfiguration;

public class AutonomousComponentVelocityBase extends AutonomousComponentBase {
    protected final VelocityConfiguration configuration;

    public AutonomousComponentVelocityBase(VelocityConfiguration configuration) {
        super();
        this.configuration = configuration;
    }

    public AutonomousComponentVelocityBase(VelocityConfiguration configuration, String id) {
        super(id);
        this.configuration = configuration;
    }

    @Override
    public void tearDown() {
        super.tearDown();
        this.configuration.stopMotion();
    }
}
