package org.gearticks.autonomous.velocity.components.generic;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.HardwareConfiguration;

/**
 * To be used as the last block in an autonomous state machine
 * never transitions
 */
public class Stopped extends AutonomousComponentHardware<HardwareConfiguration> {
	@SuppressWarnings("unchecked")
	public Stopped(OpModeContext opModeContext) {
		super(opModeContext);
	}

	@Override
	public void setup() {
		super.setup();
		this.configuration.stopMotion();
	}
	@Override
	public Transition run() {
		final Transition superTransition = super.run();
		if (superTransition != null) return superTransition;

		this.configuration.stopMotion();
		return null; //never finishes
	}
}