package org.gearticks.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.opmodes.BaseOpMode;

@Autonomous
public class CantonBeaconAutonomous extends BaseOpMode {
	private VelocityConfiguration configuration;
	private DriveDirection direction;
	private ElapsedTime stageTimer;

	private enum Stage {
		SHOOT,
		STOPPED
	}
	private Stage stage;

	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap);
		this.direction = new DriveDirection();
		this.stage = Stage.values()[0];
	}
	protected void loopBeforeStart() {
		this.telemetry.addData("Heading", this.configuration.imu.getHeading());
	}
	protected void matchStart() {
		this.stageTimer = new ElapsedTime();
	}
	protected void loopAfterStart() {
		switch (this.stage) {
			case SHOOT:
				this.configuration.shooter.setPower(MotorConstants.SHOOTER_BACK);
				if (this.stageTimer.seconds() > 2.0) {
					this.configuration.shooter.stop();
					this.nextStage();
				}
				break;
			case STOPPED:
				this.configuration.stopMotion();
		}
		if (this.stage != Stage.STOPPED) this.configuration.move(this.direction);
	}
	private void nextStage() {
		this.stage = Stage.values()[this.stage.ordinal() + 1];
		this.stageTimer.reset();
	}
}