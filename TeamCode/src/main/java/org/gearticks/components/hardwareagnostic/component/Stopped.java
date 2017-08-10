package org.gearticks.components.hardwareagnostic.component;

import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent.NoTransition;
import org.gearticks.components.generic.component.OpModeComponentHardware;
import org.gearticks.hardware.configurations.HardwareConfiguration;

/**
 * To be used as the last block in an autonomous state machine
 * never transitions
 */
public class Stopped extends OpModeComponentHardware<HardwareConfiguration, NoTransition> {
	public Stopped(OpModeContext<? extends HardwareConfiguration> opModeContext) {
		super(opModeContext, NoTransition.class);
	}

	@Override
	public void setup() {
		super.setup();
		this.configuration.stopMotion();
	}
	@Override
	public NoTransition run() {
		final NoTransition superTransition = super.run();
		if (superTransition != null) return superTransition;

		this.configuration.stopMotion();
		return null; //never finishes
	}
}