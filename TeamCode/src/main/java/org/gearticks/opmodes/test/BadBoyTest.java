package org.gearticks.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp
@Disabled
public class BadBoyTest extends BaseOpMode {
	private VelocityConfiguration configuration;

	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap);
	}
	protected void loopAfterStart() {
		this.telemetry.addData("Ball", this.configuration.ballInSnake());
	}
}