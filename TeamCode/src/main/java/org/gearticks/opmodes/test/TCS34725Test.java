package org.gearticks.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp
@Disabled
public class TCS34725Test extends BaseOpMode {
	private VelocityConfiguration configuration;

	public void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap);
		this.configuration.activateWhiteLineColor();
		//Switch to reading color rather than clear
		this.configuration.whiteLineColorSensor.stopReading();
		this.configuration.whiteLineColorSensor.startReadingColor();
	}
	public void loopAfterStart() {
		this.configuration.whiteLineColorLed.enable(this.gamepads[0].getA());
		if (this.gamepads[0].getB()) this.configuration.whiteLineColorSensor.calibrate();
		this.telemetry.addData("Clear", this.configuration.whiteLineColorSensor.getClear());
		this.telemetry.addData("Red", this.configuration.whiteLineColorSensor.getRed());
		this.telemetry.addData("Green", this.configuration.whiteLineColorSensor.getGreen());
		this.telemetry.addData("Blue", this.configuration.whiteLineColorSensor.getBlue());
	}
	public void matchEnd() {
		this.configuration.deactivateWhiteLineColor();
	}
}