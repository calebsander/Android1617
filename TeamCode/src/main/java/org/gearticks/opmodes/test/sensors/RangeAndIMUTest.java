package org.gearticks.opmodes.test.sensors;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp
@Disabled
public class RangeAndIMUTest extends BaseOpMode {
	private VelocityConfiguration configuration;

	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap);
		this.configuration.imu.eulerRequest.startReading();
		this.configuration.rangeSensor.ultrasonicRequest.startReading();
		this.configuration.rangeSensor.opticalRequest.startReading();
	}
	protected void matchStart() {
		this.configuration.imu.resetHeading();
	}
	protected void loopAfterStart() {
		this.telemetry.addData("Ultrasonic", this.configuration.rangeSensor.cmUltrasonic());
		this.telemetry.addData("Optical", this.configuration.rangeSensor.cmOptical());
		this.telemetry.addData("Yaw", this.configuration.imu.getRelativeYaw());
	}
	protected void matchEnd() {
		this.configuration.teardown();
	}
}