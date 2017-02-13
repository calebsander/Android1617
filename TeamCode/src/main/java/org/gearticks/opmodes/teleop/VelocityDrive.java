package org.gearticks.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl;
import org.gearticks.autonomous.generic.component.AutonomousComponentTimer;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.LeftPressBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.RightPressBeaconServo;
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
		public int run() {
			configuration.clutch.setPosition(MotorConstants.CLUTCH_V2_ENGAGED);
			configuration.snake.setPosition(getDefaultSnakePosition());
			autoShooterUnlessBumper();
			final boolean snakeReturnedToHolding = this.stageTimer.seconds() > MotorConstants.SNAKE_V2_TIME_TO_MOVE;
			if (snakeReturnedToHolding && configuration.ballInSnake()) return NEXT_STATE;
			else return NOT_DONE;
		}
	}
	private class HoldingComponent extends AutonomousComponentAbstractImpl {
		public int run() {
			configuration.clutch.setPosition(MotorConstants.CLUTCH_V2_CLUTCHED);
			configuration.snake.setPosition(getDefaultSnakePosition());
			autoShooterUnlessBumper();
			if (!ballInShooter && configuration.isShooterDown()) return NEXT_STATE;
			else return NOT_DONE;
		}
	}
	private class LoadingComponent extends AutonomousComponentTimer {
		public int run() {
			configuration.clutch.setPosition(MotorConstants.CLUTCH_V2_CLUTCHED);
			configuration.snake.setPosition(MotorConstants.SNAKE_V2_DUMPING);
			configuration.advanceShooterToDown();
			if (this.stageTimer.seconds() > MotorConstants.SNAKE_V2_TIME_TO_MOVE) return NEXT_STATE;
			else return NOT_DONE;
		}
		public void tearDown() {
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

	private class WaitForBeaconChange extends AutonomousComponentAbstractImpl {
		public int run() {
			if (gamepads[CALVIN].getB() && !gamepads[CALVIN].getLast().getB()) return NEXT_STATE;
			else return NOT_DONE;
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
		final AutonomousComponent holding = new HoldingComponent();
		final AutonomousComponent loading = new LoadingComponent();
		this.ballStateMachine.setInitialComponent(intaking);
		this.ballStateMachine.addConnection(intaking, NEXT_STATE, holding);
		this.ballStateMachine.addConnection(holding, NEXT_STATE, loading);
		this.ballStateMachine.addConnection(loading, NEXT_STATE, intaking);

		this.shooterState = ShooterState.values()[0];

		this.beaconStateMachine = new NetworkedStateMachine("Beacon state");
		final AutonomousComponent waitBeforeLeft = new WaitForBeaconChange();
		final AutonomousComponent engageLeft = new LeftPressBeaconServo(configuration, "Beacon left");
		final AutonomousComponent waitBeforeRight = new WaitForBeaconChange();
		final AutonomousComponent engageRight = new RightPressBeaconServo(configuration, "Beacon right");
		final AutonomousComponent waitBeforeDisengage = new WaitForBeaconChange();
		final AutonomousComponent disengage = new DisengageBeaconServo(configuration, "Beacon neutral");
		this.beaconStateMachine.setInitialComponent(waitBeforeLeft);
		this.beaconStateMachine.addConnection(waitBeforeLeft, NEXT_STATE, engageLeft);
		this.beaconStateMachine.addConnection(engageLeft, NEXT_STATE, waitBeforeRight);
		this.beaconStateMachine.addConnection(waitBeforeRight, NEXT_STATE, engageRight);
		this.beaconStateMachine.addConnection(engageRight, NEXT_STATE, waitBeforeDisengage);
		this.beaconStateMachine.addConnection(waitBeforeDisengage, NEXT_STATE, disengage);
		this.beaconStateMachine.addConnection(disengage, NEXT_STATE, waitBeforeLeft);

		this.rollersDeployed = true;
		this.configuration.rollersDown();
	}
	protected void loopAfterStart() {
		int driveGamepad;
		double yScaleFactor;
		double sScaleFactor;
		if ((Math.abs(this.gamepads[CALVIN].getLeftY()) < 0.1 && Math.abs(this.gamepads[CALVIN].getRightX())  < 0.1)
				&& (Math.abs(this.gamepads[JACK].getLeftY()) > 0.1 || Math.abs(this.gamepads[JACK].getRightX()) > 0.1)){
			driveGamepad = JACK;
			yScaleFactor = 0.7;
			sScaleFactor = Math.max(0.3, Math.abs(this.gamepads[driveGamepad].getLeftY() * yScaleFactor)); //if just turning, turn slower for greater accuracy
		}
		else {
			driveGamepad = CALVIN;
			yScaleFactor = 1.0;
			sScaleFactor = Math.max(0.5, Math.abs(this.gamepads[driveGamepad].getLeftY() * yScaleFactor)); //if just turning, turn slower for greater accuracy
		}


		final double scaleFactor;
		final boolean slowMode = this.gamepads[CALVIN].getLeftBumper();
		if (slowMode) scaleFactor = 0.4; //limit max speed
		else scaleFactor = 1.0;
		final double accelLimit;
		if (slowMode) accelLimit = 0.03;
		else accelLimit = MotorWrapper.NO_ACCEL_LIMIT;

		this.direction.drive(0.0, scaleStick(this.gamepads[driveGamepad].getLeftY()) * yScaleFactor * scaleFactor);
		this.direction.turn(scaleStick(this.gamepads[driveGamepad].getRightX()) * sScaleFactor * scaleFactor);

		this.configuration.move(this.direction, accelLimit);

		this.telemetry.addData("Controller", driveGamepad);
		this.telemetry.addData("Calvin's left Y", this.gamepads[CALVIN].getLeftY());
		this.telemetry.addData("Forward power", this.direction.getY());

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
	private static double scaleStick(double stick) {
		return stick * stick * stick;
	}
	private double getDefaultSnakePosition() {
		if (this.gamepads[JACK].getB()) return MotorConstants.SNAKE_V2_DUMPING;
		else return MotorConstants.SNAKE_V2_HOLDING;
	}
}