package org.gearticks.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.LED;
import org.gearticks.dimsensors.i2c.TCS34725;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp
public class TCS34725Test extends BaseOpMode {
	private TCS34725 colorSensor;
	private LED led;

	public void initialize() {
		this.colorSensor = new TCS34725((I2cDevice)this.hardwareMap.get("whiteLineColor"));
		this.colorSensor.startReadingColor();
		this.led = (LED)this.hardwareMap.get("whiteLineColorLed");
	}
	public void loopAfterStart() {
		this.led.enable(this.gamepad1.a);
		this.telemetry.addData("Clear", this.colorSensor.getClear());
		this.telemetry.addData("Red", this.colorSensor.getRed());
		this.telemetry.addData("Green", this.colorSensor.getGreen());
		this.telemetry.addData("Blue", this.colorSensor.getBlue());
	}
	public void matchEnd() {
		this.colorSensor.stopReading();
	}
}