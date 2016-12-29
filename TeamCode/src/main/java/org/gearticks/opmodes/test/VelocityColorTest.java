package org.gearticks.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp
@Disabled
public class VelocityColorTest extends BaseOpMode {
	/*private VelocityConfiguration configuration;

	protected void initializeAtMatchStart() {
		this.configuration = new VelocityConfiguration(this.hardwareMap);
	}
	protected void matchStart() {
		this.configuration.startReadingColor();
	}
	protected void loopAfterStart() {
		this.telemetry.addData("Left R", this.configuration.colorLeft.getRed());
		this.telemetry.addData("Left G", this.configuration.colorLeft.getGreen());
		this.telemetry.addData("Left B", this.configuration.colorLeft.getBlue());
		this.telemetry.addData("Right R", this.configuration.colorRight.getRed());
		this.telemetry.addData("Right G", this.configuration.colorRight.getGreen());
		this.telemetry.addData("Right B", this.configuration.colorRight.getBlue());
		this.configuration.setLEDs(this.gamepads[0].getA());
	}
	protected void matchEnd() {
		this.configuration.teardown();
	}*/
}