package org.gearticks.opmodes.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.gearticks.autonomous.component.AutonomousComponentVelocityBase;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class AutoShootParticle extends AutonomousComponentVelocityBase {
	private boolean shooterWasDown;
	private ElapsedTime stageTimer;

	public AutoShootParticle(VelocityConfiguration configuration, String id) {
		super(configuration, id);
		this.shooterWasDown = false;
	}

	@Override
	public void initialize() {
		super.initialize();
		this.stageTimer = new ElapsedTime();
	}

	@Override
	public void setup(int inputPort) {
		super.setup(inputPort);
	}

	private enum Stage {
		MOVE_SHOOTER_TO_SENSOR,
		MOVE_SHOOTER_DOWN,
		WAIT,
		RELEASE_SECOND_BALL,
	}
	private Stage stage;

	private void nextStage() {
		this.stage = Stage.values()[this.stage.ordinal() + 1];
		this.stageTimer.reset();
	}

	@Override
	public int run() {
		int transition = 0;
		super.run();
		final boolean shooterIsDown = getConfiguration().isShooterDown();
		switch(stage){
			case MOVE_SHOOTER_TO_SENSOR:
				this.getConfiguration().shooter.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
				this.getConfiguration().shooter.setPower(VelocityConfiguration.MotorConstants.SHOOTER_BACK_SLOW * 2);
				if (shooterIsDown){
					this.stage.
				}
				break;
			case MOVE_SHOOTER_DOWN:

				break;
			case WAIT:

				break;
			case RELEASE_SECOND_BALL:

				break;
			default:
				break;
		}
		if (shooterIsDown) {
			//set shooter was down to true if sensor triggers, but never again to false
			this.shooterWasDown = shooterIsDown;
		}
		if (this.shooterWasDown) {
			if (shooterIsDown) {
				//move shooter slowly below the sensor
				this.getConfiguration().shooter.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
				this.getConfiguration().shooter.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
				this.getConfiguration().shooter.setPower(VelocityConfiguration.MotorConstants.SHOOTER_BACK_SLOW);
			}
			else {
				//hold shooter at position lower than sensor
				if(this.getConfiguration().shooter.encoderValue() > -30){
					this.getConfiguration().shooter.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
					this.getConfiguration().shooter.setPower(VelocityConfiguration.MotorConstants.SHOOTER_BACK_SLOW);
				}
				else {
					transition = 1;
				}
			}
		}
		else {
			//move the shooter to the sensor
			this.getConfiguration().shooter.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
			this.getConfiguration().shooter.setPower(VelocityConfiguration.MotorConstants.SHOOTER_BACK_SLOW * 2);
		}

		return transition;
	}

	@Override
	public void tearDown() {
		super.tearDown();
	}
}
