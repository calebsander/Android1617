package org.gearticks.autonomous.velocity.opmode.generic;

import org.gearticks.autonomous.generic.opmode.HardwareComponentAutonomous;
import org.gearticks.dimsensors.i2c.GearticksBNO055.EulerAngle;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * An OpMode that instantiates a VelocityConfiguration
 * and executes a single component (possibly a state machine)
 */
public abstract class VelocityBaseOpMode extends HardwareComponentAutonomous<VelocityConfiguration> {
	private boolean hasResetHeading;

	protected void initialize() {
		super.initialize();
		this.configuration.imu.eulerRequest.startReading();
		this.hasResetHeading = false;
	}
	protected void loopBeforeStart() {
		super.loopBeforeStart();
		final EulerAngle heading = this.configuration.imu.getHeading();
		this.telemetry.addData("Heading", heading);
		if (heading != null) {
			if (this.gamepads[0].getA()) {
				this.hasResetHeading = true;
				this.configuration.imu.resetHeading();
			}
			if (this.hasResetHeading) {
				this.telemetry.addData("Relative to wall", this.configuration.imu.getRelativeYaw());
			}
			else this.telemetry.addData("To reset heading", "Press A");
		}
	}

	protected VelocityConfiguration newConfiguration() {
		return new VelocityConfiguration(this.hardwareMap, this.isV2());
	}
	protected abstract boolean isV2();
}