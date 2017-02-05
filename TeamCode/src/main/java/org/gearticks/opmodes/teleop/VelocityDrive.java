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
	private enum ShooterState {
		//Going to down position
		ADVANCING_TO_DOWN,
		//Going to shooting position
		ADVANCING_TO_SHOOTING
	}
	//The current state of the shooter control
	private ShooterState shooterState;
	//Keeps track of the amount of time elapsed since the switch to the current BallState
	private ElapsedTime ballStateTimer;
	//Whether we think there is a ball in the shooter
	private boolean ballInShooter;
	//Whether rollers are deployed
	private boolean rollersDeployed;

	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap, true);
		this.direction = new DriveDirection();
		this.ballState = BallState.values()[0];
		this.shooterState = ShooterState.values()[0];
		this.ballStateTimer = new ElapsedTime();
		this.ballInShooter = false;
		this.rollersDeployed = true;
	}
	protected void loopAfterStart() {
		final boolean slowMode = this.gamepads[CALVIN].getLeftBumper();
		final int driveGamepad = CALVIN;
		final double yScaleFactor = 1.0;
		final double sScaleFactor = Math.max(0.5, Math.abs(this.gamepads[driveGamepad].getLeftY() * yScaleFactor)); //if just turning, turn slower for greater accuracy
		final double scaleFactor;
		if (slowMode) scaleFactor = 0.4; //limit max speed
		else scaleFactor = 1.0;
		this.direction.drive(0.0, scaleStick(this.gamepads[driveGamepad].getLeftY()) * yScaleFactor * scaleFactor);
		this.direction.turn(scaleStick(this.gamepads[driveGamepad].getRightX()) * sScaleFactor * scaleFactor);
		final double accelLimit;
		if (slowMode) accelLimit = 0.03;
		else accelLimit = MotorWrapper.NO_ACCEL_LIMIT;
		this.configuration.move(this.direction, accelLimit);

		final double intakePower;
		if (this.gamepads[CALVIN].getRightBumper() || this.gamepads[JACK].getRightBumper()) {
			intakePower = MotorConstants.INTAKE_IN;
		}
		else if (this.gamepads[CALVIN].getRightTrigger() || this.gamepads[JACK].getRightTrigger()) {
			intakePower = MotorConstants.INTAKE_OUT;
		}
		else {
			intakePower = MotorWrapper.STOPPED; //leave intake off by default to save battery
		}
		this.configuration.intake.setPower(intakePower);

		double snakePosition  = MotorConstants.SNAKE_V2_HOLDING,
		       clutchPosition = MotorConstants.CLUTCH_V2_CLUTCHED;
		switch (this.ballState) {
			case INTAKING:
				clutchPosition = MotorConstants.CLUTCH_V2_ENGAGED; //this is the only state when it is safe to load a ball into the snake
				this.autoShooterUnlessBumper();
				//Wait for snake to return to holding because it triggers the sensors on the way down
				final boolean snakeReturnedToHolding = this.ballStateTimer.seconds() > MotorConstants.SNAKE_V2_TIME_TO_MOVE;
				if (snakeReturnedToHolding && this.configuration.ballInSnake()) this.nextBallState();
				break;
			case HOLDING:
				this.autoShooterUnlessBumper();
				if (this.configuration.isShooterDown() && !this.ballInShooter) this.nextBallState();
				break;
			case LOADING:
				this.configuration.advanceShooterToDown(); //hold shooter down
				snakePosition = MotorConstants.SNAKE_V2_DUMPING; //this is the only state when the snake should be up
				if (this.ballStateTimer.seconds() > MotorConstants.SNAKE_V2_TIME_TO_MOVE) {
					this.ballInShooter = true;
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
		if (this.gamepads[JACK].getY()) capBallPower = MotorConstants.CAP_BALL_UP;
		else if (this.gamepads[JACK].getA()) capBallPower = MotorConstants.CAP_BALL_DOWN;
		else capBallPower = MotorWrapper.STOPPED;
		final double capBallScaling;
		if (this.gamepads[JACK].getBack()) capBallScaling = MotorConstants.CAP_BALL_SLOW_SCALE;
		else capBallScaling = 1.0;
		this.configuration.capBall.setPower(capBallPower * capBallScaling);

		if (this.gamepads[CALVIN].getX() && !this.gamepads[CALVIN].getLast().getX()) {
			this.rollersDeployed = !this.rollersDeployed;
			if (this.rollersDeployed) this.configuration.rollersDown();
			else this.configuration.rollersUp();
		}
	}

	//Move shooter to down unless bumper is pressed, in which case, fire ball
	private void autoShooterUnlessBumper() {
		switch (this.shooterState) {
			case ADVANCING_TO_DOWN:
				this.configuration.advanceShooterToDown();
				//Require ball to be newly loaded (unless overridden)
				final boolean shotRequested = this.gamepads[JACK].getLeftBumper() && (this.ballInShooter || this.gamepads[JACK].getX());
				if (this.configuration.isShooterDown() && shotRequested) {
					this.shooterState = ShooterState.ADVANCING_TO_SHOOTING;
				}
				break;
			case ADVANCING_TO_SHOOTING:
				this.configuration.advanceShooterToShooting();
				if (this.configuration.isShooterAtTarget()) {
					this.ballInShooter = false;
					this.configuration.resetAutoShooter();
					this.shooterState = ShooterState.ADVANCING_TO_DOWN;
				}
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