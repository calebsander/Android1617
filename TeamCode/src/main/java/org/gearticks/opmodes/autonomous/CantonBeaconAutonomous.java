package org.gearticks.opmodes.autonomous;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.vuforia.HINT;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;
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
import org.gearticks.vuforia.VuforiaKey;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.joystickoptions.IncrementOption;
import org.gearticks.joystickoptions.ValuesJoystickOption;
import org.gearticks.opmodes.BaseOpMode;

@SuppressWarnings("deprecation")
@Autonomous
@Disabled
public class CantonBeaconAutonomous extends BaseOpMode {
	private VelocityConfiguration configuration;
	private DriveDirection direction;
	private IncrementOption delayOption;
	private ElapsedTime angleCorrectionTimer;
	private boolean allianceColorIsBlue;
	private double buttonAngle;
	private static final Map<String, Integer> IMAGE_IDS = new HashMap<>();
	static {
		IMAGE_IDS.put("Wheels", 0);
		IMAGE_IDS.put("Tools", 1);
		IMAGE_IDS.put("Legos", 2);
		IMAGE_IDS.put("Gears", 3);
	}
	private VuforiaTrackables beaconImages;
	private VuforiaTrackableDefaultListener firstTargetListener;
	private BlockingQueue<CloseableFrame> frameQueue;
	private CloseableFrame beaconFrame;
	private ElapsedTime stageTimer;
	private static final int CROP_WIDTH = 600, CROP_HEIGHT = 350;
	private static final int CROP_LEFT_X = 275;
	private static final double SCALE_FACTOR = 0.4;
	private static final int SCALED_WIDTH = (int)(CROP_WIDTH * SCALE_FACTOR), SCALED_HEIGHT = (int)(CROP_HEIGHT * SCALE_FACTOR);
	private static final int HALF_WIDTH = SCALED_WIDTH / 2;

	private enum Stage {
		DELAY,
		SHOOT_FIRST_BALL,
		MOVE_SHOOTER_DOWN,
		DUMP_SECOND_BALL,
		RESET_SNAKE,
		SHOOT_SECOND_BALL,
		DRIVE_OFF_WALL,
		WAIT_BEFORE_FIRST_TURN,
		TURN_TO_FAR_TARGET,
		DRIVE_IN_FRONT_OF_NEAR_TARGET,
		WAIT_BEFORE_SECOND_TURN,
		FACE_NEAR_TARGET,
		VUFORIA_TO_PICTURE,
		FACE_PICTURE,
		VUFORIA_TO_BEACON,
		SELECT_SIDE,
		TURN_TO_PRESS_BUTTON,
		PUSH_BUTTON,
		FACE_SQUARE,
		BACK_UP,
		TURN_TO_KNOCK_BALL,
		TURN_BACK_AFTER_BALL,
		SLOW_TO_CENTER,
		STOPPED
	}
	private Stage stage;
	private enum Routine {
		SHOOTER_ONLY(true, false),
		BOTH(true, true),
		DRIVE_ONLY(false, true);

		public final boolean runShooter, runDrive;

		Routine(boolean runShooter, boolean runDrive) {
			this.runShooter = runShooter;
			this.runDrive = runDrive;
		}
	}
	private ValuesJoystickOption<Routine> routineOption;

	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap);
		this.configuration.imu.eulerRequest.startReading();
		this.direction = new DriveDirection();
		final VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(/*R.id.cameraMonitorViewId*/);
		parameters.vuforiaLicenseKey = VuforiaKey.KEY;
		parameters.cameraDirection = CameraDirection.FRONT;
		final VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(parameters);
		Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 2);
		Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
		this.beaconImages = vuforia.loadTrackablesFromAsset("FTC_2016-17");
		vuforia.setFrameQueueCapacity(1);
		this.frameQueue = vuforia.getFrameQueue();
		this.beaconFrame = null;
		this.delayOption = new IncrementOption("Delay", 10.0);
		this.addOption(this.delayOption);
		this.routineOption = new ValuesJoystickOption<>("Routine", Routine.values());
		this.routineOption.selectOption(Routine.BOTH);
		this.addOption(this.routineOption);
		this.angleCorrectionTimer = new ElapsedTime();
		this.stage = Stage.values()[0];
	}
	protected void loopBeforeStart() {
		this.configuration.safeShooterStopper(MotorConstants.SHOOTER_STOPPER_UP); //make sure shooter stopper is at top
		this.telemetry.addData("Heading", String.valueOf(this.configuration.imu.getHeading()));
		this.configuration.advanceShooterToDown(); //make sure shooter begins in down position, ready to shoot
	}
	protected void matchStart() {
		this.allianceColorIsBlue = AllianceOption.allianceOption.getRawSelectedOption() == AllianceOption.BLUE;
		final String firstTargetName;
		if (this.allianceColorIsBlue) firstTargetName = "Wheels";
		else firstTargetName = "Gears";
		this.firstTargetListener = (VuforiaTrackableDefaultListener)this.beaconImages.get(IMAGE_IDS.get(firstTargetName)).getListener();
		this.telemetry.clear();
		this.beaconImages.activate();
		this.direction.stopDrive();
		this.configuration.resetEncoder();
		this.configuration.resetAutoShooter();
		this.stageTimer = new ElapsedTime();
		this.configuration.imu.resetHeading();
	}
	protected void loopAfterStart() {
		final double angleMultiplier;
		if (this.allianceColorIsBlue) angleMultiplier = 1.0; //angles were calculated for blue side
		else angleMultiplier = -1.0; //invert all angles for red side
		final OpenGLMatrix firstTargetPose = this.firstTargetListener.getPose();
		switch (this.stage) {
			case DELAY:
				if (this.stageTimer.seconds() > this.delayOption.getValue()) {
					if (this.routineOption.getRawSelectedOption().runShooter) this.nextStage();
					else this.setStage(Stage.DRIVE_OFF_WALL);
				}
				break;
			case SHOOT_FIRST_BALL:
				this.configuration.shooter.setRunMode(RunMode.RUN_TO_POSITION);
				this.configuration.shooter.setTarget(MotorConstants.SHOOTER_TICKS_TO_SHOOTING);
				this.configuration.shooter.setPower(MotorConstants.SHOOTER_BACK);
				if (!this.configuration.shooter.notAtTarget()) this.nextStage();
				break;
			case MOVE_SHOOTER_DOWN:
				this.configuration.advanceShooterToDown();
				if (this.configuration.isShooterDown()) {
					this.configuration.resetAutoShooter();
					this.nextStage();
				}
				break;
			case DUMP_SECOND_BALL:
				this.configuration.snake.setPosition(MotorConstants.SNAKE_DUMPING);
				if (this.stageTimer.seconds() > 0.7) this.nextStage();
				break;
			case RESET_SNAKE:
				this.configuration.snake.setPosition(MotorConstants.SNAKE_HOLDING);
				if (this.stageTimer.seconds() > 0.5) this.nextStage();
				break;
			case SHOOT_SECOND_BALL:
				this.configuration.shooter.setRunMode(RunMode.RUN_TO_POSITION);
				this.configuration.shooter.setTarget(MotorConstants.SHOOTER_TICKS_TO_SHOOTING);
				this.configuration.shooter.setPower(MotorConstants.SHOOTER_BACK);
				if (!this.configuration.shooter.notAtTarget()) {
					if (this.routineOption.getRawSelectedOption().runDrive) this.nextStage();
					else this.setStage(Stage.STOPPED);
				}
				break;
			case DRIVE_OFF_WALL:
				this.direction.drive(0.0, 0.7);
				this.direction.gyroCorrect(0.0, 1.0, this.configuration.imu.getRelativeYaw(), 0.05, 0.1);
				if (this.configuration.encoderPositive() > 1700) {
					this.direction.stopDrive();
					this.nextStage();
				}
				break;
			case WAIT_BEFORE_FIRST_TURN:
				this.direction.stopDrive();
				if (this.stageTimer.seconds() > 0.3) this.nextStage();
				break;
			case TURN_TO_FAR_TARGET:
				if (this.direction.gyroCorrect(40.0 * angleMultiplier, 1.0, this.configuration.imu.getRelativeYaw(), 0.08, 0.1) > 10) {
					this.direction.stopDrive();
					this.configuration.resetEncoder();
					this.nextStage();
				}
				break;
			case DRIVE_IN_FRONT_OF_NEAR_TARGET:
				this.direction.drive(0.0, 0.7);
				this.direction.gyroCorrect(40.0 * angleMultiplier, 1.0, this.configuration.imu.getRelativeYaw(), 0.05, 0.1);
				if (this.configuration.encoderPositive() > 2900) {
					this.direction.stopDrive();
					this.nextStage();
				}
				break;
			case WAIT_BEFORE_SECOND_TURN:
				this.direction.stopDrive();
				if (this.stageTimer.seconds() > 0.3) this.nextStage();
				break;
			case FACE_NEAR_TARGET:
				if (this.direction.gyroCorrect(90.0 * angleMultiplier, 1.0, this.configuration.imu.getRelativeYaw(), 0.08, 0.1) > 10) {
					this.direction.stopDrive();
					this.nextStage();
				}
				break;
			case VUFORIA_TO_PICTURE:
				vuforiaIn(firstTargetPose, 500F);
				break;
			case FACE_PICTURE:
				if (firstTargetPose == null) {
					if (this.direction.gyroCorrect(90.0 * angleMultiplier, 1.0, this.configuration.imu.getRelativeYaw(), 0.08, 0.1) < 10) {
						this.stageTimer.reset();
					}
				}
				else {
					if ((this.angleCorrectionTimer.seconds() % 0.3) < 0.1) { //for .1 second out of each .3 seconds, correct using Vuforia
						if (!this.vuforiaTurn(firstTargetPose.getTranslation(), 0.05)) this.stageTimer.reset();
					}
					else this.direction.stopDrive(); //then pause for .2 seconds
				}
				if (this.stageTimer.seconds() > 0.4) { //if we have been on target for .4 seconds in a row, take the picture
					try { this.beaconFrame = this.frameQueue.take(); }
					catch (InterruptedException e) {}
					if (this.beaconFrame != null) this.nextStage(); //wait until getting a frame
				}
				break;
			case VUFORIA_TO_BEACON:
				vuforiaIn(firstTargetPose, 175F);
				break;
			case SELECT_SIDE:
				final CloseableFrame frame = this.beaconFrame;
				final long images = frame.getNumImages();
				Bitmap bitmap = null;
				for (int i = 0; i < images; i++) {
					final Image image = frame.getImage(i);
					if (image.getFormat() == PIXEL_FORMAT.RGB565) {
						bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Config.RGB_565);
						bitmap.copyPixelsFromBuffer(image.getPixels());
						break;
					}
				}
				frame.close();
				bitmap = Bitmap.createBitmap(bitmap, CROP_LEFT_X, 0, CROP_WIDTH, CROP_HEIGHT); //crop to beacon
				bitmap = Bitmap.createScaledBitmap(bitmap, SCALED_WIDTH, SCALED_HEIGHT, false); //scale down to decrease processing time
				/*final File outputDir = new File(Environment.getExternalStorageDirectory() + "/Pictures");
				outputDir.mkdirs();
				try {
					final File outputFile = new File(outputDir.getPath() + "/abc.png");
					final FileOutputStream outputStream = new FileOutputStream(outputFile);
					bitmap.compress(CompressFormat.PNG, 90, outputStream);
					outputStream.close();
				}
				catch (IOException e) {
					throw new RuntimeException(e.getMessage());
				}*/
				int leftRed = 0, leftBlue = 0;
				int rightRed = 0, rightBlue = 0;
				for (int y = 0; y < SCALED_HEIGHT; y++) {
					for (int x = 0; x < HALF_WIDTH; x++) {
						final int pixel = bitmap.getPixel(x, y);
						leftRed += Color.red(pixel);
						leftBlue += Color.blue(pixel);
					}
					for (int x = HALF_WIDTH; x < SCALED_WIDTH; x++) {
						final int pixel = bitmap.getPixel(x, y);
						rightRed += Color.red(pixel);
						rightBlue += Color.blue(pixel);
					}
				}
				System.out.println("leftRed: " + leftRed);
				System.out.println("rightRed: " + rightRed);
				System.out.println("leftBlue: " + leftBlue);
				System.out.println("rightBlue: " + rightBlue);
				this.buttonAngle = 90.0 * angleMultiplier;
				final boolean beacon1BlueLeft = leftRed + rightBlue < rightRed + leftBlue;
				if (beacon1BlueLeft ^ this.allianceColorIsBlue) {
					System.out.println("Going right");
					this.buttonAngle -= 10.0;
				}
				else {
					System.out.println("Going left");
					this.buttonAngle += 10.0;
				}
				this.nextStage();
				break;
			case TURN_TO_PRESS_BUTTON:
				if (this.direction.gyroCorrect(this.buttonAngle, 1.0, this.configuration.imu.getRelativeYaw(), 0.08, 0.1) > 10) {
					this.direction.stopDrive();
					this.configuration.resetEncoder();
					this.nextStage();
				}
				break;
			case PUSH_BUTTON:
				this.direction.drive(0.0, 0.3);
				this.direction.gyroCorrect(this.buttonAngle, 1.0, this.configuration.imu.getRelativeYaw(), 0.05, 0.1);
				if (this.configuration.encoderPositive() > 200) {
					this.direction.stopDrive();
					this.configuration.resetEncoder();
					this.nextStage();
				}
				break;
			case FACE_SQUARE:
				if (this.direction.gyroCorrect(90.0 * angleMultiplier, 1.0, this.configuration.imu.getRelativeYaw(), 0.08, 0.1) > 10) {
					this.direction.stopDrive();
					this.nextStage();
				}
				break;
			case BACK_UP:
				this.direction.drive(0.0, -0.5);
				this.direction.gyroCorrect(90.0 * angleMultiplier, 1.0, this.configuration.imu.getRelativeYaw(), 0.05, 0.1);
				if (this.configuration.encoderPositive() > 4000) {
					this.direction.stopDrive();
					this.nextStage();
				}
				break;
			case TURN_TO_KNOCK_BALL:
				if (this.direction.gyroCorrect(45.0 * angleMultiplier, 1.0, this.configuration.imu.getRelativeYaw(), 0.08, 0.1) > 10) {
					this.direction.stopDrive();
					this.nextStage();
				}
				break;
			case TURN_BACK_AFTER_BALL:
				if (this.direction.gyroCorrect(90.0 * angleMultiplier, 1.0, this.configuration.imu.getRelativeYaw(), 0.08, 0.1) > 10) {
					this.direction.stopDrive();
					this.configuration.resetEncoder();
					this.nextStage();
				}
				break;
			case SLOW_TO_CENTER:
				this.direction.drive(0.0, -0.3);
				if (this.configuration.encoderPositive() > 1000) {
					this.direction.stopDrive();
					this.nextStage();
				}
				break;
			case STOPPED:
				this.configuration.stopMotion();
		}
		if (this.stage.ordinal() >= Stage.DRIVE_OFF_WALL.ordinal()) this.configuration.advanceShooterToDown();
		if (this.stage != Stage.STOPPED) this.configuration.move(this.direction, 0.06);
		this.telemetry.addData("Stage", this.stage);
	}
	protected void matchEnd() {
		this.configuration.teardown();
	}

	//Gets distance we are to the left of the center of the image
	private static float getLateralDistance(VectorF translation) {
		final int target = 0;
		return -(translation.get(1) - target);
	}
	private boolean vuforiaTurn(VectorF translation, double minPower) {
		final float lateralDistance = getLateralDistance(translation);
		if (Math.abs(lateralDistance) > 50) {
			double turnPower = lateralDistance * 0.0007;
			if (Math.abs(turnPower) > 0.05) turnPower = Math.signum(turnPower) * 0.05; //max out the turn power
			this.direction.turn(turnPower + minPower * Math.signum(turnPower));
			return false;
		}
		else {
			this.direction.turn(0.0);
			return true;
		}
	}
	//Drive forward towards beacon and turn to face it, until reaching desired distance
	private void vuforiaIn(OpenGLMatrix pose, float finalDistance) {
		if (pose == null) {
			this.direction.drive(0.0, 0.05);
			this.direction.turn(0.0);
		}
		else {
			final VectorF translation = pose.getTranslation();
			final float normalDistance = -translation.get(2);
			if (normalDistance < finalDistance) {
				this.direction.stopDrive();
				this.nextStage();
			}
			else {
				this.direction.drive(0.0, 0.12);
				this.vuforiaTurn(translation, 0.0);
			}
		}
	}
	//Set the stage variable and reset the stage timer
	private void setStage(Stage newStage) {
		this.stage = newStage;
		this.stageTimer.reset();
	}
	private void nextStage() {
		this.setStage(Stage.values()[this.stage.ordinal() + 1]);
	}
}