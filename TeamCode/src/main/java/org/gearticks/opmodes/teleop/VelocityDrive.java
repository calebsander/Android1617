package org.gearticks.opmodes.teleop;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.AutonomousComponent.Transition;
import org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl;
import org.gearticks.autonomous.generic.component.AutonomousComponentTimer;
import org.gearticks.autonomous.generic.component.ParallelComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.opmodes.BaseOpMode;
import org.gearticks.opmodes.utility.Utils;

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
			if (this.stageTimer.seconds() > 0) return NEXT_STATE; //wait for ball to settle in snake before loading
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

			if (!ballInShooter && configuration.isShooterPassedEncoder()) return NEXT_STATE;
			else return null;
		}
	}
	private class LoadingComponent extends AutonomousComponentTimer {
		public Transition run() {
			final Transition superTransition = super.run();
			if (superTransition != null) return superTransition;

			configuration.clutch.setPosition(MotorConstants.CLUTCH_V2_CLUTCHED);
			configuration.snake.setPosition(MotorConstants.SNAKE_V2_DUMPING);
			configuration.advanceShooterToDownWithEncoder();

			if (this.stageTimer.seconds() > MotorConstants.SNAKE_V2_TIME_TO_MOVE) return NEXT_STATE;
			else return null;
		}
		public void tearDown() {
			super.tearDown();
			ballInShooter = true;
		}
	}
	private class SnakeReturnComponent extends AutonomousComponentTimer {
		public Transition run() {
			final Transition superTransition = super.run();
			if (superTransition != null) return superTransition;

			configuration.clutch.setPosition(MotorConstants.CLUTCH_V2_CLUTCHED);
			configuration.snake.setPosition(getDefaultSnakePosition());
			autoShooterUnlessBumper();

			if (this.stageTimer.seconds() > MotorConstants.SNAKE_V2_TIME_TO_MOVE*0.8) return NEXT_STATE;
			else return null;
		}
	}


	private enum ShooterState {
		//Going to down position
		ADVANCING_TO_DOWN,
		//Going to shooting position
		ADVANCING_TO_SHOOTING
	}
	//The current state of the shooter control
	private ShooterState shooterState;

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

			configuration.frontBeaconPresser.setPosition(this.position);
			if (gamepads[CALVIN].getB()) {
				if (this.stageTimer.seconds() > MotorConstants.PRESSER_V2_TIME_TO_MOVE) return SWITCH;
				else return null;
			}
			else return STOP;
		}
	}

	private class BumperDown extends AutonomousComponentTimer {
		@SuppressWarnings("ConstantConditions")
		@Override
		public void setup() {
			super.setup();
			configuration.frontBumper.setPower(MotorConstants.FRONT_BUMPER_DOWN);
		}

		@Override
		public Transition run() {
			final Transition superTransition = super.run();
			if (superTransition != null) return superTransition;

			if (this.stageTimer.seconds() > 0.5) return NEXT_STATE;
			else return null;
		}

		@Override
		public void tearDown() {
			super.tearDown();
			configuration.frontBumper.setPower(0.0);
		}
	}
	private class BumperUpWhenTrigger extends AutonomousComponentAbstractImpl {
		@Override
		public Transition run() {
			final Transition superTransition = super.run();
			if (superTransition != null) return superTransition;

			final double power;
			if (gamepads[CALVIN].getLeftTrigger()) power = MotorConstants.FRONT_BUMPER_UP;
			else power = 0.0;
			configuration.frontBumper.setPower(power);

			if (gamepads[CALVIN].dpadDown()) return NEXT_STATE;
			else return null;
		}
	}

	//The component running all of the state machines
	private ParallelComponent stateMachines;
	//Whether rollers are deployed
	private boolean rollersDeployed;

	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap, true);
		this.direction = new DriveDirection();
		this.stateMachines = new ParallelComponent();

		this.ballInShooter = false;
		final NetworkedStateMachine ballStateMachine = new NetworkedStateMachine("Ball state");
		final AutonomousComponent intaking = new IntakingComponent();
		final AutonomousComponent settling = new SettleInSnakeComponent();
		final AutonomousComponent holding = new HoldingComponent();
		final AutonomousComponent loading = new LoadingComponent();
		final AutonomousComponent snakeReturning = new SnakeReturnComponent();
		ballStateMachine.setInitialComponent(intaking);
		ballStateMachine.addConnection(intaking, NEXT_STATE, settling);
		ballStateMachine.addConnection(settling, NEXT_STATE, holding);
		ballStateMachine.addConnection(holding, NEXT_STATE, loading);
		ballStateMachine.addConnection(holding, MANUAL_SNAKE, intaking);
		ballStateMachine.addConnection(loading, NEXT_STATE, snakeReturning);
		ballStateMachine.addConnection(snakeReturning, NEXT_STATE, intaking);
		this.stateMachines.addComponent(ballStateMachine);

		this.shooterState = ShooterState.values()[0];

		final NetworkedStateMachine beaconStateMachine = new NetworkedStateMachine("Beacon state");
		final LinearStateMachine in = new LinearStateMachine();
		for(int pullIn = 0; pullIn < MotorConstants.PRESSER_V2_TIMES_PULL_IN; pullIn++) {
			in.addComponent(new PresserEngaged(MotorConstants.PRESSER_V2_FRONT_IN_STRAIN));
			in.addComponent(new Wait(0.05, "Wait to go out"));
			in.addComponent(new PresserEngaged(MotorConstants.PRESSER_V2_FRONT_IN));
			in.addComponent(new Wait(0.05, "Wait to go out"));
		}
		final AutonomousComponent stayIn = new PresserEngaged(MotorConstants.PRESSER_V2_FRONT_IN);
		final AutonomousComponent out = new PresserEngaged(MotorConstants.PRESSER_V2_FRONT_OUT);
		beaconStateMachine.setInitialComponent(stayIn);
		beaconStateMachine.addConnection(stayIn, NEXT_STATE, out);
		beaconStateMachine.addConnection(out, SWITCH, in);
		beaconStateMachine.addConnection(stayIn, SWITCH, out);
		beaconStateMachine.addConnection(stayIn, STOP, stayIn);
		beaconStateMachine.addConnection(out, STOP, in);
		beaconStateMachine.addConnection(in, NEXT_STATE, stayIn);
		//this.stateMachines.addComponent(beaconStateMachine);

		final NetworkedStateMachine bumperStateMachine = new NetworkedStateMachine("Front bumper state");
		final AutonomousComponent bumperUp = new BumperUpWhenTrigger();
		final AutonomousComponent bumperDown = new BumperDown();
		bumperStateMachine.setInitialComponent(bumperUp);
		bumperStateMachine.addConnection(bumperUp, NEXT_STATE, bumperDown);
		bumperStateMachine.addConnection(bumperDown, NEXT_STATE, bumperUp);
		this.stateMachines.addComponent(bumperStateMachine);

		this.rollersDeployed = true;
	}
	protected void matchStart() {
		this.configuration.rollersDown();
	}
	@SuppressWarnings("ConstantConditions")
	protected void loopAfterStart() {
		final int driveGamepad;
		double yScaleFactor;
		double sScaleFactor;
		if (this.gamepads[CALVIN].leftStickAtRest() && this.gamepads[CALVIN].rightStickAtRest()) {
			driveGamepad = JACK;
			yScaleFactor = 0.8;
			sScaleFactor = Math.max(0.4, Math.abs(this.gamepads[driveGamepad].getLeftY() * yScaleFactor)); //if just turning, turn slower for greater accuracy
		}
		else {
			driveGamepad = CALVIN;
			yScaleFactor = 1.0;
			sScaleFactor = Math.max(0.5, Math.abs(this.gamepads[driveGamepad].getLeftY() * yScaleFactor)); //if just turning, turn slower for greater accuracy
		}

		final boolean slowMode = this.gamepads[CALVIN].getLeftBumper();
		final double maxPower;
		if (slowMode) {
			maxPower = 0.4;
			sScaleFactor = Math.max(0.15, Math.abs(this.gamepads[driveGamepad].getLeftY() * maxPower));
		}
		else maxPower = 1.0;
		final double accelLimit;
		if (slowMode) accelLimit = 0.075;
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

		this.stateMachines.run();

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
		if (this.isManualSnakeOn()) {
			this.ballInShooter = true;
			return MotorConstants.SNAKE_V2_DUMPING;
		}
		else return MotorConstants.SNAKE_V2_HOLDING;
	}
}