package org.gearticks.opmodes.autonomous;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.vuforia.HINT;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CloseableFrame;
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
	private BlockingQueue<CloseableFrame> frameQueue;
	private CloseableFrame beaconFrame;
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
		SELECT_SIDE,
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
		Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
		this.beaconImages = vuforia.loadTrackablesFromAsset("FTC_2016-17");
		for (Map.Entry<String, Integer> imageId : IMAGE_IDS.entrySet()) this.beaconImages.get(imageId.getValue()).setName(imageId.getKey());
		this.wheelsListener = (VuforiaTrackableDefaultListener)this.beaconImages.get(IMAGE_IDS.get("Wheels")).getListener();
		this.legosListener = (VuforiaTrackableDefaultListener)this.beaconImages.get(IMAGE_IDS.get("Legos")).getListener();
		vuforia.setFrameQueueCapacity(1);
		this.frameQueue = vuforia.getFrameQueue();
		this.beaconFrame = null;
		this.stage = Stage.DRIVE_OFF_WALL; //Stage.values()[0];
	}
	protected void loopBeforeStart() {
		this.configuration.safeShooterStopper(MotorConstants.SHOOTER_STOPPER_UP);
		this.configuration.shooter.setRunMode(RunMode.STOP_AND_RESET_ENCODER);
		this.telemetry.addData("Heading", this.configuration.imu.getHeading());
	}
	protected void matchStart() {
		this.telemetry.clear();
		//this.configuration.setLEDs(false);
		//this.configuration.startReadingColor();
		this.beaconImages.activate();
		this.direction.stopDrive();
		this.configuration.resetEncoder();
		this.configuration.resetAutoShooter();
		this.stageTimer = new ElapsedTime();
	}
	protected void loopAfterStart() {
		switch (this.stage) {
			case MOVE_SHOOTER_DOWN_FIRST:
				this.configuration.advanceShooterToDown();
				if (this.configuration.isShooterDown()) {
					this.configuration.resetAutoShooter();
					this.nextStage();
				}
				break;
			case SHOOT_FIRST_BALL:
				this.configuration.advanceShooterToDown();
				if (this.configuration.isShooterDown()) {
					this.configuration.resetAutoShooter();
					this.nextStage();
				}
				break;
			case RELEASE_SECOND_BALL:
				this.configuration.particleBlocker.setPosition(MotorConstants.PARTICLE_BLOCKER_AWAY);
				if (this.stageTimer.seconds() > 1.0) this.nextStage();
				break;
			case SHOOT_SECOND_BALL:
				this.configuration.advanceShooterToDown();
				if (this.configuration.isShooterDown()) this.nextStage();
				break;
			case DRIVE_OFF_WALL:
				this.direction.drive(0.0, 0.7);
				this.direction.gyroCorrect(0.0, 1.0, this.configuration.imu.getRelativeYaw(), 0.05, 0.1);
				if (this.configuration.encoderPositive() > 1700) {
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
					final float lateralDistance = -(translation.get(1) - 50);
					final float normalDistance = -translation.get(2);
					if (normalDistance < 100F) {
						this.direction.stopDrive();
						this.nextStage();
					}
					else {
						double turnPower = lateralDistance * 0.001;
						if (Math.abs(turnPower) > 0.05) turnPower = Math.signum(turnPower) * 0.05;
						this.direction.turn(turnPower);
						if (normalDistance < 400F && this.beaconFrame == null) {
							try { this.beaconFrame = this.frameQueue.take(); }
							catch (InterruptedException e) {}
						}
					}
					this.telemetry.addData("Target", translation);
				}
				break;
			case SELECT_SIDE:
				final CloseableFrame frame = this.beaconFrame;
				final long images = frame.getNumImages();
				Bitmap bitmap;
				for (int i = 0; i < images; i++) {
					final Image image = frame.getImage(i);
					if (image.getFormat() == PIXEL_FORMAT.RGB565) {
						bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Config.RGB_565);
						bitmap.copyPixelsFromBuffer(image.getPixels());
						break;
					}
				}
				frame.close();
				this.nextStage();
				break;
			case STOPPED:
				this.configuration.stopMotion();
		}
		if (this.stage != Stage.STOPPED) this.configuration.move(this.direction);
		this.telemetry.addData("Stage", this.stage);
		this.telemetry.addData("Heading", this.configuration.imu.getHeading());
	}
	protected void matchEnd() {
		this.configuration.teardown();
	}

	private void nextStage() {
		this.stage = Stage.values()[this.stage.ordinal() + 1];
		this.stageTimer.reset();
	}
}