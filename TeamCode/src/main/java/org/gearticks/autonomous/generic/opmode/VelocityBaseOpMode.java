package org.gearticks.autonomous.generic.opmode;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.opmodes.BaseOpMode;

public abstract class VelocityBaseOpMode extends BaseOpMode {
	protected VelocityConfiguration configuration;
	private AutonomousComponent component;

	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap);
		this.configuration.imu.eulerRequest.startReading();
		this.component = this.getComponent();
		this.component.initialize();
	}
	protected void loopBeforeStart() {
		this.telemetry.addData("Heading", this.configuration.imu.getHeading());
	}
	protected void matchStart() {
		this.telemetry.clear();
		this.configuration.imu.resetHeading();
		this.component.setup();
	}
	protected void loopAfterStart() {
		this.component.run();
	}
	protected void matchEnd() {
		this.component.tearDown();
		this.configuration.teardown();
	}

	protected abstract AutonomousComponent getComponent();
}