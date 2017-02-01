package org.gearticks.hardware.configurations;

import android.util.Log;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController.Mode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.Servo;
import org.gearticks.dimsensors.i2c.GearticksBNO055;
import org.gearticks.dimsensors.i2c.GearticksMRRangeSensor;
import org.gearticks.dimsensors.i2c.TCS34725;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.hardware.drive.MotorWrapper.MotorType;
import org.gearticks.hardware.drive.TankDrive;
import org.gearticks.opmodes.utility.Utils;

public class VelocityConfiguration implements HardwareConfiguration {
	public final MotorWrapper intake, shooter, beaconBumpers, capball;
	private boolean shooterWasDown;
	public final MotorWrapper driveLeft, driveRight;
	public final TankDrive drive;
	public final Servo clutch, snake, beaconPresser;
	private final CRServo shooterStopper;
	public final GearticksBNO055 imu;
	public final GearticksMRRangeSensor rangeSensor;
	private final DigitalChannel shooterDown;
	private final DigitalChannel shooterNear, shooterFar;
	private final DigitalChannel badBoy1, badBoy2;
	private final DigitalChannel whiteLineSensor;
	public final TCS34725 whiteLineColorSensor;
	public final LED whiteLineColorLed;

	public VelocityConfiguration(HardwareMap hardwareMap) {
		this.intake = new MotorWrapper((DcMotor) hardwareMap.get("intake"), MotorType.NEVEREST_20);
		this.shooter = new MotorWrapper((DcMotor) hardwareMap.get("shooter"), MotorType.NEVEREST_40);
		this.shooter.setRunMode(RunMode.STOP_AND_RESET_ENCODER);
		this.shooter.setRunMode(RunMode.RUN_USING_ENCODER);
		this.capball = new MotorWrapper((DcMotor) hardwareMap.get("capball"), MotorType.NEVEREST_40);
		this.resetAutoShooter();
		this.beaconBumpers = new MotorWrapper((DcMotor)hardwareMap.get("shooter"), MotorType.NEVEREST_40);
		this.beaconBumpers.setRunMode(RunMode.STOP_AND_RESET_ENCODER);
		this.beaconBumpers.setRunMode(RunMode.RUN_WITHOUT_ENCODER);
		this.driveLeft = new MotorWrapper((DcMotor) hardwareMap.get("left"), MotorType.NEVEREST_20, true);
		this.driveRight = new MotorWrapper((DcMotor) hardwareMap.get("right"), MotorType.NEVEREST_20, true);
		this.drive = new TankDrive();
		this.drive.addLeftMotor(this.driveLeft);
		this.drive.addRightMotor(this.driveRight);
		this.driveLeft.setRunMode(RunMode.STOP_AND_RESET_ENCODER);
		this.driveRight.setRunMode(RunMode.STOP_AND_RESET_ENCODER);
		this.driveLeft.setRunMode(RunMode.RUN_USING_ENCODER);
		this.driveRight.setRunMode(RunMode.RUN_USING_ENCODER);
		this.driveLeft.setStopMode(ZeroPowerBehavior.BRAKE);
		this.driveRight.setStopMode(ZeroPowerBehavior.BRAKE);

		this.clutch = (Servo) hardwareMap.get("clutch");
		this.clutch.setPosition(MotorConstants.CLUTCH_ENGAGED);
		this.beaconPresser = (Servo) hardwareMap.get("beacon");
		this.beaconPresser.setPosition(MotorConstants.BEACON_PRESSER_DISENGAGED);
		this.snake = (Servo) hardwareMap.get("snake");
		this.snake.setPosition(MotorConstants.SNAKE_HOLDING);
		this.shooterStopper = (CRServo) hardwareMap.get("shooterStopper");
		this.shooterStopper.setPower(0.0);

		this.imu = new GearticksBNO055((I2cDevice) hardwareMap.get("bno"));
		this.rangeSensor = new GearticksMRRangeSensor((I2cDevice) hardwareMap.get("range"));
		this.shooterDown = (DigitalChannel) hardwareMap.get("shooterDown");
		this.shooterDown.setMode(Mode.INPUT);
		this.shooterNear = (DigitalChannel) hardwareMap.get("shooterNear");
		this.shooterNear.setMode(Mode.INPUT);
		this.shooterFar = (DigitalChannel) hardwareMap.get("shooterFar");
		this.shooterFar.setMode(Mode.INPUT);
		this.badBoy1 = (DigitalChannel) hardwareMap.get("badBoy1");
		this.badBoy1.setMode(Mode.INPUT);
		this.badBoy2 = (DigitalChannel) hardwareMap.get("badBoy2");
		this.badBoy2.setMode(Mode.INPUT);
		this.whiteLineSensor = (DigitalChannel) hardwareMap.get("whiteLine");
		this.whiteLineColorSensor = new TCS34725((I2cDevice) hardwareMap.get("whiteLineColor"));
		this.whiteLineColorLed = (LED) hardwareMap.get("whiteLineColorLed");
	}

	public void teardown() {
		this.imu.terminate();
		this.rangeSensor.terminate();
	}

	public void stopMotion() {
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

	private boolean badBoy1Triggered() {
		return !this.badBoy1.getState();
	}

	private boolean badBoy2Triggered() {
		return !this.badBoy2.getState();
	}

	public boolean ballInSnake() {
		return this.badBoy1Triggered() || this.badBoy2Triggered();
	}

	public void resetEncoder() {
		final RunMode leftLastMode = this.driveLeft.getRunMode();
		this.driveLeft.setRunMode(RunMode.STOP_AND_RESET_ENCODER);
		final RunMode rightLastMode = this.driveRight.getRunMode();
		this.driveRight.setRunMode(RunMode.STOP_AND_RESET_ENCODER);

		this.driveLeft.setRunMode(leftLastMode);
		this.driveRight.setRunMode(rightLastMode);
	}
	/**
	 * Returns an encoder value from the drive motors.
	 * Sign flips depending on direction of motion
	 * @return a combination of the encoder values from the two drive motors
	 */
	public int signedEncoder() {
		return (this.driveLeft.encoderValue() - this.driveRight.encoderValue()) / 2;
	}
	public int encoderPositive() {
		return Math.abs(this.signedEncoder());
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
			} else this.shootSlow();
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
			} else this.shootFast();
		}
	}

	public void resetAutoShooter() {
		this.shooterWasDown = false;
	}

	public boolean isShooterAtTarget() {
		return Math.abs(this.shooter.encoderValue() - this.shooter.getTarget()) < 10;
	}

	public boolean isShooterDown() {
		return this.shooterWasDown && this.isShooterAtTarget();
	}

	public void beaconPresserEngageLeft() {
		this.beaconPresser.setPosition(MotorConstants.BEACON_PRESSER_LEFT_ENGAGED);
	}
	public void beaconPresserEngageRight() {
		this.beaconPresser.setPosition(MotorConstants.BEACON_PRESSER_RIGHT_ENGAGED);
	}
	public void beaconPresserDisengage() {
		this.beaconPresser.setPosition(MotorConstants.BEACON_PRESSER_DISENGAGED);
	}

	/**
	 *
	 * @return true if white line detected
	 */
	public boolean isWhiteLineColor() {
		boolean isWhiteLine = false;
		int clear = this.whiteLineColorSensor.getClear();
		Log.v(Utils.TAG, "Clear = " + clear);
		int whiteLineThreshold = 275;

		if (clear > whiteLineThreshold){
			isWhiteLine = true;
		}

		return isWhiteLine;
	}

	public void activateWhiteLineColor(){
		this.whiteLineColorSensor.startReadingClear();
		this.whiteLineColorLed.enable(true);
		this.whiteLineColorSensor.setIntegrationTime(50);

	}
	public void deactivateWhiteLineColor(){
		this.whiteLineColorSensor.stopReading();
		this.whiteLineColorLed.enable(false);
	}

	/**
	 *
	 * @return true if white line detected
	 */
	public boolean isWhiteLineIR() {
		return !this.whiteLineSensor.getState();
	}

	public static abstract class MotorConstants {
		public static final double INTAKE_OUT = 1.0;
		public static final double INTAKE_IN = -INTAKE_OUT;

		public static final double CAPBALL_UP = 1.0;
		public static final double CAPBALL_DOWN = -CAPBALL_UP;

		public static final double SHOOTER_FORWARD = 1.0;
		public static final double SHOOTER_BACK = -SHOOTER_FORWARD;
		public static final double SHOOTER_BACK_SLOW = SHOOTER_BACK * 0.5;
		public static final int SHOOTER_TICKS_PER_ROTATION = -1870;
		public static final int SHOOTER_TICKS_TO_DOWN = (int)(MotorConstants.SHOOTER_TICKS_PER_ROTATION * 0.1);
		public static final int SHOOTER_TICKS_TO_SHOOTING = (int)(MotorConstants.SHOOTER_TICKS_PER_ROTATION * 0.2);

		public static final double SNAKE_HOLDING = 0.25;
		public static final double SNAKE_DUMPING = 0.61;

		public static final double ROLLERS_DOWN = 0.0;
		public static final double ROLLERS_UP = 1.0;

		public static final double BEACON_PRESSER_RIGHT_ENGAGED = 1.0; //TODO: fix value
		public static final double BEACON_PRESSER_LEFT_ENGAGED = 0.0;
		public static final double BEACON_PRESSER_DISENGAGED = 0.25;

		public static final double CLUTCH_CLUTCHED = 1.0;
		public static final double CLUTCH_ENGAGED = 0.5;

		public static final double SHOOTER_STOPPER_UP = 1.0;
		public static final double SHOOTER_STOPPER_DOWN = -SHOOTER_STOPPER_UP;
	}
}
