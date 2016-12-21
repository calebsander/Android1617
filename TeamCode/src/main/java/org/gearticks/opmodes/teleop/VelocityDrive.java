package org.gearticks.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp
public class VelocityDrive extends BaseOpMode {
	private static final int CALVIN = 0, JACK = 1;
	private static final double TIME_TO_MOVE_SNAKE = 0.4; //seconds for snake to switch positions
	private VelocityConfiguration configuration;
	private DriveDirection direction;
	private boolean clutchClutched;

	private enum BallState {
		INTAKING,
		HOLDING,
		LOADING
	}
	private BallState ballState;
	private ElapsedTime ballStateTimer;
	private boolean shotBall;

	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap);
		this.direction = new DriveDirection();
		this.clutchClutched = false;
		this.ballState = BallState.values()[0];
		this.ballStateTimer = new ElapsedTime();
		this.shotBall = true;
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

		final double manualIntakePower;
		if (this.gamepads[CALVIN].getRightBumper() || this.gamepads[JACK].getRightBumper()) {
			manualIntakePower = MotorConstants.INTAKE_IN;
		}
		else if (this.gamepads[CALVIN].getRightTrigger() || this.gamepads[JACK].getRightTrigger()) {
			manualIntakePower = MotorConstants.INTAKE_OUT;
		}
		else {
			manualIntakePower = MotorWrapper.STOPPED;
		}

		double autoIntakePower = MotorWrapper.STOPPED;
		switch (this.ballState) {
			case INTAKING:
				autoIntakePower = MotorConstants.INTAKE_IN;
				this.autoShooterUnlessBumper();
				this.configuration.snake.setPosition(MotorConstants.SNAKE_HOLDING);
				if (this.configuration.ballInSnake() && this.ballStateTimer.seconds() > TIME_TO_MOVE_SNAKE) this.nextBallState();
				break;
			case HOLDING:
				this.autoShooterUnlessBumper();
				this.configuration.snake.setPosition(MotorConstants.SNAKE_HOLDING);
				if (this.configuration.isShooterDown() && this.shotBall) this.nextBallState();
				break;
			case LOADING:
				this.configuration.teleopAdvanceShooterToDown();
				this.configuration.snake.setPosition(MotorConstants.SNAKE_DUMPING);
				if (this.ballStateTimer.seconds() > TIME_TO_MOVE_SNAKE) {
					this.shotBall = false;
					this.nextBallState();
				}
		}
		if (manualIntakePower == MotorWrapper.STOPPED) this.configuration.intake.setPower(autoIntakePower); //not controlling it manually
		else this.configuration.intake.setPower(manualIntakePower);

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
	}

	private void autoShooterUnlessBumper() {
		if (this.gamepads[JACK].getLeftBumper()) {
			this.configuration.resetAutoShooter();
			this.configuration.shootFast();
			this.shotBall = true;
		}
		else {
			this.configuration.teleopAdvanceShooterToDown();
		}
	}
	private void nextBallState() {
		this.ballStateTimer.reset();
		final BallState[] ballStates = BallState.values();
		this.ballState = ballStates[(this.ballState.ordinal() + 1) % ballStates.length];
	}
	private static double scaleStick(double stick) {
		return stick * stick * stick;
	}
}