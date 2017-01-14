package org.gearticks.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp
@Disabled
public class TCS34725Test extends BaseOpMode {
	private VelocityConfiguration configuration;
	private DriveDirection direction;

	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap);
		this.direction = new DriveDirection();
	}
	protected void matchStart() {
		this.configuration.activateWhiteLineColor();
		//Switch to reading color rather than clear
		this.configuration.whiteLineColorSensor.stopReading();
		this.configuration.whiteLineColorSensor.startReadingColor();
	}
	protected void loopAfterStart() {
		this.telemetry.addData("Enable LED", "Press A");
		this.telemetry.addData("Calibrate", "Pres B");
		this.configuration.whiteLineColorLed.enable(this.gamepads[0].getA());
		if (this.gamepads[0].getB()) this.configuration.whiteLineColorSensor.calibrate();
		this.telemetry.addData("Clear", this.configuration.whiteLineColorSensor.getRelativeClear());
		this.telemetry.addData("Red", this.configuration.whiteLineColorSensor.getRelativeRed());
		this.telemetry.addData("Green", this.configuration.whiteLineColorSensor.getRelativeGreen());
		this.telemetry.addData("Blue", this.configuration.whiteLineColorSensor.getRelativeBlue());
		this.direction.drive(0.0, this.gamepads[0].getLeftY());
		this.direction.turn(this.gamepads[0].getRightX());
		this.configuration.move(this.direction, MotorWrapper.NO_ACCEL_LIMIT);
	}
	protected void matchEnd() {
		this.configuration.deactivateWhiteLineColor();
	}
}