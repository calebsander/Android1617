package org.gearticks.autonomous.velocity.components.velocity.single;

import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
public class ShooterStopperToNear extends AutonomousComponentHardware<VelocityConfiguration> {

	public ShooterStopperToNear(VelocityConfiguration configuration, String id) {
		super(configuration, id);
	}

	@Override
	public Transition run() {
		final Transition superTransition = super.run();
		if (superTransition != null) return superTransition;

		this.configuration.safeShooterStopper(VelocityConfiguration.MotorConstants.SHOOTER_STOPPER_DOWN);
		if (this.configuration.shooterNearTriggered()) return NEXT_STATE;
		else return null;
	}
}
