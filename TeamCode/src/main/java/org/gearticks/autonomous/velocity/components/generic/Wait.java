package org.gearticks.autonomous.velocity.components.generic;

import org.gearticks.autonomous.generic.component.AutonomousComponent.DefaultTransition;
import org.gearticks.autonomous.generic.component.AutonomousComponentTimer;

public class Wait extends AutonomousComponentTimer<DefaultTransition> {
	private final double waitSeconds;

	/**
	 * @param waitSeconds - wait time in seconds
	 * @param id - descriptive name for logging
	 */
	public Wait(double waitSeconds, String id) {
		super(DefaultTransition.class, id);
		this.waitSeconds = waitSeconds;
	}

	@Override
	public DefaultTransition run() {
		final DefaultTransition superTransition = super.run();
		if (superTransition != null) return superTransition;

		if (this.stageTimer.seconds() > this.waitSeconds) return DefaultTransition.DEFAULT;
		else return null;
	}
}