package org.gearticks.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp
public class VelocityDrive extends BaseOpMode {
	private static final int CALVIN = 0, JACK = 1;
	private VelocityConfiguration configuration;
	private DriveDirection direction;

	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap);
		this.direction = new DriveDirection();
	}
	protected void loopAfterStart() {
		final int gamepad;
		final double yScaleFactor, sScaleFactor;
		if (this.gamepads[CALVIN].leftStickAtRest() && this.gamepads[CALVIN].rightStickAtRest()) { //if Calvin not driving, Jack can drive
			gamepad = JACK;
			yScaleFactor = 0.4;
			sScaleFactor = 0.3;
		}
		else { //Calvin driving
			gamepad = CALVIN;
			yScaleFactor = 0.8;
			sScaleFactor = 0.5;
		}
		this.direction.drive(0.0, this.gamepads[gamepad].getLeftY() * yScaleFactor);
		this.direction.turn(scaleStick(this.gamepads[gamepad].getRightX()) * sScaleFactor);
		this.configuration.move(this.direction);

		final double intakePower;
		if (this.gamepads[CALVIN].getRightBumper()) intakePower = VelocityConfiguration.MotorConstants.INTAKE_IN;
		else if (this.gamepads[CALVIN].getRightTrigger()) intakePower = VelocityConfiguration.MotorConstants.INTAKE_OUT;
		else intakePower = MotorWrapper.STOPPED;
		this.configuration.intake.setPower(intakePower);

		final double shooterPower;
		if (this.gamepads[JACK].getRightBumper()) shooterPower = VelocityConfiguration.MotorConstants.SHOOTER_BACK;
		else if (this.gamepads[JACK].getRightTrigger()) shooterPower = VelocityConfiguration.MotorConstants.SHOOTER_FORWARD;
		else shooterPower = MotorWrapper.STOPPED;
		this.configuration.shooter.setPower(shooterPower);
	}

	private static float scaleStick(float stick) {
		return stick * stick * stick;
	}
}
