package org.gearticks.autonomous.velocity.components.velocity.single;

import android.support.annotation.NonNull;

import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * Created by BenMorris on 2/5/2017.
 */

public class DisengageSideRollers extends AutonomousComponentHardware<VelocityConfiguration> {
	/**
	 * @param configuration - config file
	 * @param id - descriptive name for logging
	 */
	public DisengageSideRollers(@NonNull VelocityConfiguration configuration, String id) {
		super(configuration, id);
	}

	@Override
	public void setup() {
		super.setup();
		configuration.rollersUp();
	}

	@Override
	public int run() {
	final int superTransition = super.run();
		if (superTransition != NOT_DONE) return superTransition;

		if (this.stageTimer.seconds() > 0.5) return NEXT_STATE;
		else return NOT_DONE;
	}
}
