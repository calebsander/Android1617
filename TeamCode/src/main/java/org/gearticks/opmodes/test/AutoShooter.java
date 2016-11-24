package org.gearticks.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp
public class AutoShooter extends BaseOpMode {
	private VelocityConfiguration configuration;

	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap);
		this.configuration.shooter.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
	}
	protected void loopAfterStart() {
		if (this.gamepads[0].getA()) {
			this.configuration.holdToShooterDown();
		}
		else {
//			this.configuration.shooter.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
//			this.configuration.shooter.setPower(this.gamepads[0].getLeftY() * VelocityConfiguration.MotorConstants.SHOOTER_BACK);
		}
		this.telemetry.addData("Encoder", this.configuration.shooter.encoderValue());
		this.telemetry.addData("Sensor", this.configuration.isShooterDown());
		this.telemetry.addData("Power", this.configuration.shooter.getPower());
	}
}