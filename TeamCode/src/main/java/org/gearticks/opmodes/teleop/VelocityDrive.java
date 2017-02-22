package org.gearticks.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.AutonomousComponent.Transition;
import org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl;
import org.gearticks.autonomous.generic.component.AutonomousComponentTimer;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.opmodes.BaseOpMode;

import static org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl.NEXT_STATE;

@TeleOp
public class VelocityDrive extends BaseOpMode {
	private static final int CALVIN = 0, JACK = 1;
	private VelocityConfiguration configuration;
	private DriveDirection direction;

	//Whether we think there is a ball in the shooter
	private boolean ballInShooter;
	private class IntakingComponent extends AutonomousComponentTimer {
		public Transition run() {
			final Transition superTransition = super.run();
			if (superTransition != null) return superTransition;

			configuration.clutch.setPosition(MotorConstants.CLUTCH_V2_ENGAGED);
			configuration.snake.setPosition(getDefaultSnakePosition());
			autoShooterUnlessBumper();
			if (configuration.ballInSnake()) return NEXT_STATE;
			else return null;
		}
	}
	private class SettleInSnakeComponent extends AutonomousComponentTimer {
		public Transition run() {
			final Transition superTransition = super.run();
			if (superTransition != null) return superTransition;

			configuration.clutch.setPosition(MotorConstants.CLUTCH_V2_CLUTCHED);
			configuration.snake.setPosition(getDefaultSnakePosition());
			autoShooterUnlessBumper();
			if (this.stageTimer.seconds() > 0.1) return NEXT_STATE; //wait for ball to settle in snake before loading
			else return null;
		}
	}
	private static final Transition MANUAL_SNAKE = new Transition("Snake triggered manually");
	private class HoldingComponent extends AutonomousComponentAbstractImpl {
		public Transition run() {
			final Transition superTransition = super.run();
			if (superTransition != null) return superTransition;

			configuration.clutch.setPosition(MotorConstants.CLUTCH_V2_CLUTCHED);
			configuration.snake.setPosition(getDefaultSnakePosition());
			autoShooterUnlessBumper();

			if (isManualSnakeOn()) return MANUAL_SNAKE;

			if (!ballInShooter && configuration.isShooterDown()) return NEXT_STATE;
			else return null;
		}
	}
	private class LoadingComponent extends AutonomousComponentTimer {
		public Transition run() {
			final Transition superTransition = super.run();
			if (superTransition != null) return superTransition;

			configuration.clutch.setPosition(MotorConstants.CLUTCH_V2_CLUTCHED);
			configuration.snake.setPosition(MotorConstants.SNAKE_V2_DUMPING);
			configuration.advanceShooterToDown();
			if (this.stageTimer.seconds() > MotorConstants.SNAKE_V2_TIME_TO_MOVE) return NEXT_STATE;
			else return null;
		}
		public void tearDown() {
			super.tearDown();
			ballInShooter = true;
		}
	}
	//The state machine switching between intaking, holding, and loading
	private NetworkedStateMachine ballStateMachine;


	private enum ShooterState {
		//Going to down position
		ADVANCING_TO_DOWN,
		//Going to shooting position
		ADVANCING_TO_SHOOTING
	}
	//The current state of the shooter control
	private ShooterState shooterState;

	private class PresserNeutral extends AutonomousComponentAbstractImpl {
		public Transition run() {
			final Transition superTransition = super.run();
			if (superTransition != null) return superTransition;

			configuration.beaconPresser.setPosition(MotorConstants.PRESSER_V2_NEUTRAL);
			if (gamepads[CALVIN].getB()) return NEXT_STATE;
			else return null;
		}
	}
	private static final Transition
		SWITCH = new Transition("Switch sides"),
		STOP = new Transition("Stop pressing");
	private class PresserEngaged extends AutonomousComponentTimer {
		private final double position;

		public PresserEngaged(double position) {
			this.position = position;
		}

		public Transition run() {
			final Transition superTransition = super.run();
			if (superTransition != null) return superTransition;

			configuration.beaconPresser.setPosition(this.position);
			if (gamepads[CALVIN].getB()) {
				if (this.stageTimer.seconds() > MotorConstants.PRESSER_V2_TIME_TO_MOVE) return SWITCH;
				else return null;
			}
			else return STOP;
		}
	}
	//The state machine controlling manually pressing the beacon
	private NetworkedStateMachine beaconStateMachine;

	//Whether rollers are deployed
	private boolean rollersDeployed;

	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap, true);
		this.direction = new DriveDirection();

		this.ballInShooter = false;
		this.ballStateMachine = new NetworkedStateMachine("Ball state");
		final AutonomousComponent intaking = new IntakingComponent();
		final AutonomousComponent settling = new SettleInSnakeComponent();
		final AutonomousComponent holding = new HoldingComponent();
		final AutonomousComponent loading = new LoadingComponent();
		this.ballStateMachine.setInitialComponent(intaking);
		this.ballStateMachine.addConnection(intaking, NEXT_STATE, settling);
		this.ballStateMachine.addConnection(settling, NEXT_STATE, holding);
		this.ballStateMachine.addConnection(holding, NEXT_STATE, loading);
		this.ballStateMachine.addConnection(holding, MANUAL_SNAKE, intaking);
		this.ballStateMachine.addConnection(loading, NEXT_STATE, intaking);

		this.shooterState = ShooterState.values()[0];

		this.beaconStateMachine = new NetworkedStateMachine("Beacon state");
		//final AutonomousComponent neutral = new PresserNeutral();
		final AutonomousComponent in = new PresserEngaged(MotorConstants.PRESSER_V2_LEFT_IN);
		final AutonomousComponent out = new PresserEngaged(MotorConstants.PRESSER_V2_LEFT_OUT);
		this.beaconStateMachine.setInitialComponent(in);
		this.beaconStateMachine.addConnection(in, NEXT_STATE, out);
		this.beaconStateMachine.addConnection(out, SWITCH, in);
		this.beaconStateMachine.addConnection(in, SWITCH, out);
		this.beaconStateMachine.addConnection(in, STOP, in);
		this.beaconStateMachine.addConnection(out, STOP, in);

		this.rollersDeployed = true;
	}
	protected void matchStart() {
		this.configuration.rollersDown();
	}
	@SuppressWarnings("ConstantConditions")
	protected void loopAfterStart() {
		int driveGamepad;
		double yScaleFactor;
		double sScaleFactor;
		if (this.gamepads[CALVIN].leftStickAtRest() && this.gamepads[CALVIN].rightStickAtRest()) {
			driveGamepad = JACK;
			yScaleFactor = 0.7;
			sScaleFactor = Math.max(0.3, Math.abs(this.gamepads[driveGamepad].getLeftY() * yScaleFactor)); //if just turning, turn slower for greater accuracy
		}
		else {
			driveGamepad = CALVIN;
			yScaleFactor = 1.0;
			sScaleFactor = Math.max(0.5, Math.abs(this.gamepads[driveGamepad].getLeftY() * yScaleFactor)); //if just turning, turn slower for greater accuracy
		}

		final boolean slowMode = this.gamepads[CALVIN].getLeftBumper();
		final double maxPower;
		if (slowMode) maxPower = 0.4;
		else maxPower = 1.0;
		final double accelLimit;
		if (slowMode) accelLimit = 0.03;
		else accelLimit = MotorWrapper.NO_ACCEL_LIMIT;

		this.direction.drive(0.0, scaleStick(this.gamepads[driveGamepad].getLeftY()) * yScaleFactor);
		this.direction.turn(scaleStick(this.gamepads[driveGamepad].getRightX()) * sScaleFactor);

		this.configuration.drive.calculatePowers(this.direction);
		this.configuration.drive.scaleMotorsDown(maxPower);
		this.configuration.drive.accelLimit(accelLimit);
		this.configuration.drive.commitPowers();

		this.telemetry.addData("Controller", driveGamepad);
		this.telemetry.addData("Shooter down", this.configuration.isShooterDown());

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

		this.ballStateMachine.run();
		this.beaconStateMachine.run();

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
		final DcMotor.RunMode capBallMode;
		if (this.gamepads[JACK].getY()) {
			if(this.configuration.isCapBallUp()) {
				capBallPower = MotorConstants.CAP_BALL_SUPER_SLOW_UP;
				capBallMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
			}
			else {
				capBallPower = MotorConstants.CAP_BALL_UP;
				capBallMode = DcMotor.RunMode.RUN_USING_ENCODER;
			}
		}
		else if (this.gamepads[JACK].getA()) {
			capBallPower = MotorConstants.CAP_BALL_DOWN;
			capBallMode = DcMotor.RunMode.RUN_USING_ENCODER;
		}
		else {
			capBallPower = MotorWrapper.STOPPED;
			capBallMode = DcMotor.RunMode.RUN_USING_ENCODER;
		}
		final double capBallScaling;
		if (this.gamepads[JACK].getBack()) capBallScaling = MotorConstants.CAP_BALL_SLOW_SCALE;
		else capBallScaling = 1.0;
		this.configuration.capBall.setPower(capBallPower * capBallScaling);
		this.configuration.capBall.setRunMode(capBallMode);

		if (this.gamepads[CALVIN].getX() && !this.gamepads[CALVIN].getLast().getX()) {
			this.rollersDeployed = !this.rollersDeployed;
			if (this.rollersDeployed) this.configuration.rollersDown();
			else this.configuration.rollersUp();
		}
		if (this.gamepads[CALVIN].getX()){

		}
	}

	//Move shooter to down unless bumper is pressed, in which case, fire ball
	private void autoShooterUnlessBumper() {
		this.telemetry.addData("shooterEncoder", this.configuration.shooter.encoderValue());
		this.telemetry.addData("passedEncoder", this.configuration.isShooterPassedEncoder());
		switch (this.shooterState) {
			case ADVANCING_TO_DOWN:
				this.configuration.advanceShooterToDownWithEncoder();
				//Require ball to be newly loaded (unless overridden)
				final boolean shotRequested = this.gamepads[JACK].getLeftBumper() && (this.ballInShooter || this.gamepads[JACK].getX());
				if (this.configuration.isShooterDown() && shotRequested) {
					this.shooterState = ShooterState.ADVANCING_TO_SHOOTING;
				}
				break;
			case ADVANCING_TO_SHOOTING:
				this.configuration.shootFast();
				if (this.configuration.hasShot()) {
					this.ballInShooter = false;
					this.configuration.resetAutoShooter();
					this.shooterState = ShooterState.ADVANCING_TO_DOWN;
				}
		}
	}
	private static double scaleStick(double stick) {
		return stick * stick * stick;
	}
	private boolean isManualSnakeOn() {
		return this.gamepads[JACK].getB();
	}
	private double getDefaultSnakePosition() {
		if (this.isManualSnakeOn()) return MotorConstants.SNAKE_V2_DUMPING;
		else return MotorConstants.SNAKE_V2_HOLDING;
	}
}