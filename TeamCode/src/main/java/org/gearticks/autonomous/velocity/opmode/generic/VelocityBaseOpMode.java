package org.gearticks.autonomous.velocity.opmode.generic;

import org.gearticks.autonomous.generic.opmode.HardwareComponentAutonomous;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * An OpMode that instantiates a VelocityConfiguration
 * and executes a single component (possibly a state machine)
 */
public abstract class VelocityBaseOpMode extends HardwareComponentAutonomous<VelocityConfiguration> {
	protected void initialize() {
		super.initialize();
		this.configuration.imu.eulerRequest.startReading();
	}
	protected void loopBeforeStart() {
		super.loopBeforeStart();
		this.telemetry.addData("Heading", this.configuration.imu.getHeading());
	}
	protected void matchStart() {
		this.configuration.imu.resetHeading();
		super.matchStart();
	}

	protected VelocityConfiguration newConfiguration() {
		return new VelocityConfiguration(this.hardwareMap);
	}
}