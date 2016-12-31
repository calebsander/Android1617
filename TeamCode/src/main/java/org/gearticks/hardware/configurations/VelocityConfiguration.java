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
import org.gearticks.hardware.drive.TankDrive;
import org.gearticks.opmodes.utility.Utils;

/**
 * Stores all the hardware devices on the Velocity Vortex robot.
 * Has code for fetching them out of a {@link HardwareMap}
 * and several utility methods for them.
 */
public class VelocityConfiguration implements HardwareConfiguration {
	/**
	 * The color sensor clear value above which is considered on the white line
	 */
	private static final int WHITE_LINE_THRESHOLD = 280;
	/**
	 * The number of encoder ticks the shooter is allowed to be from its target
	 * and be considered to have reached it.
	 * This is used rather than {@link MotorWrapper#notAtTarget()} because
	 * sometimes the shooter is not given enough power to quite reach the target.
	 */
	private static final int SHOOTER_ENCODER_THRESHOLD = 10;

	/**
	 * The intake motor
	 */
	public final MotorWrapper intake;
	/**
	 * The shooter motor
	 */
	public final MotorWrapper shooter;
	/**
	 * Whether the shooter has passed the sensor
	 * since being commanded to move down
	 */
	private boolean shooterWasAtSensor;
	/**
	 * The left drive motor (attached to 2 motors)
	 */
	public final MotorWrapper driveLeft;
	/**
	 * The right drive motor (attached to 2 motors)
	 */
	public final MotorWrapper driveRight;
	/**
	 * The drive system
	 */
	public final TankDrive drive;
	/**
	 * The clutch servo
	 */
	public final Servo clutch;
	/**
	 * The snake servo
	 */
	public final Servo snake;
	/**
	 * The shooter stopper CR servo.
	 * This is private because it should always be moved with {@link #safeShooterStopper(double)}.
	 */
	private final CRServo shooterStopper;
	/**
	 * The heading sensor
	 */
	public final GearticksBNO055 imu;
	/**
	 * The range sensor pointing towards the wall
	 */
	public final GearticksMRRangeSensor rangeSensor;
	/**
	 * The IR proximity sensor on the shooter
	 */
	private final DigitalChannel shooterDown;
	/**
	 * The limit switch at the bottom of the shooter stopper
	 */
	private final DigitalChannel shooterNear;
	/**
	 * The limit switch at the top of the shooter stopper
	 */
	private final DigitalChannel shooterFar;
	/**
	 * One of the IR proximity sensors next to the snake
	 */
	private final DigitalChannel badBoy1, badBoy2;
	/**
	 * The color sensor used to find the white line
	 * in front of the beacons
	 */
	public final TCS34725 whiteLineColorSensor;
	/**
	 * The LED on {@link #whiteLineColorSensor}
	 */
	public final LED whiteLineColorLed;

	/**
	 * Creates a new configuration from a populated hardware map.
	 * The "Velocity Drive" configuration file must be active.
	 * @param hardwareMap the hardware map containing all the devices
	 */
	public VelocityConfiguration(HardwareMap hardwareMap) {
		//Motors
		this.intake = new MotorWrapper((DcMotor)hardwareMap.get("intake"), MotorWrapper.MotorType.NEVEREST_20);
		this.shooter = new MotorWrapper((DcMotor)hardwareMap.get("shooter"), MotorWrapper.MotorType.NEVEREST_40);
		this.shooter.setRunMode(RunMode.STOP_AND_RESET_ENCODER);
		this.shooter.setRunMode(RunMode.RUN_USING_ENCODER);
		this.resetAutoShooter();
		this.driveLeft = new MotorWrapper((DcMotor)hardwareMap.get("left"), MotorWrapper.MotorType.NEVEREST_20, true);
		this.driveRight = new MotorWrapper((DcMotor)hardwareMap.get("right"), MotorWrapper.MotorType.NEVEREST_20, true);
		this.drive = new TankDrive();
		this.drive.addLeftMotor(this.driveLeft);
		this.drive.addRightMotor(this.driveRight);
		this.driveLeft.setRunMode(RunMode.STOP_AND_RESET_ENCODER);
		this.driveRight.setRunMode(RunMode.STOP_AND_RESET_ENCODER);
		this.driveLeft.setRunMode(RunMode.RUN_USING_ENCODER);
		this.driveRight.setRunMode(RunMode.RUN_USING_ENCODER);
		this.driveLeft.setStopMode(ZeroPowerBehavior.BRAKE);
		this.driveRight.setStopMode(ZeroPowerBehavior.BRAKE);

		//Servos
		this.clutch = (Servo)hardwareMap.get("clutch");
		this.clutch.setPosition(MotorConstants.CLUTCH_ENGAGED);
		this.snake = (Servo)hardwareMap.get("snake");
		this.snake.setPosition(MotorConstants.SNAKE_HOLDING);
		this.shooterStopper = (CRServo)hardwareMap.get("shooterStopper");
		this.shooterStopper.setPower(0.0);

		//Sensors
		this.imu = new GearticksBNO055((I2cDevice)hardwareMap.get("bno"));
		this.rangeSensor = new GearticksMRRangeSensor((I2cDevice)hardwareMap.get("range"));
		this.shooterDown = (DigitalChannel)hardwareMap.get("shooterDown");
		this.shooterDown.setMode(Mode.INPUT);
		this.shooterNear = (DigitalChannel)hardwareMap.get("shooterNear");
		this.shooterNear.setMode(Mode.INPUT);
		this.shooterFar = (DigitalChannel)hardwareMap.get("shooterFar");
		this.shooterFar.setMode(Mode.INPUT);
		this.badBoy1 = (DigitalChannel)hardwareMap.get("badBoy1");
		this.badBoy1.setMode(Mode.INPUT);
		this.badBoy2 = (DigitalChannel)hardwareMap.get("badBoy2");
		this.badBoy2.setMode(Mode.INPUT);
		this.whiteLineColorSensor = new TCS34725((I2cDevice)hardwareMap.get("whiteLineColor"));
		this.whiteLineColorLed = (LED)hardwareMap.get("whiteLineColorLed");
	}

	public void teardown() {
		this.imu.terminate();
		this.rangeSensor.terminate();
		this.whiteLineColorSensor.terminate();
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
	/**
	 * Returns whether the shooter stopper is at the top of its range
	 * @return whether the shooter stopper is at the top of its range
	 */
	private boolean shooterFarTriggered() {
		return !this.shooterFar.getState();
	}
	/**
	 * Returns whether the shooter stopper is at the bottom of its range
	 * @return whether the shooter stopper is at the bottom of its range
	 */
	private boolean shooterNearTriggered() {
		return !this.shooterNear.getState();
	}
	/**
	 * Moves the shooter stopper with the desired power,
	 * stopping it if it is at the limit of its range
	 * @param power the desired power
	 */
	public void safeShooterStopper(double power) {
		if (
			(Math.signum(power) == Math.signum(MotorConstants.SHOOTER_STOPPER_UP) && this.shooterFarTriggered()) ||
			(Math.signum(power) == Math.signum(MotorConstants.SHOOTER_STOPPER_DOWN) && this.shooterNearTriggered())
		) {
			power = 0.0;
		}
		this.shooterStopper.setPower(power);
	}
	/**
	 * Returns whether the first bad boy detects a ball in the snake
	 * @return whether the first bad boy detects a ball in the snake
	 */
	private boolean badBoy1Triggered() {
		return !this.badBoy1.getState();
	}
	/**
	 * Returns whether the second bad boy detects a ball in the snake
	 * @return whether the second bad boy detects a ball in the snake
	 */
	private boolean badBoy2Triggered() {
		return !this.badBoy2.getState();
	}
	/**
	 * Returns whether there is a ball in the snake
	 * @return whether either bad boy is triggered
	 */
	public boolean ballInSnake() {
		return this.badBoy1Triggered() || this.badBoy2Triggered();
	}
	/**
	 * Resets the encoder on the drive motor
	 * whose encoder value is returned by {@link #encoderPositive()}
	 */
	public void resetEncoder() {
		final MotorWrapper driveMotor = this.driveLeft;
		final RunMode lastMode = driveMotor.getRunMode();
		driveMotor.setRunMode(RunMode.STOP_AND_RESET_ENCODER);
		driveMotor.setRunMode(lastMode);
	}
	/**
	 * Returns the absolute value of the encoder value
	 * on the left drive motor (arbitrarily chosen)
	 * @return the encoder distance traveled since the last reset
	 */
	public int encoderPositive() {
		return Math.abs(this.driveLeft.encoderValue());
	}
	/**
	 * Returns whether the shooter is at its proximity sensor
	 * @return whether the shooter is at its proximity sensor
	 */
	public boolean isShooterAtSensor() {
		return !this.shooterDown.getState();
	}
	/**
	 * Moves the shooter at a slow constant speed
	 */
	public void shootSlow() {
		this.shooter.setRunMode(RunMode.RUN_USING_ENCODER);
		this.shooter.setPower(MotorConstants.SHOOTER_BACK_SLOW);
	}
	/**
	 * Moves the shooter at a fast constant speed
	 */
	public void shootFast() {
		this.shooter.setRunMode(RunMode.RUN_USING_ENCODER);
		this.shooter.setPower(MotorConstants.SHOOTER_BACK);
	}
	/**
	 * Moves the shooter from the sensor to the down position using the encoder.
	 * Only needs to be called once to carry out the full movement.
	 * Sets {@link #shooterWasAtSensor} to true.
	 */
	private void moveShooterFromSensorToDown() {
		this.shooter.setRunMode(RunMode.STOP_AND_RESET_ENCODER);
		this.shooter.setRunMode(RunMode.RUN_TO_POSITION);
		this.shooter.setTarget(MotorConstants.SHOOTER_TICKS_TO_DOWN);
		this.shooter.setPower(MotorConstants.SHOOTER_BACK_SLOW);
		this.shooterWasAtSensor = true;
	}
	/**
	 * Slowly advances the shooter to down.
	 * Must be called repeatedly.
	 */
	public void advanceShooterToDown() {
		if (!this.shooterWasAtSensor) {
			if (this.isShooterAtSensor()) this.moveShooterFromSensorToDown();
			else this.shootSlow();
		}
	}
	/**
	 * Quickly advances the shooter to down.
	 * Must be called repeatedly.
	 */
	public void teleopAdvanceShooterToDown() {
		if (!this.shooterWasAtSensor) {
			if (this.isShooterAtSensor()) this.moveShooterFromSensorToDown();
			else this.shootFast();
		}
	}
	/**
	 * Resets the variable keeping storing whether the shooter has reached the sensor.
	 * Must be called between subsequent movements of the shooter to the down position.
	 */
	public void resetAutoShooter() {
		this.shooterWasAtSensor = false;
	}
	/**
	 * Returns whether the shooter is pretty close to the target
	 * @return whether the shooter encoder is within {@link #SHOOTER_ENCODER_THRESHOLD} ticks of its target
	 */
	public boolean isShooterAtTarget() {
		return Math.abs(this.shooter.encoderValue() - this.shooter.getTarget()) < SHOOTER_ENCODER_THRESHOLD;
	}
	/**
	 * Returns whether the shooter has reached the down position
	 * @return whether the shooter has reached the down position
	 * @see #advanceShooterToDown()
	 * @see #teleopAdvanceShooterToDown()
	 */
	public boolean isShooterDown() {
		return this.shooterWasAtSensor && this.isShooterAtTarget();
	}
	/**
	 * @return true iff white line detected
	 */
	public boolean isWhiteLine() {
		final int clear = this.whiteLineColorSensor.getClear();
		Log.v(Utils.TAG, "Clear = " + clear);
		return clear > WHITE_LINE_THRESHOLD;
	}
	/**
	 * Starts reading values from the color sensor,
	 * turns on its LED,
	 * and sets the integration time.
	 */
	public void activateWhiteLineColor() {
		this.whiteLineColorSensor.setIntegrationTime(50.0);
		this.whiteLineColorSensor.startReadingClear();
		this.whiteLineColorLed.enable(true);
	}
	/**
	 * Stops reading values from the color sensor
	 * and disables its LED
	 */
	public void deactivateWhiteLineColor() {
		this.whiteLineColorSensor.stopReading();
		this.whiteLineColorLed.enable(false);
	}

	/**
	 * Constants for motor powers and servo positions
	 */
	public static abstract class MotorConstants {
		public static final double INTAKE_OUT = 1.0;
		public static final double INTAKE_IN = -INTAKE_OUT;

		public static final double SHOOTER_FORWARD = 1.0;
		public static final double SHOOTER_BACK = -SHOOTER_FORWARD;
		public static final double SHOOTER_BACK_SLOW = SHOOTER_BACK * 0.5;
		public static final int SHOOTER_TICKS_PER_ROTATION = -1870;
		public static final int SHOOTER_TICKS_TO_DOWN = (int)(MotorConstants.SHOOTER_TICKS_PER_ROTATION * 0.1);
		public static final int SHOOTER_TICKS_TO_SHOOTING = (int)(MotorConstants.SHOOTER_TICKS_PER_ROTATION * 0.2);

		public static final double SNAKE_HOLDING = 0.9;
		public static final double SNAKE_DUMPING = 0.7;

		public static final double CLUTCH_CLUTCHED = 0.7;
		public static final double CLUTCH_ENGAGED = 0.3;

		public static final double SHOOTER_STOPPER_UP = 1.0;
		public static final double SHOOTER_STOPPER_DOWN = -SHOOTER_STOPPER_UP;
	}
}
