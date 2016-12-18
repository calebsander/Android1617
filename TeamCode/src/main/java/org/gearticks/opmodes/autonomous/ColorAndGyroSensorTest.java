package org.gearticks.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.gearticks.AutonomousDatalogger;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.joystickoptions.IncrementOption;
import org.gearticks.opmodes.BaseOpMode;

@Autonomous(name = "Color and Gyro Sensor Test Autonomous")
//@Disabled
public class ColorAndGyroSensorTest extends BaseOpMode {
	protected VelocityConfiguration configuration;
	private DriveDirection direction;
	private ElapsedTime stageTimer;

	private enum Stage {
		FORWARD_1,
		WAIT_2,
		BACKWARD_3,
		WAIT_4,
		FORWARD_5,
		WAIT_6,
		BACKWARD_7,
		STOPPED
	}
	private Stage stage;
	private AutonomousDatalogger datalogger;

	private void nextStage() {
		this.stageTimer.reset();
		this.stage = Stage.values()[this.stage.ordinal() + 1];
	}
	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap);
		this.configuration.imu.eulerRequest.startReading();
		this.direction = new DriveDirection();
		this.datalogger = new AutonomousDatalogger();
		this.stage = Stage.values()[0];
	}
	protected void matchStart() {
		this.direction.stopDrive();
		this.configuration.resetEncoder();
		this.configuration.resetAutoShooter();
		this.stageTimer = new ElapsedTime();
		this.configuration.imu.resetHeading();
		this.telemetry.clear();
	}
	protected void loopAfterStart() {
		switch (this.stage) {
			case FORWARD_1:
				this.direction.drive(0.0, 0.7);
				this.direction.gyroCorrect(0.0, 1.0, this.configuration.imu.getRelativeYaw(), 0.05, 0.1);
				if (this.configuration.encoderPositive() > 1700) {
					this.direction.stopDrive();
					this.nextStage();
				}
				break;
			case WAIT_2:
				this.direction.stopDrive();
				this.configuration.resetEncoder();
				if (this.stageTimer.seconds() > 0.5) this.nextStage();
				break;
			case BACKWARD_3:
				this.direction.drive(0.0, -0.7);
				this.direction.gyroCorrect(0.0, 1.0, this.configuration.imu.getRelativeYaw(), 0.05, 0.1);
				if (this.configuration.encoderPositive() > 1700) {
					this.direction.stopDrive();
					this.nextStage();
				}
			case WAIT_4:
				this.direction.stopDrive();
				this.configuration.resetEncoder();
				if (this.stageTimer.seconds() > 0.5) this.nextStage();
				break;
			case FORWARD_5:
				this.direction.drive(0.0, 0.7);
				this.direction.gyroCorrect(0.0, 1.0, this.configuration.imu.getRelativeYaw(), 0.05, 0.1);
				if (this.configuration.encoderPositive() > 1700) {
					this.direction.stopDrive();
					this.nextStage();
				}
				break;
			case WAIT_6:
				this.direction.stopDrive();
				this.configuration.resetEncoder();
				if (this.stageTimer.seconds() > 0.5) this.nextStage();
				break;
			case BACKWARD_7:
				this.direction.drive(0.0, -0.7);
				this.direction.gyroCorrect(0.0, 1.0, this.configuration.imu.getRelativeYaw(), 0.05, 0.1);
				if (this.configuration.encoderPositive() > 1700) {
					this.direction.stopDrive();
					this.nextStage();
				}
				break;
			case STOPPED:
				this.configuration.stopMotion();
		}
		if (this.matchTime.seconds() > 30.0) this.stage = Stage.STOPPED;
		if (this.stage != Stage.STOPPED) this.configuration.move(this.direction, 0.06);
		this.telemetry.addData("Stage", this.stage);
		this.datalogger.writeLine(this.matchTime.seconds());
	}
	protected void matchEnd() {
		this.datalogger.close();
	}
	private void setStage(Stage newStage) {
		this.stage = newStage;
		this.stageTimer.reset();
	}

}