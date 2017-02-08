package org.gearticks.autonomous.velocity.components.generic;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.HardwareConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * To be used as the last block in an autonomous state machine
 * never transitions
 */
public class Stopped extends AutonomousComponentHardware<HardwareConfiguration> {
	public Stopped(OpModeContext opModeContext) {
		super(opModeContext.configuration);
	}

	@Override
	public void setup() {
		super.setup();
		this.configuration.stopMotion();
	}
	@Override
	public int run() {
		final int superTransition = super.run();
		if (superTransition != NOT_DONE) return superTransition;

		this.configuration.stopMotion();
		return NOT_DONE; //never finishes
	}
}