package org.gearticks.components.velocity.opmode.generic;

import org.gearticks.components.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;

public abstract class InitializedAutonomous extends VelocityBaseOpMode {
	@Override
	protected void loopBeforeStart() {
		super.loopBeforeStart();
		this.configuration.safeShooterStopper(MotorConstants.SHOOTER_STOPPER_UP);
		this.configuration.advanceShooterToDownSlowly();
		this.configuration.beaconPressersIn();
	}
	@Override
	protected void matchStart() {
		super.matchStart();
		this.configuration.disengageTopLatch();
	}
	protected boolean isV2() {
		return true;
	}
}