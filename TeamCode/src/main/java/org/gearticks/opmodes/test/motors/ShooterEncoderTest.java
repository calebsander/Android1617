package org.gearticks.opmodes.test.motors;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp
@Disabled
public class ShooterEncoderTest extends BaseOpMode {
	private VelocityConfiguration configuration;

	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap, true);
	}
	protected void loopAfterStart() {
		if (this.gamepads[0].getA()) this.configuration.shooter.setRunMode(RunMode.STOP_AND_RESET_ENCODER);
		else {
			this.configuration.shooter.setRunMode(RunMode.RUN_WITHOUT_ENCODER);
			this.configuration.shooter.setPower(this.gamepads[0].getLeftY() * MotorConstants.SHOOTER_BACK);
			this.telemetry.addData("Encoder", this.configuration.shooter.encoderValue());
		}
	}
}