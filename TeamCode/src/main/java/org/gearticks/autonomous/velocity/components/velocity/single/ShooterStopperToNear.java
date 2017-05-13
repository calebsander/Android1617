package org.gearticks.autonomous.velocity.components.velocity.single;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent.DefaultTransition;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class ShooterStopperToNear extends AutonomousComponentHardware<VelocityConfiguration, DefaultTransition> {
	public ShooterStopperToNear(OpModeContext<VelocityConfiguration> opModeContext, String id) {
		super(opModeContext, DefaultTransition.class, id);
	}

	@Override
	public DefaultTransition run() {
		final DefaultTransition superTransition = super.run();
		if (superTransition != null) return superTransition;

		this.configuration.safeShooterStopper(VelocityConfiguration.MotorConstants.SHOOTER_STOPPER_DOWN);
		if (this.configuration.shooterNearTriggered()) return DefaultTransition.DEFAULT;
		else return null;
	}
}
