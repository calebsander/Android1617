package org.gearticks.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.LED;
import org.gearticks.dimsensors.i2c.TCS34725;

@Autonomous
public class TCS34725Test extends OpMode {
	private TCS34725 colorSensor;
	private LED led;

	public void init() {
		this.colorSensor = new TCS34725((I2cDevice)this.hardwareMap.get("whiteLineColor"));
		this.led = (LED)this.hardwareMap.get("whiteLineColorLed");
	}
	public void start() {
		this.colorSensor.startReadingColor();
		this.led.enable(true);
	}
	public void loop() {
		this.led.enable(this.gamepad1.a);
		this.telemetry.addData("Clear", this.colorSensor.getClear());
		this.telemetry.addData("Red", this.colorSensor.getRed());
		this.telemetry.addData("Green", this.colorSensor.getGreen());
		this.telemetry.addData("Blue", this.colorSensor.getBlue());
	}
	public void stop() {
		this.colorSensor.stopReading();
	}
}