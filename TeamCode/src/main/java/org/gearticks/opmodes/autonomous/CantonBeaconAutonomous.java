package org.gearticks.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.vuforia.HINT;
import com.vuforia.Vuforia;
import java.util.HashMap;
import java.util.Map;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.gearticks.VuforiaKey;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.opmodes.BaseOpMode;

@Autonomous
public class CantonBeaconAutonomous extends BaseOpMode {
	private VelocityConfiguration configuration;
	private DriveDirection direction;
	private static final Map<String, Integer> IMAGE_IDS = new HashMap<>();
	static {
		IMAGE_IDS.put("Wheels", 0);
		IMAGE_IDS.put("Tools", 1);
		IMAGE_IDS.put("Legos", 2);
		IMAGE_IDS.put("Gears", 3);
	}
	private VuforiaTrackables beaconImages;
	private VuforiaTrackableDefaultListener wheelsListener, legosListener;
	private ElapsedTime stageTimer;

	private enum Stage {
		MOVE_SHOOTER_DOWN_FIRST,
		SHOOT_FIRST_BALL,
		RELEASE_SECOND_BALL,
		SHOOT_SECOND_BALL,
		DRIVE_OFF_WALL,
		TURN_TO_FAR_TARGET,
		DRIVE_IN_FRONT_OF_NEAR_TARGET,
		FACE_NEAR_TARGET,
		VUFORIA_TO_BEACON,
		STOPPED
	}
	private Stage stage;

	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap);
		this.configuration.imu.eulerRequest.startReading();
		this.direction = new DriveDirection();
		final VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(com.qualcomm.ftcrobotcontroller.R.id.cameraMonitorViewId);
		parameters.vuforiaLicenseKey = VuforiaKey.KEY;
		parameters.cameraDirection = CameraDirection.FRONT;
		final VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(parameters);
		Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 2);
		this.beaconImages = vuforia.loadTrackablesFromAsset("FTC_2016-17");
		for (Map.Entry<String, Integer> imageId : IMAGE_IDS.entrySet()) this.beaconImages.get(imageId.getValue()).setName(imageId.getKey());
		this.wheelsListener = (VuforiaTrackableDefaultListener)this.beaconImages.get(IMAGE_IDS.get("Wheels")).getListener();
		this.legosListener = (VuforiaTrackableDefaultListener)this.beaconImages.get(IMAGE_IDS.get("Legos")).getListener();
		this.stage = Stage.values()[0];
	}
	protected void loopBeforeStart() {
		this.configuration.safeShooterStopper(MotorConstants.SHOOTER_STOPPER_UP);
		this.configuration.shooter.setRunMode(RunMode.STOP_AND_RESET_ENCODER);
		this.telemetry.addData("Heading", this.configuration.imu.getHeading());
	}
	protected void matchStart() {
		this.telemetry.clear();
		this.beaconImages.activate();
		this.stageTimer = new ElapsedTime();
		this.direction.stopDrive();
		this.configuration.resetAutoShooter();
	}
	protected void loopAfterStart() {
		switch (this.stage) {
			case MOVE_SHOOTER_DOWN_FIRST:
				this.configuration.advanceShooterToDown();
				if (this.configuration.isShooterDown()) this.nextStage();
				break;
			case SHOOT_FIRST_BALL:
				this.configuration.advanceShooterToDown();
				if (this.configuration.isShooterAtSensor()) this.nextStage();
				break;
			case RELEASE_SECOND_BALL:
				this.configuration.particleBlocker.setPosition(MotorConstants.PARTICLE_BLOCKER_AWAY);
				if (this.stageTimer.seconds() > 1.0) this.nextStage();
				break;
			case SHOOT_SECOND_BALL:
				this.configuration.advanceShooterToDown();
				if (this.configuration.isShooterAtSensor()) this.stage = Stage.STOPPED; //this.nextStage();
				break;
			case DRIVE_OFF_WALL:
				this.direction.drive(0.0, 0.7);
				this.direction.gyroCorrect(0.0, 1.0, this.configuration.imu.getRelativeYaw(), 0.05, 0.1);
				if (this.configuration.encoderPositive() > 2000) {
					this.direction.stopDrive();
					this.nextStage();
				}
				break;
			case TURN_TO_FAR_TARGET:
				if (this.direction.gyroCorrect(40.0, 1.0, this.configuration.imu.getRelativeYaw(), 0.08, 0.1) > 10) {
					this.direction.stopDrive();
					this.configuration.resetEncoder();
					this.nextStage();
				}
				break;
			case DRIVE_IN_FRONT_OF_NEAR_TARGET:
				this.direction.drive(0.0, 0.7);
				this.direction.gyroCorrect(0.0, 1.0, this.configuration.imu.getRelativeYaw(), 0.05, 0.1);
				if (this.configuration.encoderPositive() > 2500) {
					this.direction.stopDrive();
					this.nextStage();
				}
				break;
			case FACE_NEAR_TARGET:
				if (this.direction.gyroCorrect(90.0, 1.0, this.configuration.imu.getRelativeYaw(), 0.08, 0.1) > 10) {
					this.direction.stopDrive();
					this.nextStage();
				}
				break;
			case VUFORIA_TO_BEACON:
				final OpenGLMatrix pose = this.wheelsListener.getPose();
				if (pose == null) {
					this.direction.drive(0.0, 0.05);
					this.direction.turn(0.0);
				}
				else {
					this.direction.drive(0.0, 0.10);
					final VectorF translation = pose.getTranslation();
					final float lateralDistance = -translation.get(1);
					final float normalDistance = -translation.get(2);
					if (normalDistance < 100F) {
						this.direction.stopDrive();
						this.nextStage();
					}
					else {
						double turnPower = lateralDistance / 500.0;
						if (Math.abs(turnPower) > 0.05) turnPower = Math.signum(turnPower) * 0.05;
						this.direction.turn(turnPower);
					}
					this.telemetry.addData("Target", translation);
				}
				break;
			case STOPPED:
				this.configuration.stopMotion();
		}
		if (this.stage != Stage.STOPPED) this.configuration.move(this.direction);
		this.telemetry.addData("Stage", this.stage);
		this.telemetry.addData("Sensor", this.configuration.isShooterAtSensor());
	}
	protected void matchEnd() {
		this.configuration.teardown();
	}

	private void nextStage() {
		this.stage = Stage.values()[this.stage.ordinal() + 1];
		this.stageTimer.reset();
	}
}