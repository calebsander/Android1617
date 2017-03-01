package org.gearticks.autonomous.velocity.opmode.generic;

import org.gearticks.autonomous.generic.opmode.HardwareComponentAutonomous;
import org.gearticks.dimsensors.DimLed;
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
		final boolean redOn, blueOn;
		if (heading == null) redOn = blueOn = true;
		else {
			if (!this.hasResetHeading) {
				this.configuration.imu.resetHeading();
				this.hasResetHeading = true;
			}
			final double yaw = this.configuration.imu.getRelativeYaw();
			this.telemetry.addData("Relative to wall", yaw);
			final double headingDiff = ((yaw - this.targetHeading() + 540.0) % 360.0) - 180.0; //get difference in range of -180 to 180
			if (Math.abs(headingDiff) < 0.5) {
				redOn = false;
				blueOn = false;
			}
			else if (headingDiff > 0.0) {
				redOn = false;
				blueOn = true;
			}
			else { //headingDiff < 0.0
				redOn = true;
				blueOn = false;
			}
		}
		this.configuration.dim.setLED(DimLed.RED.id, redOn);
		this.configuration.dim.setLED(DimLed.BLUE.id, blueOn);
	}

	protected VelocityConfiguration newConfiguration() {
		return new VelocityConfiguration(this.hardwareMap, this.isV2());
	}
	protected abstract boolean isV2();
	protected double targetHeading() {
		return 0.0;
	}
}