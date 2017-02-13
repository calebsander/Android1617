package org.gearticks.autonomous.generic.component;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.hardware.configurations.HardwareConfiguration;

/**
 * Stores a hardware configuration object
 * @param <HARDWARE_TYPE> type type of hardware configuration object, e.g. VelocityConfiguration
 */
public class AutonomousComponentHardware<HARDWARE_TYPE extends HardwareConfiguration> extends AutonomousComponentTimer {
    protected final HARDWARE_TYPE configuration;

    public AutonomousComponentHardware(OpModeContext<HARDWARE_TYPE> opModeContext) {
        super();
        this.configuration = opModeContext.configuration;
    }

    public AutonomousComponentHardware(OpModeContext<HARDWARE_TYPE> opModeContext, String id) {
        super(id);
        this.configuration = opModeContext.configuration;
    }

    @Override
    public void tearDown() {
        super.tearDown();
        this.configuration.stopMotion();
    }
}
