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
	private boolean clutchToggle = false;

	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap);
		this.direction = new DriveDirection();
	}
	protected void loopAfterStart() {
		final int gamepad;
		final double yScaleFactor, sScaleAtRest, sScaleWhenMoving;
		if (this.gamepads[CALVIN].leftStickAtRest() && this.gamepads[CALVIN].rightStickAtRest()) { //if Calvin not driving, Jack can drive
			gamepad = JACK;
			yScaleFactor = 0.3;
			sScaleAtRest = 0.2;
			sScaleWhenMoving = 0.3;
		}
		else { //Calvin driving
			gamepad = CALVIN;
			yScaleFactor = 1.0;
			sScaleAtRest = 0.4;
			sScaleWhenMoving = 1.0;
		}
		if (this.gamepads[gamepad].getLeftY() == 0.0) { //if just turning, turn slower for greater accuracy
			this.direction.drive(0.0, 0.0);
			this.direction.turn(scaleStick(this.gamepads[gamepad].getRightX()) * sScaleAtRest);
		}
		else { //if banana-turning, turn faster
			this.direction.drive(0.0, scaleStick(this.gamepads[gamepad].getLeftY()) * yScaleFactor);
			this.direction.turn(scaleStick(this.gamepads[gamepad].getRightX()) * sScaleWhenMoving);
		}
		this.configuration.move(this.direction, 0.15);

		final double intakePower;
		if (this.gamepads[CALVIN].getRightBumper() || this.gamepads[CALVIN].getRightTrigger()) { //Calvin picking up
			if (this.gamepads[CALVIN].getRightBumper()) {
				intakePower = MotorConstants.INTAKE_IN;
			}
			else if (this.gamepads[CALVIN].getRightTrigger()) {
				intakePower = MotorConstants.INTAKE_OUT;
			}
			else {
				intakePower = MotorWrapper.STOPPED;
			}
		}
		else { //if Calvin not picking up, Jack can elevate
			intakePower = scaleStick(this.gamepads[JACK].getRightY()) * MotorConstants.INTAKE_IN;
		}
		this.configuration.intake.setPower(intakePower);

		if (this.gamepads[JACK].getRightBumper()) {
			this.configuration.resetAutoShooter();
			this.configuration.shootFast();
		}
		else {
			this.configuration.teleopAdvanceShooterToDown();
		}

		if (this.gamepads[JACK].getLeftBumper()) {
			this.configuration.safeShooterStopper(MotorConstants.SHOOTER_STOPPER_UP);
		}
		else if (this.gamepads[JACK].getLeftTrigger()) {
			this.configuration.safeShooterStopper(MotorConstants.SHOOTER_STOPPER_DOWN);
		}
		else this.configuration.shooterStopper.setPower(MotorWrapper.STOPPED);

		if (this.gamepads[CALVIN].getA() && !this.gamepads[CALVIN].getLast().getA()) {
			this.clutchToggle = !this.clutchToggle;
			if (this.clutchToggle) {
				this.configuration.clutch.setPosition(MotorConstants.CLUTCH_CLUTCHED);
			}
			else {
				this.configuration.clutch.setPosition(MotorConstants.CLUTCH_ENGAGED);
			}
		}

		if (this.gamepads[JACK].getA()) {
			this.configuration.particleBlocker.setPosition(MotorConstants.SNAKE_DUMPING);
		}
		else {
			this.configuration.particleBlocker.setPosition(MotorConstants.SNAKE_HOLDING);
		}
	}

	private static double scaleStick(double stick) {
		return stick * stick * stick;
	}
}
