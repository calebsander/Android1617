package org.gearticks.autonomous.velocity.components.velocity.single;

import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * Created by BenMorris on 2/7/2017.
 */

public class ShooterStopperToNear extends AutonomousComponentHardware<VelocityConfiguration> {

	public ShooterStopperToNear(VelocityConfiguration configuration, String id) {
		super(configuration, id);
	}

	@Override
	public int run() {
		final int superTransition = super.run();
		if (superTransition != NOT_DONE) return superTransition;

		this.configuration.safeShooterStopper(VelocityConfiguration.MotorConstants.SHOOTER_STOPPER_DOWN);
		if (this.configuration.shooterNearTriggered()) return NEXT_STATE;
		else return NOT_DONE;
	}
}
