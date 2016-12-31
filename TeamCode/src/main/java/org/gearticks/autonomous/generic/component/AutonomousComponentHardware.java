package org.gearticks.autonomous.generic.component;

import org.gearticks.hardware.configurations.HardwareConfiguration;

/**
 * Stores a hardware configuration object
 * @param <HARDWARE_TYPE> type type of hardware configuration object, e.g. VelocityConfiguration
 */
public class AutonomousComponentHardware<HARDWARE_TYPE extends HardwareConfiguration> extends AutonomousComponentBase {
    protected final HARDWARE_TYPE configuration;

    public AutonomousComponentHardware(HARDWARE_TYPE configuration) {
        super();
        this.configuration = configuration;
    }

    public AutonomousComponentHardware(HARDWARE_TYPE configuration, String id) {
        super(id);
        this.configuration = configuration;
    }

    @Override
    public void tearDown() {
        super.tearDown();
        this.configuration.stopMotion();
    }
}
