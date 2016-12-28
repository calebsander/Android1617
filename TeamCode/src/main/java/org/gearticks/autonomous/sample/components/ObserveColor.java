package org.gearticks.autonomous.sample.components;

import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * Sample of an AutonomousComponent that makes a decision and has 2 output ports
 * Transition 1 for red, transition 2 for blue
 *
 */
public class ObserveColor extends AutonomousComponentVelocityBase {
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
