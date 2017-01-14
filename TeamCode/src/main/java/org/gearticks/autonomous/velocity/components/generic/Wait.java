package org.gearticks.autonomous.velocity.components.generic;

import org.gearticks.autonomous.generic.component.AutonomousComponentTimer;

public class Wait extends AutonomousComponentTimer {
	private final double waitSeconds;

	/**
	 *
	 * @param waitSeconds - wait time in seconds
	 * @param id - descriptive name for logging
	 */
	public Wait(double waitSeconds, String id) {
		super(id);
		this.waitSeconds = waitSeconds;
	}

	@Override
	public int run() {
		final int superTransition = super.run();
		if (superTransition != NOT_DONE) return superTransition;

		if (this.stageTimer.seconds() > this.waitSeconds) return NEXT_STATE;
		else return NOT_DONE;
	}
}