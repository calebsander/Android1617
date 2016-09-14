package org.gearticks.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cDevice;
import org.gearticks.dimsensors.i2c.I2CSwitcher;
import org.gearticks.dimsensors.i2c.TCS34725;

@Autonomous(name = "Switcher Test", group = "test")
public class I2CSwitcherTest extends OpMode {
	private static final int COLOR_SENSOR_COUNT = 2;

	private I2CSwitcher switcher;
	private TCS34725[] colorSensors;

	public void init() {
		final I2cDevice device = (I2cDevice)this.hardwareMap.get("switcher");
		this.switcher = new I2CSwitcher(device);
		this.colorSensors = new TCS34725[COLOR_SENSOR_COUNT];
		for (int i = 0; i < COLOR_SENSOR_COUNT; i++) {
			final TCS34725 colorSensor;
			colorSensor = new TCS34725(device, this.switcher, i);
			colorSensor.startReadingColor();
		}
	}
	public void loop() {
		for (int i = 0; i < COLOR_SENSOR_COUNT; i++) {
			final TCS34725 colorSensor = this.colorSensors[i];
			this.telemetry.addData("Clear", colorSensor.getClear());
			this.telemetry.addData("Red", colorSensor.getRed());
			this.telemetry.addData("Green", colorSensor.getGreen());
			this.telemetry.addData("Blue", colorSensor.getBlue());
		}
	}
	public void stop() {
		for (final TCS34725 colorSensor : this.colorSensors) colorSensor.stopReading();
		this.switcher.terminate();
	}
}