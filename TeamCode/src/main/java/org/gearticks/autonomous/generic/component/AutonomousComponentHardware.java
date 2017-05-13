package org.gearticks.autonomous.generic.component;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.hardware.configurations.HardwareConfiguration;

/**
 * Stores a hardware configuration object
 * @param <HARDWARE_TYPE> type type of hardware configuration object, e.g. VelocityConfiguration
 */
public class AutonomousComponentHardware<HARDWARE_TYPE extends HardwareConfiguration, TRANSITION_TYPE extends Enum<?>>
extends AutonomousComponentTimer<TRANSITION_TYPE> {
    protected final HARDWARE_TYPE configuration;

    public AutonomousComponentHardware(OpModeContext<? extends HARDWARE_TYPE> opModeContext, Class<TRANSITION_TYPE> transitionClass) {
        super(transitionClass);
        this.configuration = opModeContext.configuration;
    }

    public AutonomousComponentHardware(OpModeContext<? extends HARDWARE_TYPE> opModeContext, Class<TRANSITION_TYPE> transitionClass, String id) {
        super(transitionClass, id);
        this.configuration = opModeContext.configuration;
    }

    @Override
    public void tearDown() {
        super.tearDown();
        this.configuration.stopMotion();
    }
}
