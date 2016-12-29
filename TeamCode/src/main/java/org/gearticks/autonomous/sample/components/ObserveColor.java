package org.gearticks.autonomous.sample.components;

import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * Sample of an AutonomousComponent that makes a decision and has 2 output ports
 */
public class ObserveColor extends AutonomousComponentHardware<VelocityConfiguration> {
	public static final int RED = newTransition(), BLUE = newTransition();

	public ObserveColor(VelocityConfiguration configuration, String id) {
		super(configuration, id);
	}

	@Override
	public int run() {
		/*
		 * 30 % probability blue = transition 2
		 * 30 % probability red = transition 1
		 * 40 % Probability that no color is detected
		 */
		final double random = Math.random();
		if (random < 0.3) return BLUE;
		if (random < 0.6) return RED;
		return NOT_DONE;
	}
}
