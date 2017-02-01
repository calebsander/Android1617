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

	private enum BallState {
		//No ball in snake, so trying to put one into it
		INTAKING,
		//Ball in snake; prevent another from entering snake
		HOLDING,
		//Ball is ready to be loaded into shooter
		LOADING
	}
	//The current state of the ball loader
	private BallState ballState;
	//Keeps track of the amount of time elapsed since the switch to the current BallState
	private ElapsedTime ballStateTimer;
	//Whether ball has been shot since it was last loaded
	private boolean shotBall;

	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap);
		this.direction = new DriveDirection();
		this.ballState = BallState.values()[0];
		this.ballStateTimer = new ElapsedTime();
		this.shotBall = true;
	}
	protected void loopAfterStart() {
		final int driveGamepad = CALVIN;
		final double yScaleFactor = 1.0;
		final double sScaleFactor = Math.max(0.5, Math.abs(this.gamepads[driveGamepad].getLeftY() * yScaleFactor)); //if just turning, turn slower for greater accuracy
		if (this.gamepads[driveGamepad].leftStickAtRest()) {
			this.direction.drive(0.0, 0.0);
			this.direction.turn(scaleStick(this.gamepads[driveGamepad].getRightX()) * sScaleFactor);
		}
		else { //if banana-turning, turn faster
			this.direction.drive(0.0, scaleStick(this.gamepads[driveGamepad].getLeftY()) * yScaleFactor);
			this.direction.turn(scaleStick(this.gamepads[driveGamepad].getRightX()) * sScaleFactor);
		}
		this.configuration.move(this.direction, MotorWrapper.NO_ACCEL_LIMIT);

		final double intakePower;
		if (this.gamepads[CALVIN].getRightBumper() /*|| this.gamepads[JACK].getRightBumper()*/) {
			intakePower = MotorConstants.INTAKE_IN;
		}
		else if (this.gamepads[CALVIN].getRightTrigger() /*|| this.gamepads[JACK].getRightTrigger()*/) {
			intakePower = MotorConstants.INTAKE_OUT;
		}
		else {
			intakePower = MotorWrapper.STOPPED; //leave intake off by default to save battery
		}
		this.configuration.intake.setPower(intakePower);

		double snakePosition  = MotorConstants.SNAKE_HOLDING,
		       clutchPosition = MotorConstants.CLUTCH_CLUTCHED;
		switch (this.ballState) {
			case INTAKING:
				clutchPosition = MotorConstants.CLUTCH_ENGAGED; //this is the only state when it is safe to load a ball into the snake
				this.autoShooterUnlessBumper();
				//Wait for snake to return to holding because it triggers the sensors on the way down
				if (this.ballStateTimer.seconds() > TIME_TO_MOVE_SNAKE && this.configuration.ballInSnake()) this.nextBallState();
				break;
			case HOLDING:
				this.autoShooterUnlessBumper();
				if (this.configuration.isShooterDown() && this.shotBall) this.nextBallState();
				break;
			case LOADING:
				this.configuration.teleopAdvanceShooterToDown(); //hold shooter down
				snakePosition = MotorConstants.SNAKE_DUMPING; //this is the only state when the snake should be up
				if (this.ballStateTimer.seconds() > TIME_TO_MOVE_SNAKE) {
					this.shotBall = false;
					this.nextBallState();
				}
		}
		this.configuration.snake.setPosition(snakePosition);
		this.configuration.clutch.setPosition(clutchPosition);

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

		final double capBallPower;
		if (this.gamepads[JACK].getRightBumper()) {
			capBallPower = MotorConstants.CAPBALL_UP;
		}
		else if (this.gamepads[JACK].getRightTrigger()) {
			capBallPower = MotorConstants.CAPBALL_DOWN;
		}
		else {
			capBallPower = MotorWrapper.STOPPED;
		}
		this.configuration.capball.setPower(capBallPower);
	}

	//Move shooter to down unless bumper is pressed, in which case, fire ball
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
	//Advance to next BallState (wrapping around) and reset state timer
	private void nextBallState() {
		this.ballStateTimer.reset();
		final BallState[] ballStates = BallState.values();
		this.ballState = ballStates[(this.ballState.ordinal() + 1) % ballStates.length];
	}
	private static double scaleStick(double stick) {
		return stick * stick * stick;
	}
}