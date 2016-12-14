package org.gearticks.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp
public class VelocityDrive extends BaseOpMode {
	private static final int CALVIN = 0, JACK = 1;
	private VelocityConfiguration configuration;
	private DriveDirection direction;
	private boolean clutchClutched;

	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap);
		this.direction = new DriveDirection();
		this.clutchClutched = false;
	}
	protected void loopAfterStart() {
		final int driveGamepad = CALVIN;
		final double yScaleFactor = 1.0,
			sScaleAtRest = 0.4,
			sScaleWhenMoving = 1.0;
		if (this.gamepads[driveGamepad].leftStickAtRest()) { //if just turning, turn slower for greater accuracy
			this.direction.drive(0.0, 0.0);
			this.direction.turn(scaleStick(this.gamepads[driveGamepad].getRightX()) * sScaleAtRest);
		}
		else { //if banana-turning, turn faster
			this.direction.drive(0.0, scaleStick(this.gamepads[driveGamepad].getLeftY()) * yScaleFactor);
			this.direction.turn(scaleStick(this.gamepads[driveGamepad].getRightX()) * sScaleWhenMoving);
		}
		this.configuration.move(this.direction, MotorWrapper.NO_ACCEL_LIMIT);

		final double intakePower;
		if (this.gamepads[CALVIN].getRightBumper() || this.gamepads[JACK].getRightBumper()) {
			intakePower = MotorConstants.INTAKE_IN;
		}
		else if (this.gamepads[CALVIN].getRightTrigger() || this.gamepads[JACK].getRightTrigger()) {
			intakePower = MotorConstants.INTAKE_OUT;
		}
		else {
			intakePower = MotorWrapper.STOPPED;
		}
		this.configuration.intake.setPower(intakePower);

		if (this.gamepads[JACK].getLeftBumper()) {
			this.configuration.resetAutoShooter();
			this.configuration.shootFast();
		}
		else {
			this.configuration.teleopAdvanceShooterToDown();
		}

		final double shooterStopperPower;
		if (this.gamepads[JACK].dpadUp()) {
			shooterStopperPower = MotorConstants.SHOOTER_STOPPER_UP;
		}
		else if (this.gamepads[JACK].dpadDown()) {
			shooterStopperPower = MotorConstants.SHOOTER_STOPPER_DOWN;
		}
		else {
			shooterStopperPower = MotorWrapper.STOPPED;
		}
		this.configuration.safeShooterStopper(shooterStopperPower);

		if (this.gamepads[CALVIN].getA() && !this.gamepads[CALVIN].getLast().getA()) {
			this.clutchClutched = !this.clutchClutched;
			final double clutchPosition;
			if (this.clutchClutched) {
				clutchPosition = MotorConstants.CLUTCH_CLUTCHED;
			}
			else {
				clutchPosition = MotorConstants.CLUTCH_ENGAGED;
			}
			this.configuration.clutch.setPosition(clutchPosition);
		}

		final double snakePosition;
		if (this.gamepads[JACK].getA()) {
			snakePosition = MotorConstants.SNAKE_DUMPING;
		}
		else {
			snakePosition = MotorConstants.SNAKE_HOLDING;
		}
		this.configuration.snake.setPosition(snakePosition);
	}

	private static double scaleStick(double stick) {
		return stick * stick * stick;
	}
}