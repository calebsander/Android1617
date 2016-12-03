package org.gearticks.hardware.configurations;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController.Mode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.Servo;
import org.gearticks.dimsensors.i2c.BNO055;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.hardware.drive.TankDrive;

public class VelocityConfiguration implements HardwareConfiguration {
	public final MotorWrapper intake, shooter;
	private boolean shooterWasDown;
	public final MotorWrapper driveLeft, driveRight;
	public final TankDrive drive;
	public final Servo clutch, snake;
	public final CRServo shooterStopper;
	public final BNO055 imu;
	public final DigitalChannel shooterDown;
	public final DigitalChannel shooterNear, shooterFar;

	public VelocityConfiguration(HardwareMap hardwareMap) {
		this.intake = new MotorWrapper((DcMotor)hardwareMap.get("intake"), MotorWrapper.MotorType.NEVEREST_40);
		this.shooter = new MotorWrapper((DcMotor)hardwareMap.get("shooter"), MotorWrapper.MotorType.NEVEREST_40);
		this.shooterWasDown = false;
		this.driveLeft = new MotorWrapper((DcMotor)hardwareMap.get("left"), MotorWrapper.MotorType.NEVEREST_20, true);
		this.driveRight = new MotorWrapper((DcMotor)hardwareMap.get("right"), MotorWrapper.MotorType.NEVEREST_20, true);
		this.drive = new TankDrive();
		this.drive.addLeftMotor(this.driveLeft);
		this.drive.addRightMotor(this.driveRight);
		this.driveLeft.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
		this.driveRight.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
		this.driveLeft.setStopMode(ZeroPowerBehavior.BRAKE);
		this.driveRight.setStopMode(ZeroPowerBehavior.BRAKE);

		this.clutch = (Servo)hardwareMap.get("clutch");
		this.clutch.setPosition(MotorConstants.CLUTCH_ENGAGED);
		this.snake = (Servo)hardwareMap.get("snake");
		this.snake.setPosition(MotorConstants.SNAKE_HOLDING);
		this.shooterStopper = (CRServo)hardwareMap.get("shooterStopper");
		this.shooterStopper.setPower(0.0);

		this.imu = new BNO055((I2cDevice)hardwareMap.get("bno"));
		this.shooterDown = (DigitalChannel)hardwareMap.get("shooterDown");
		this.shooterDown.setMode(Mode.INPUT);
		this.shooterNear = (DigitalChannel)hardwareMap.get("shooterNear");
		this.shooterNear.setMode(Mode.INPUT);
		this.shooterFar = (DigitalChannel)hardwareMap.get("shooterFar");
		this.shooterFar.setMode(Mode.INPUT);
	}
	public void teardown() {
		this.imu.eulerRequest.stopReading();
		this.imu.terminate();
	}
	public void stopMotion() {
		this.intake.stop();
		this.driveLeft.stop();
		this.driveRight.stop();
	}

	public void move(DriveDirection direction, double accelLimit) {
		this.drive.calculatePowers(direction);
		this.drive.scaleMotorsDown();
		this.drive.accelLimit(accelLimit);
		this.drive.commitPowers();
	}
	private boolean shooterFarTriggered() {
		return !this.shooterFar.getState();
	}
	private boolean shooterNearTriggered() {
		return !this.shooterNear.getState();
	}
	public void safeShooterStopper(double power) {
		if (
			(Math.signum(power) == Math.signum(MotorConstants.SHOOTER_STOPPER_UP) && this.shooterFarTriggered()) ||
			(Math.signum(power) == Math.signum(MotorConstants.SHOOTER_STOPPER_DOWN) && this.shooterNearTriggered())
		) {
			power = 0.0;
		}
		this.shooterStopper.setPower(power);
	}
	public void resetEncoder() {
		final MotorWrapper driveMotor = this.driveLeft;
		final RunMode lastMode = driveMotor.getRunMode();
		driveMotor.setRunMode(RunMode.STOP_AND_RESET_ENCODER);
		driveMotor.setRunMode(lastMode);
	}
	public int encoderPositive() {
		return Math.abs(this.driveLeft.encoderValue());
	}
	public boolean isShooterAtSensor() {
		return !this.shooterDown.getState();
	}
	public void shootSlow() {
		this.shooter.setRunMode(RunMode.RUN_USING_ENCODER);
		this.shooter.setPower(MotorConstants.SHOOTER_BACK_SLOW);
	}
	public void shootFast() {
		this.shooter.setRunMode(RunMode.RUN_USING_ENCODER);
		this.shooter.setPower(MotorConstants.SHOOTER_BACK);
	}
	public void advanceShooterToDown() {
		if (!this.shooterWasDown) {
			if (this.isShooterAtSensor()) {
				this.shooter.setRunMode(RunMode.STOP_AND_RESET_ENCODER);
				this.shooter.setRunMode(RunMode.RUN_TO_POSITION);
				this.shooter.setTarget(MotorConstants.SHOOTER_TICKS_TO_DOWN);
				this.shooter.setPower(MotorConstants.SHOOTER_BACK_SLOW);
				this.shooterWasDown = true;
			}
			else this.shootSlow();
		}
	}
	public void teleopAdvanceShooterToDown() {
		if (!this.shooterWasDown) {
			if (this.isShooterAtSensor()) {
				this.shooter.setRunMode(RunMode.STOP_AND_RESET_ENCODER);
				this.shooter.setRunMode(RunMode.RUN_TO_POSITION);
				this.shooter.setTarget(MotorConstants.SHOOTER_TICKS_TO_DOWN);
				this.shooter.setPower(MotorConstants.SHOOTER_BACK_SLOW);
				this.shooterWasDown = true;
			}
			else this.shootFast();
		}
	}
	public void resetAutoShooter() {
		this.shooterWasDown = false;
	}
	public boolean isShooterDown() {
		return this.shooterWasDown && !this.shooter.isBusy();
	}

	public static abstract class MotorConstants {
		public static final double INTAKE_IN = 1.0;
		public static final double INTAKE_OUT = -INTAKE_IN;

		public static final double SHOOTER_FORWARD = 1.0;
		public static final double SHOOTER_BACK = -SHOOTER_FORWARD;
		public static final double SHOOTER_BACK_SLOW = SHOOTER_BACK * 0.5;
		public static final int SHOOTER_TICKS_PER_ROTATION = -1870;
		public static final int SHOOTER_TICKS_TO_DOWN = (int)(MotorConstants.SHOOTER_TICKS_PER_ROTATION * 0.1);

		public static final double SNAKE_HOLDING = 0.9;
		public static final double SNAKE_DUMPING = 0.7;

		public static final double CLUTCH_CLUTCHED = 0.7;
		public static final double CLUTCH_ENGAGED = 0.3;

		public static final double SHOOTER_STOPPER_UP = 1.0;
		public static final double SHOOTER_STOPPER_DOWN = -SHOOTER_STOPPER_UP;

		//Flips a servo to the other range
		private static double invert(double pos) {
			return 1.0 - pos;
		}
	}
}
