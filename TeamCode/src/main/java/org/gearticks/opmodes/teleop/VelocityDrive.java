package org.gearticks.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.gearticks.autonomous.generic.component.AutonomousComponent.DefaultTransition;
import org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl;
import org.gearticks.autonomous.generic.component.AutonomousComponentTimer;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.generic.Wait;
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

	//Whether we think there is a ball in the shooter
	private boolean ballInShooter;
	private class IntakingComponent extends AutonomousComponentTimer<DefaultTransition> {
		public IntakingComponent() {
			super(DefaultTransition.class);
		}

		@Override
		public DefaultTransition run() {
			final DefaultTransition superTransition = super.run();
			if (superTransition != null) return superTransition;

			configuration.clutch.setPosition(MotorConstants.CLUTCH_V2_ENGAGED);
			configuration.snake.setPosition(getDefaultSnakePosition());
			autoShooterUnlessBumper();
			if (configuration.ballInSnake()) return DefaultTransition.DEFAULT;
			else return null;
		}
	}
	private class SettleInSnakeComponent extends AutonomousComponentTimer<DefaultTransition> {
		public SettleInSnakeComponent() {
			super(DefaultTransition.class);
		}

		@Override
		public DefaultTransition run() {
			final DefaultTransition superTransition = super.run();
			if (superTransition != null) return superTransition;

			configuration.clutch.setPosition(MotorConstants.CLUTCH_V2_CLUTCHED);
			configuration.snake.setPosition(getDefaultSnakePosition());
			autoShooterUnlessBumper();
			if (this.stageTimer.seconds() > 0) return DefaultTransition.DEFAULT; //wait for ball to settle in snake before loading
			else return null;
		}
	}
	private enum HoldingTransition {
		SHOOTER_DOWN,
		MANUAL_SNAKE
	}
	private class HoldingComponent extends AutonomousComponentAbstractImpl<HoldingTransition> {
		public HoldingComponent() {
			super(HoldingTransition.class);
		}

		@Override
		public HoldingTransition run() {
			final HoldingTransition superTransition = super.run();
			if (superTransition != null) return superTransition;

			configuration.clutch.setPosition(MotorConstants.CLUTCH_V2_CLUTCHED);
			configuration.snake.setPosition(getDefaultSnakePosition());
			autoShooterUnlessBumper();

			if (isManualSnakeOn()) return HoldingTransition.MANUAL_SNAKE;

			if (!ballInShooter && configuration.isShooterPassedEncoder()) return HoldingTransition.SHOOTER_DOWN;
			else return null;
		}
	}
	private class LoadingComponent extends AutonomousComponentTimer<DefaultTransition> {
		public LoadingComponent() {
			super(DefaultTransition.class);
		}

		@Override
		public DefaultTransition run() {
			final DefaultTransition superTransition = super.run();
			if (superTransition != null) return superTransition;

			configuration.clutch.setPosition(MotorConstants.CLUTCH_V2_CLUTCHED);
			configuration.snake.setPosition(MotorConstants.SNAKE_V2_DUMPING);
			configuration.advanceShooterToDownWithEncoder();

			if (this.stageTimer.seconds() > MotorConstants.SNAKE_V2_TIME_TO_MOVE) return DefaultTransition.DEFAULT;
			else return null;
		}
		@Override
		public void tearDown() {
			super.tearDown();
			ballInShooter = true;
		}
	}
	private class SnakeReturnComponent extends AutonomousComponentTimer<DefaultTransition> {
		public SnakeReturnComponent() {
			super(DefaultTransition.class);
		}

		@Override
		public DefaultTransition run() {
			final DefaultTransition superTransition = super.run();
			if (superTransition != null) return superTransition;

			configuration.clutch.setPosition(MotorConstants.CLUTCH_V2_CLUTCHED);
			configuration.snake.setPosition(getDefaultSnakePosition());
			autoShooterUnlessBumper();

			if (this.stageTimer.seconds() > MotorConstants.SNAKE_V2_TIME_TO_MOVE*0.8) return DefaultTransition.DEFAULT;
			else return null;
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

	private enum PresserTransition {
		SWITCH,
		STOP
	}
	private class PresserEngaged extends AutonomousComponentTimer<PresserTransition> {
		private final double position;

		public PresserEngaged(double position) {
			super(PresserTransition.class);
			this.position = position;
		}

		@Override
		public PresserTransition run() {
			final PresserTransition superTransition = super.run();
			if (superTransition != null) return superTransition;

			configuration.frontBeaconPresser.setPosition(this.position);
			if (gamepads[CALVIN].getB()) {
				if (this.stageTimer.seconds() > MotorConstants.PRESSER_V2_TIME_TO_MOVE) return PresserTransition.SWITCH;
				else return null;
			}
			else return PresserTransition.STOP;
		}
	}
	//The state machine controlling manually pressing the beacon
	private NetworkedStateMachine beaconStateMachine;

	private class BumperDown extends AutonomousComponentTimer<DefaultTransition> {
		public BumperDown() {
			super(DefaultTransition.class);
		}

		@SuppressWarnings("ConstantConditions")
		@Override
		public void setup() {
			super.setup();
			configuration.frontBumper.setPower(MotorConstants.FRONT_BUMPER_DOWN);
		}

		@Override
		public DefaultTransition run() {
			final DefaultTransition superTransition = super.run();
			if (superTransition != null) return superTransition;

			if (this.stageTimer.seconds() > 0.5) return DefaultTransition.DEFAULT;
			else return null;
		}

		@Override
		public void tearDown() {
			super.tearDown();
			configuration.frontBumper.setPower(0.0);
		}
	}
	private class BumperUpWhenTrigger extends AutonomousComponentAbstractImpl<DefaultTransition> {
		public BumperUpWhenTrigger() {
			super(DefaultTransition.class);
		}

		@Override
		public DefaultTransition run() {
			final DefaultTransition superTransition = super.run();
			if (superTransition != null) return superTransition;

			final double power;
			if (gamepads[CALVIN].getLeftTrigger()) power = MotorConstants.FRONT_BUMPER_UP;
			else power = 0.0;
			configuration.frontBumper.setPower(power);

			if (gamepads[CALVIN].dpadDown()) return DefaultTransition.DEFAULT;
			else return null;
		}
	}
	private NetworkedStateMachine bumperStateMachine;

	//Whether rollers are deployed
	private boolean rollersDeployed;

	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap, true);
		this.direction = new DriveDirection();

		this.ballInShooter = false;
		this.ballStateMachine = new NetworkedStateMachine("Ball state");
		final IntakingComponent intaking = new IntakingComponent();
		final SettleInSnakeComponent settling = new SettleInSnakeComponent();
		final HoldingComponent holding = new HoldingComponent();
		final LoadingComponent loading = new LoadingComponent();
		final SnakeReturnComponent snakeReturning = new SnakeReturnComponent();
		this.ballStateMachine.setInitialComponent(intaking);
		this.ballStateMachine.addConnection(intaking, DefaultTransition.DEFAULT, settling);
		this.ballStateMachine.addConnection(settling, DefaultTransition.DEFAULT, holding);
		this.ballStateMachine.addConnection(holding, HoldingTransition.SHOOTER_DOWN, loading);
		this.ballStateMachine.addConnection(holding, HoldingTransition.MANUAL_SNAKE, intaking);
		this.ballStateMachine.addConnection(loading, DefaultTransition.DEFAULT, snakeReturning);
		this.ballStateMachine.addConnection(snakeReturning, DefaultTransition.DEFAULT, intaking);

		this.shooterState = ShooterState.values()[0];

		this.beaconStateMachine = new NetworkedStateMachine("Beacon state");
		//final AutonomousComponent neutral = new PresserNeutral();
		final LinearStateMachine in = new LinearStateMachine();
		for(int pullIn = 0; pullIn < MotorConstants.PRESSER_V2_TIMES_PULL_IN; pullIn++) {
			in.addComponent(new PresserEngaged(MotorConstants.PRESSER_V2_FRONT_IN_STRAIN));
			in.addComponent(new Wait(0.05, "Wait to go out"));
			in.addComponent(new PresserEngaged(MotorConstants.PRESSER_V2_FRONT_IN));
			in.addComponent(new Wait(0.05, "Wait to go out"));
		}
		final PresserEngaged stayIn = new PresserEngaged(MotorConstants.PRESSER_V2_FRONT_IN);
		final PresserEngaged out = new PresserEngaged(MotorConstants.PRESSER_V2_FRONT_OUT);
		this.beaconStateMachine.setInitialComponent(stayIn);
		this.beaconStateMachine.addConnection(stayIn, PresserTransition.SWITCH, out);
		this.beaconStateMachine.addConnection(stayIn, PresserTransition.STOP, stayIn);
		this.beaconStateMachine.addConnection(out, PresserTransition.SWITCH, in);
		this.beaconStateMachine.addConnection(out, PresserTransition.STOP, in);
		this.beaconStateMachine.addConnection(in, DefaultTransition.DEFAULT, stayIn);

		this.bumperStateMachine = new NetworkedStateMachine("Front bumper state");
		final BumperUpWhenTrigger bumperUp = new BumperUpWhenTrigger();
		final BumperDown bumperDown = new BumperDown();
		this.bumperStateMachine.setInitialComponent(bumperUp);
		this.bumperStateMachine.addConnection(bumperUp, DefaultTransition.DEFAULT, bumperDown);
		this.bumperStateMachine.addConnection(bumperDown, DefaultTransition.DEFAULT, bumperUp);

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
//			yScaleFactor = -yScaleFactor;
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

		this.ballStateMachine.run();
		//this.beaconStateMachine.run();
		this.bumperStateMachine.run();

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
		if (this.isManualSnakeOn()) return MotorConstants.SNAKE_V2_DUMPING;
		else return MotorConstants.SNAKE_V2_HOLDING;
	}
}