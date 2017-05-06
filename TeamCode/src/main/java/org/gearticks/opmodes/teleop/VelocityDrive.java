package org.gearticks.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.AutonomousComponent.DefaultTransition;
import org.gearticks.GamepadWrapper;
import org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.autonomous.generic.component.AutonomousComponentTimer;
import org.gearticks.autonomous.generic.component.ParallelComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.opmodes.teleop.components.TeleopCapBall;
import org.gearticks.opmodes.teleop.components.TeleopDrive;
import org.gearticks.opmodes.teleop.components.TeleopIntake;
import org.gearticks.opmodes.teleop.components.TeleopRollers;
import org.gearticks.opmodes.teleop.components.TeleopShooterStopper;

@TeleOp
public class VelocityDrive extends VelocityBaseOpMode {
	public static final int CALVIN = 0, JACK = 1;
	public VelocityConfiguration configuration;


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

	//The component running all of the state machines
	private ParallelComponent teleopComponent;

	protected AutonomousComponent<?> getComponent(OpModeContext opModeContext) {
		this.configuration = new VelocityConfiguration(this.hardwareMap, true);
		this.teleopComponent = new ParallelComponent();

		this.ballInShooter = false;
		final NetworkedStateMachine ballStateMachine = new NetworkedStateMachine("Ball state");
		final IntakingComponent intaking = new IntakingComponent();
		final SettleInSnakeComponent settling = new SettleInSnakeComponent();
		final HoldingComponent holding = new HoldingComponent();
		final LoadingComponent loading = new LoadingComponent();
		final SnakeReturnComponent snakeReturning = new SnakeReturnComponent();
		ballStateMachine.setInitialComponent(intaking);
		ballStateMachine.addConnection(intaking, DefaultTransition.DEFAULT, settling);
		ballStateMachine.addConnection(settling, DefaultTransition.DEFAULT, holding);
		ballStateMachine.addConnection(holding, HoldingTransition.SHOOTER_DOWN, loading);
		ballStateMachine.addConnection(holding, HoldingTransition.MANUAL_SNAKE, intaking);
		ballStateMachine.addConnection(loading, DefaultTransition.DEFAULT, snakeReturning);
		ballStateMachine.addConnection(snakeReturning, DefaultTransition.DEFAULT, intaking);
		this.teleopComponent.addComponent(ballStateMachine);

		this.shooterState = ShooterState.values()[0];

		final NetworkedStateMachine beaconStateMachine = new NetworkedStateMachine("Beacon state");
		final LinearStateMachine in = new LinearStateMachine();
		for(int pullIn = 0; pullIn < MotorConstants.PRESSER_V2_TIMES_PULL_IN; pullIn++) {
			in.addComponent(new PresserEngaged(MotorConstants.PRESSER_V2_FRONT_IN_STRAIN));
			in.addComponent(new Wait(0.05, "Wait to go out"));
			in.addComponent(new PresserEngaged(MotorConstants.PRESSER_V2_FRONT_IN));
			in.addComponent(new Wait(0.05, "Wait to go out"));
		}
		final PresserEngaged stayIn = new PresserEngaged(MotorConstants.PRESSER_V2_FRONT_IN);
		final PresserEngaged out = new PresserEngaged(MotorConstants.PRESSER_V2_FRONT_OUT);
		beaconStateMachine.setInitialComponent(stayIn);
		beaconStateMachine.addConnection(stayIn, PresserTransition.SWITCH, out);
		beaconStateMachine.addConnection(stayIn, PresserTransition.STOP, stayIn);
		beaconStateMachine.addConnection(out, PresserTransition.SWITCH, in);
		beaconStateMachine.addConnection(out, PresserTransition.STOP, in);
		beaconStateMachine.addConnection(in, DefaultTransition.DEFAULT, stayIn);
		//this.teleopComponent.addComponent(beaconStateMachine);

		final NetworkedStateMachine bumperStateMachine = new NetworkedStateMachine("Front bumper state");
		final BumperUpWhenTrigger bumperUp = new BumperUpWhenTrigger();
		final BumperDown bumperDown = new BumperDown();
		bumperStateMachine.setInitialComponent(bumperUp);
		bumperStateMachine.addConnection(bumperUp, DefaultTransition.DEFAULT, bumperDown);
		bumperStateMachine.addConnection(bumperDown, DefaultTransition.DEFAULT, bumperUp);
		this.teleopComponent.addComponent(bumperStateMachine);

		this.teleopComponent.addComponent(new TeleopCapBall(opModeContext));
		this.teleopComponent.addComponent(new TeleopDrive(opModeContext));
		this.teleopComponent.addComponent(new TeleopIntake(opModeContext));
		this.teleopComponent.addComponent(new TeleopRollers(opModeContext));
		this.teleopComponent.addComponent(new TeleopShooterStopper(opModeContext));

		return this.teleopComponent;
	}
	protected void matchStart() {
		this.configuration.rollersDown();
		this.teleopComponent.onMatchStart();
		this.teleopComponent.setup();
	}
	@SuppressWarnings("ConstantConditions")

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
	public static double scaleStick(double stick) {
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
	public boolean isV2() {
		return true;
	}
}
