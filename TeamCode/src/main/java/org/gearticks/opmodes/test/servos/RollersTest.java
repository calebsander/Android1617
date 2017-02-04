package org.gearticks.opmodes.test.servos;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp
@Disabled
public class RollersTest extends BaseOpMode {
	private VelocityConfiguration configuration;

	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap, true);
	}
	protected void loopAfterStart() {
		if (this.gamepads[0].getA()) this.configuration.rollersDown();
		else this.configuration.rollersUp();
	}
}
