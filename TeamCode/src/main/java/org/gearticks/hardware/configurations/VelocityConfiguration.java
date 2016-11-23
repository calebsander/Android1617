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
	public final MotorWrapper driveLeft, driveRight;
	public final TankDrive drive;
	public final Servo clutch, particleBlocker;
	public final CRServo shooterStopper;
	public final BNO055 imu;
	public final DigitalChannel shooterDown;
	public final DigitalChannel shooterNear, shooterFar;
	private boolean shooterWasDown;

	public VelocityConfiguration(HardwareMap hardwareMap) {
		this.intake = new MotorWrapper((DcMotor)hardwareMap.get("intake"), MotorWrapper.MotorType.NEVEREST_40);
		this.shooter = new MotorWrapper((DcMotor)hardwareMap.get("shooter"), MotorWrapper.MotorType.NEVEREST_40);
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
		this.clutch.setPosition(MotorConstants.CLUTCH_CLUTCHED);
		this.particleBlocker = (Servo)hardwareMap.get("particleBlocker");
		this.particleBlocker.setPosition(MotorConstants.PARTICLE_BLOCKER_BLOCKING);
		this.shooterStopper = (CRServo)hardwareMap.get("shooterStopper");
		this.shooterStopper.setPower(0.0);

		this.imu = new BNO055((I2cDevice)hardwareMap.get("bno"));
		this.shooterDown = (DigitalChannel)hardwareMap.get("shooterDown");
		this.shooterDown.setMode(Mode.INPUT);
		this.shooterNear = (DigitalChannel)hardwareMap.get("shooterNear");
		this.shooterNear.setMode(Mode.INPUT);
		this.shooterFar = (DigitalChannel)hardwareMap.get("shooterFar");
		this.shooterFar.setMode(Mode.INPUT);

		this.shooterWasDown = false;
	}
	public void teardown() {}
	public void stopMotion() {
		this.intake.stop();
		this.driveLeft.stop();
		this.driveRight.stop();
	}

	public void move(DriveDirection direction) {
		this.drive.calculatePowers(direction);
		this.drive.scaleMotorsDown();
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
	public boolean isShooterDown() {
		return !this.shooterDown.getState();
	}
	public void advanceShooterToSensor() {
		if (this.isShooterDown()) {
			if (!this.shooterWasDown) {
				this.shooter.setRunMode(RunMode.STOP_AND_RESET_ENCODER);
				this.shooter.setRunMode(RunMode.RUN_TO_POSITION);
				this.shooter.setPower(MotorConstants.SHOOTER_BACK_SLOW);
				this.shooter.setTarget(MotorConstants.SHOOTER_TICKS_TO_DOWN);
				this.shooterWasDown = true;
			}
		}
		else {
			this.shooter.setRunMode(RunMode.RUN_USING_ENCODER);
			this.shooter.setPower(MotorConstants.SHOOTER_BACK_SLOW);
		}
	}
	public void resetAutoShooter() {
		this.shooterWasDown = false;
	}

	public static abstract class MotorConstants {
		public static final double INTAKE_IN = 1.0;
		public static final double INTAKE_OUT = -INTAKE_IN;

		public static final double SHOOTER_FORWARD = 0.5;
		public static final double SHOOTER_BACK = -SHOOTER_FORWARD;
		public static final double SHOOTER_BACK_SLOW = SHOOTER_BACK * 0.5;
		public static final int SHOOTER_TICKS_PER_ROTATION = -1870;
		public static final int SHOOTER_TICKS_TO_DOWN = (int)(MotorConstants.SHOOTER_TICKS_PER_ROTATION * 0.2);

		public static final double PARTICLE_BLOCKER_BLOCKING = 0.82;
		public static final double PARTICLE_BLOCKER_AWAY = 1.0;

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
