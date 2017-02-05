package org.gearticks.autonomous.velocity.components.generic;

import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.HardwareConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * To be used as the last block in an autonomous state machine
 * never transitions
 */
public class Stopped extends AutonomousComponentHardware<HardwareConfiguration> {
	public Stopped(HardwareConfiguration configuration) {
		super(configuration);
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