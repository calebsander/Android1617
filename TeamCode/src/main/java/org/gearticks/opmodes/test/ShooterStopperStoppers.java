package org.gearticks.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp
@Disabled
public class ShooterStopperStoppers extends BaseOpMode {
	private VelocityConfiguration configuration;

	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap);
	}
	protected void loopAfterStart() {
		this.configuration.safeShooterStopper(this.gamepads[0].getLeftY() * MotorConstants.SHOOTER_STOPPER_UP);
	}
}