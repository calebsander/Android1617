package org.gearticks.opmodes.utility;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp
public class EncoderMeasurer extends BaseOpMode {
	private VelocityConfiguration configuration;
	private DriveDirection direction;

	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap);
		this.direction = new DriveDirection();
	}
	protected void loopAfterStart() {
		if (this.gamepads[0].getA()) this.configuration.resetEncoder();
		this.direction.drive(0.0, this.gamepads[0].getLeftY());
		this.direction.turn(this.gamepads[0].getRightX());
		this.configuration.move(this.direction, MotorWrapper.NO_ACCEL_LIMIT);
		this.telemetry.addData("Signed encoder", this.configuration.signedEncoder());
		this.telemetry.addData("To reset", "Press A");
	}
}