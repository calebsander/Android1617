package org.gearticks.components.hardwareagnostic.component;

import org.gearticks.components.generic.component.OpModeComponent.DefaultTransition;
import org.gearticks.components.generic.component.OpModeComponentTimer;

public class Wait extends OpModeComponentTimer<DefaultTransition> {
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