package org.gearticks.hardware.configurations;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
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

@SuppressWarnings("deprecation")
public class VelocityConfiguration implements HardwareConfiguration {
	private final boolean v2;
	public final MotorWrapper intake, shooter;
	public final MotorWrapper capBall;
	private boolean shooterWasDown;
	private boolean shooterPassedEncoder;
	public final MotorWrapper driveLeft, driveRight;
	public final TankDrive drive;
	public final Servo clutch, snake, frontBeaconPresser, backBeaconPresser;
	private final Servo frontRoller, rearRoller;
	public final Servo topLatch;
	private final CRServo shooterStopper;
	public final CRServo frontBumper;
	public final GearticksBNO055 imu;
	public final GearticksMRRangeSensor rangeSensor;
	private final DigitalChannel shooterDown;
	private final DigitalChannel shooterNear, shooterFar;
	private final DigitalChannel badBoy1, badBoy2;
	private final DigitalChannel whiteLineSensor;
	private final DigitalChannel capBallLimit;
	public final TCS34725 whiteLineColorSensor;
	public final LED whiteLineColorLed;
	public final DeviceInterfaceModule dim;

	public VelocityConfiguration(HardwareMap hardwareMap) {
		this(hardwareMap, false);
	}
	public VelocityConfiguration(HardwareMap hardwareMap, boolean v2) {
		this(hardwareMap, v2, false);
	}
	public VelocityConfiguration(HardwareMap hardwareMap, boolean v2, boolean autonomous) {
		this.v2 = v2;
		this.intake = new MotorWrapper((DcMotor) hardwareMap.get("intake"), MotorType.NEVEREST_20);
		this.shooter = new MotorWrapper((DcMotor) hardwareMap.get("shooter"), MotorType.NEVEREST_40);
		this.shooter.setRunMode(RunMode.STOP_AND_RESET_ENCODER);
		this.shooter.setRunMode(RunMode.RUN_USING_ENCODER);
		this.resetAutoShooter();
		if (v2) {
			this.capBall = new MotorWrapper((DcMotor) hardwareMap.get("capBall"), MotorType.NEVEREST_20);
			this.capBall.setRunMode(RunMode.STOP_AND_RESET_ENCODER);
			this.capBall.setRunMode(RunMode.RUN_USING_ENCODER);
		}
		else this.capBall = null;
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
		final double initialClutchPosition;
		if (v2) initialClutchPosition = MotorConstants.CLUTCH_V2_ENGAGED;
		else initialClutchPosition = MotorConstants.CLUTCH_ENGAGED;
		this.clutch.setPosition(initialClutchPosition);
		if (v2) {
			this.frontBeaconPresser = (Servo) hardwareMap.get("frontBeacon");
			this.backBeaconPresser = (Servo) hardwareMap.get("backBeacon");
			final double frontInitialPresserPosition, backInitialPresserPosition;
			frontInitialPresserPosition = MotorConstants.PRESSER_V2_FRONT_IN;
			backInitialPresserPosition = MotorConstants.PRESSER_V2_BACK_IN;
			this.frontBeaconPresser.setPosition(frontInitialPresserPosition);
			this.backBeaconPresser.setPosition(backInitialPresserPosition);
		}
		else {
			this.frontBeaconPresser = null;
			this.backBeaconPresser = null;
		}
		this.snake = (Servo) hardwareMap.get("snake");
		final double initialSnakePosition;
		if (v2) initialSnakePosition = MotorConstants.SNAKE_V2_HOLDING;
		else initialSnakePosition = MotorConstants.SNAKE_HOLDING;
		this.snake.setPosition(initialSnakePosition);
		if (v2) {
			this.frontRoller = (Servo)hardwareMap.get("frontRoller");
			this.rearRoller = (Servo)hardwareMap.get("rearRoller");
			this.rollersUp();
		}
		else {
			this.frontRoller = this.rearRoller = null;
		}

		if (v2) {
			this.topLatch = (Servo)hardwareMap.get("topLatch");
			if (autonomous) this.engageTopLatch();
			else this.disengageTopLatch();
		}
		else this.topLatch = null;

		this.shooterStopper = (CRServo) hardwareMap.get("shooterStopper");
		this.shooterStopper.setPower(0.0);
		if (v2) {
			this.frontBumper = (CRServo)hardwareMap.get("frontBumper");
			this.frontBumper.setPower(0.0);
		}
		else this.frontBumper = null;

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
		this.whiteLineSensor.setMode(Mode.INPUT);
		if (v2) {
			this.capBallLimit = (DigitalChannel) hardwareMap.get("capBallLimit");
			this.capBallLimit.setMode(Mode.INPUT);
		}
		else this.capBallLimit = null;
		this.whiteLineColorSensor = new TCS34725((I2cDevice) hardwareMap.get("whiteLineColor"));
		this.whiteLineColorLed = (LED) hardwareMap.get("whiteLineColorLed");

		this.dim = (DeviceInterfaceModule)hardwareMap.get("DIM");
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

	public boolean shooterFarTriggered() {
		return !this.shooterFar.getState();
	}

	public boolean shooterNearTriggered() {
		return !this.shooterNear.getState();
	}

	public void safeShooterStopper(double power) {
		if (
			(Math.signum(power) == Math.signum(MotorConstants.SHOOTER_STOPPER_UP) && this.shooterFarTriggered()) ||
			(Math.signum(power) == Math.signum(MotorConstants.SHOOTER_STOPPER_DOWN) && this.shooterNearTriggered())
		) {
			power = MotorWrapper.STOPPED;
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
		final double power;
		power = MotorConstants.SHOOTER_BACK_SLOW;
		this.shooter.setPower(power);
	}
	public void shootSlowAutonomous() {
		this.shooter.setRunMode(RunMode.RUN_USING_ENCODER);
		final double power;
		power = MotorConstants.SHOOTER_BACK_SLOW_AUTONOMOUS;
		this.shooter.setPower(power);
	}
	public void shootFast() {
		this.shooter.setRunMode(RunMode.RUN_USING_ENCODER);
		this.shooter.setPower(MotorConstants.SHOOTER_BACK);
	}

	public void advanceShooterToDownSlowly() {
		if (!this.shooterWasDown) {
			if (this.isShooterAtSensor()) {
				this.shooter.setRunMode(RunMode.STOP_AND_RESET_ENCODER);
				if (this.v2) {
					this.shooter.setRunMode(RunMode.RUN_WITHOUT_ENCODER);
					this.shooter.stop();
				}
				else {
					this.shooter.setRunMode(RunMode.RUN_TO_POSITION);
					this.shooter.setTarget(MotorConstants.SHOOTER_TICKS_TO_DOWN);
					this.shooter.setPower(MotorConstants.SHOOTER_BACK);
				}
				this.shooterWasDown = true;
				this.shooterPassedEncoder = true;
			}
			else {
				this.shooter.setRunMode(RunMode.RUN_USING_ENCODER);
				this.shooter.setPower(MotorConstants.SHOOTER_BACK_SUPER_SLOW);
			}
		}
	}
	public void advanceShooterToDownWithEncoder() {
		if (!this.shooterWasDown) {
			if (this.isShooterAtSensor()) {
				this.shooterPassedEncoder = true;
				this.shooter.setRunMode(RunMode.STOP_AND_RESET_ENCODER);
				if (this.v2) {
					this.shooter.setRunMode(RunMode.RUN_WITHOUT_ENCODER);
					this.shooter.stop();
				}
				else {
					this.shooter.setRunMode(RunMode.RUN_TO_POSITION);
					this.shooter.setTarget(MotorConstants.SHOOTER_TICKS_TO_DOWN);
					this.shooter.setPower(MotorConstants.SHOOTER_BACK);
				}
				this.shooterWasDown = true;
			}
			else if (Math.abs(this.shooter.encoderValue()) > Math.abs(MotorConstants.SHOOTER_TICKS_PER_ROTATION) - 450) {
				this.shooterPassedEncoder = true;
			}
			else if (Math.abs(this.shooter.encoderValue()) > Math.abs(MotorConstants.SHOOTER_TICKS_PER_ROTATION) - 300) {
				this.shootSlow();
			}
			else {
				this.shootFast();
			}
		}
	}

	public void advanceShooterToDownAutonomous() {
		if (!this.shooterWasDown) {
			if (this.isShooterAtSensor()) {
				this.shooterPassedEncoder = true;
				this.shooter.setRunMode(RunMode.STOP_AND_RESET_ENCODER);
				if (this.v2) {
					this.shooter.setRunMode(RunMode.RUN_WITHOUT_ENCODER);
					this.shooter.stop();
				}
				else {
					this.shooter.setRunMode(RunMode.RUN_TO_POSITION);
					this.shooter.setTarget(MotorConstants.SHOOTER_TICKS_TO_DOWN);
					this.shooter.setPower(MotorConstants.SHOOTER_BACK);
				}
				this.shooterWasDown = true;
				this.shooterPassedEncoder = true;
			}
			else if (this.shooter.encoderValue() > MotorConstants.SHOOTER_TICKS_PER_ROTATION - 250 * Math.signum(MotorConstants.SHOOTER_TICKS_PER_ROTATION)){
				this.shooterPassedEncoder = true;
				this.shootFast();
			}
			else {
				this.shooterPassedEncoder = false;
				this.shootSlowAutonomous();
			}
		}
	}

	public void resetAutoShooter() {
		this.shooterWasDown = false;
		this.shooterPassedEncoder = false;
	}

	public boolean hasShot() {
		return Math.signum(this.shooter.encoderValue() - MotorConstants.SHOOTER_V2_TICKS_TO_SHOOTING) == Math.signum(MotorConstants.SHOOTER_V2_TICKS_TO_SHOOTING);
	}

	public boolean isShooterAtTarget() {
		return Math.abs(this.shooter.encoderValue() - this.shooter.getTarget()) < 20;
	}

	public boolean isShooterDown() {
		if (this.v2) return this.shooterWasDown;
		else return this.shooterWasDown && this.isShooterAtTarget();
	}

	public boolean isShooterPassedEncoder() {
		return this.shooterPassedEncoder;
	}

	public void beaconPresserFrontOut() {
		this.frontBeaconPresser.setPosition(MotorConstants.PRESSER_V2_FRONT_OUT);
	}
	public void beaconPresserFrontOutPartial() {
		this.frontBeaconPresser.setPosition(MotorConstants.PRESSER_V2_FRONT_OUT_PARTIAL);
	}
	public void beaconPresserBackOut() {
		this.backBeaconPresser.setPosition(MotorConstants.PRESSER_V2_BACK_OUT);
	}
	public void beaconPresserBackOutPartial() {
		this.backBeaconPresser.setPosition(MotorConstants.PRESSER_V2_BACK_OUT_PARTIAL);
	}
	public void beaconPressersStrainIn() {
		this.frontBeaconPresser.setPosition(MotorConstants.PRESSER_V2_FRONT_IN_STRAIN);
		this.backBeaconPresser.setPosition(MotorConstants.PRESSER_V2_BACK_IN_STRAIN); // make back beacon presser
	}
	public void beaconPressersIn() {
		this.frontBeaconPresser.setPosition(MotorConstants.PRESSER_V2_FRONT_IN);
		this.backBeaconPresser.setPosition(MotorConstants.PRESSER_V2_BACK_IN); // make back beacon presser
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

	public void rollersUp() {
		this.frontRoller.setPosition(MotorConstants.FRONT_ROLLER_V2_UP);
		this.rearRoller.setPosition(MotorConstants.REAR_ROLLER_V2_UP);
	}
	public void rollersDown() {
		this.frontRoller.setPosition(MotorConstants.FRONT_ROLLER_V2_DOWN);
		this.rearRoller.setPosition(MotorConstants.REAR_ROLLER_V2_DOWN);
	}

	public void engageTopLatch() {
		this.topLatch.setPosition(MotorConstants.TOP_LATCH_ENGAGED);
	}
	public void disengageTopLatch() {
		this.topLatch.setPosition(MotorConstants.TOP_LATCH_DISENGAGED);
	}

	public boolean isCapBallUp() {
		return !capBallLimit.getState();
	}

	public static abstract class MotorConstants {
		public static final double INTAKE_OUT = 1.0;
		public static final double INTAKE_IN = -INTAKE_OUT;

		public static final double SHOOTER_FORWARD = 1.0;
		public static final double SHOOTER_BACK = -SHOOTER_FORWARD;
		public static final double SHOOTER_BACK_SLOW = SHOOTER_BACK * 0.20;
		public static final double SHOOTER_BACK_SLOW_AUTONOMOUS = SHOOTER_BACK * 0.2;
		public static final double SHOOTER_BACK_SUPER_SLOW = SHOOTER_BACK * 0.15;
		public static final int SHOOTER_TICKS_PER_ROTATION = -700;
		@Deprecated
		public static final int SHOOTER_TICKS_TO_DOWN = (int)(MotorConstants.SHOOTER_TICKS_PER_ROTATION * 0.1);
		@Deprecated
		public static final int SHOOTER_TICKS_TO_SHOOTING = (int)(MotorConstants.SHOOTER_TICKS_PER_ROTATION * 0.2);
		public static final int SHOOTER_V2_TICKS_TO_SHOOTING = -150;

		@Deprecated
		public static final double SNAKE_HOLDING = 0.9;
		public static final double SNAKE_V2_HOLDING = 0.0;
		@Deprecated
		public static final double SNAKE_DUMPING = 0.7;
		public static final double SNAKE_V2_DUMPING = 0.5;
		public static final double SNAKE_V2_TIME_TO_MOVE = 0.3; //seconds for snake to switch positions

		@Deprecated
		public static final double BEACON_PRESSER_DISENGAGED = 0.81;
		public static final double PRESSER_V2_NEUTRAL = 0.5;
		@Deprecated
		public static final double BEACON_PRESSER_RIGHT_ENGAGED = 1.0;
		public static final double PRESSER_V2_RIGHT = 1.0;
		@Deprecated
		public static final double BEACON_PRESSER_LEFT_ENGAGED = 0.54;
		public static final double PRESSER_V2_FRONT_IN = 0.74;
		public static final double PRESSER_V2_FRONT_IN_STRAIN = 0.9;
		public static final double PRESSER_V2_FRONT_OUT_PARTIAL = 0.7;
		public static final double PRESSER_V2_FRONT_OUT = 0.2;
		public static final double PRESSER_V2_BACK_IN = 0.30;
		public static final double PRESSER_V2_BACK_IN_STRAIN = 0.1;
		public static final double PRESSER_V2_BACK_OUT_PARTIAL = 0.4;
		public static final double PRESSER_V2_BACK_OUT = 0.7;
		public static final double PRESSER_V2_TIME_TO_MOVE = 0.5; //seconds for beacon presser to switch positions
		public static final int PRESSER_V2_TIMES_PULL_IN = 3; //number of times the presser pulls to straining position until in place

		@Deprecated
		public static final double CLUTCH_CLUTCHED = 0.7;
		public static final double CLUTCH_V2_CLUTCHED = 0.94;
		@Deprecated
		public static final double CLUTCH_ENGAGED = 0.3;
		public static final double CLUTCH_V2_ENGAGED = 0.3;

		public static final double FRONT_ROLLER_V2_UP = 0.4;
		public static final double FRONT_ROLLER_V2_DOWN = 0.88;
		public static final double REAR_ROLLER_V2_UP = 1.0;
		public static final double REAR_ROLLER_V2_DOWN = 0.5;

		public static final double SHOOTER_STOPPER_UP = 1.0;
		public static final double SHOOTER_STOPPER_DOWN = -SHOOTER_STOPPER_UP;

		public static final double CAP_BALL_UP = 1.0;
		public static final double CAP_BALL_DOWN = -CAP_BALL_UP * 0.1;
		public static final double CAP_BALL_SLOW_SCALE = 0.3, CAP_BALL_SUPER_SLOW_UP = CAP_BALL_UP * 0.1;

		public static final double FRONT_BUMPER_UP = 1.0;
		public static final double FRONT_BUMPER_DOWN = -FRONT_BUMPER_UP;

		public static final double TOP_LATCH_ENGAGED = 0.55;
		public static final double TOP_LATCH_DISENGAGED = 0.0;
	}
}
