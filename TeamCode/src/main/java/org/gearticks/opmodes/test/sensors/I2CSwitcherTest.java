package org.gearticks.opmodes.test.sensors;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.LED;
import org.gearticks.dimsensors.i2c.I2CSwitcher;
import org.gearticks.dimsensors.i2c.TCS34725;

@Autonomous(name = "Switcher Test", group = "test")
@Disabled
public class I2CSwitcherTest extends OpMode {
	private static final int COLOR_SENSOR_COUNT = 2;

	private I2CSwitcher switcher;
	private TCS34725[] colorSensors;
	private LED[] leds;

	public void init() {
		final I2cDevice device = (I2cDevice)this.hardwareMap.get("switcher");
		this.switcher = new I2CSwitcher(device);
		this.colorSensors = new TCS34725[COLOR_SENSOR_COUNT];
		this.leds = new LED[COLOR_SENSOR_COUNT];
		for (int i = 0; i < COLOR_SENSOR_COUNT; i++) {
			this.colorSensors[i] = new TCS34725(device, this.switcher, i);
			this.leds[i] = (LED)this.hardwareMap.get("led" + Integer.toString(i));
		}
	}
	public void start() {
		for (final TCS34725 colorSensor : this.colorSensors) colorSensor.startReadingColor();
	}
	public void loop() {
		for (int i = 0; i < COLOR_SENSOR_COUNT; i++) {
			final TCS34725 colorSensor = this.colorSensors[i];
			this.telemetry.addData(Integer.toString(i) + " Clear", colorSensor.getClear());
			this.telemetry.addData(Integer.toString(i) + " Red", colorSensor.getRed());
			this.telemetry.addData(Integer.toString(i) + " Green", colorSensor.getGreen());
			this.telemetry.addData(Integer.toString(i) + " Blue", colorSensor.getBlue());
		}
		this.leds[0].enable(this.gamepad1.a);
		this.leds[1].enable(this.gamepad1.b);
	}
	public void stop() {
		for (final TCS34725 colorSensor : this.colorSensors) colorSensor.stopReading();
		this.switcher.terminate();
	}
}