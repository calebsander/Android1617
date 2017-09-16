package org.gearticks.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.vuforia.HINT;
import com.vuforia.Vuforia;
import java.util.HashMap;
import java.util.Map;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.gearticks.AutonomousDatalogger;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.vuforia.VuforiaKey;
import org.gearticks.hardware.configurations.ResQConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.opmodes.BaseOpMode;

@Autonomous
@Disabled
public class ResQAutononomous extends BaseOpMode {
	private ResQConfiguration configuration;
	private DriveDirection direction;
	private static final Map<String, Integer> IMAGE_IDS = new HashMap<>();
	static {
		IMAGE_IDS.put("Wheels", 0);
		IMAGE_IDS.put("Tools", 1);
		IMAGE_IDS.put("Legos", 2);
		IMAGE_IDS.put("Gears", 3);
	}
	private VuforiaTrackables beaconImages;
	private VuforiaTrackableDefaultListener imageListener;
	private AutonomousDatalogger datalogger;
	private static final double TICKS_PER_MM = 4.83;
	private static final double TICKS_PER_RADIAN = 1200.0;

	private enum Stage {
		DRIVE_OUT,
		WAIT_BEFORE_VUFORIA,
		VUFORIA_SNAPSHOT,
		DRIVE_TO_IN_FRONT,
		WAIT_BEFORE_TURN,
		TURN,
		WAIT_BEFORE_BEACON,
		VUFORIA_TO_BEACON,
		STOPPED
	}
	private Stage stage;
	private double ticksToTravel;
	private double ticksToTurn;

	private void nextStage() {
		this.resetStartTime();
		this.stage = Stage.values()[this.stage.ordinal() + 1];
	}
	protected void initialize() {
		this.configuration = new ResQConfiguration(this.hardwareMap);
		this.configuration.bl.setRunMode(RunMode.STOP_AND_RESET_ENCODER);
		this.configuration.bl.setRunMode(RunMode.RUN_WITHOUT_ENCODER);
		this.direction = new DriveDirection();
		this.stage = Stage.values()[0];
		final VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(com.qualcomm.ftcrobotcontroller.R.id.cameraMonitorViewId);
		parameters.vuforiaLicenseKey = VuforiaKey.KEY;
		final VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(parameters);
		Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 2);
		this.beaconImages = vuforia.loadTrackablesFromAsset("FTC_2016-17");
		for (Map.Entry<String, Integer> imageId : IMAGE_IDS.entrySet()) this.beaconImages.get(imageId.getValue()).setName(imageId.getKey());
		this.imageListener = (VuforiaTrackableDefaultListener)this.beaconImages.get(IMAGE_IDS.get("Legos")).getListener();
		this.datalogger = new AutonomousDatalogger();
	}
	public void matchStart() {
		this.beaconImages.activate();
	}
	protected void loopAfterStart() {
		boolean resetEncoder = false; //whether to reset encoder
		OpenGLMatrix pose;
		switch (this.stage) {
			case DRIVE_OUT:
				this.direction.drive(0.0, -1.0);
				this.direction.turn(0.0);
				if (Math.abs(this.configuration.bl.encoderValue()) > 6500) {
					this.direction.stopDrive();
					this.nextStage();
				}
				break;
			case WAIT_BEFORE_VUFORIA:
				this.direction.stopDrive();
				if (this.getRuntime() > 0.3) {
					resetEncoder = true;
					this.nextStage();
				}
				break;
			case VUFORIA_SNAPSHOT:
				pose = this.imageListener.getPose();
				if (pose != null) {
					final VectorF translation = pose.getTranslation();
					final float lateralDistance = translation.get(1);
					final Orientation rotation = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS);
					final float angleToNormal = -rotation.secondAngle;
					final double distanceToTravel = lateralDistance / Math.sin(angleToNormal);
					this.ticksToTravel = distanceToTravel * TICKS_PER_MM;
					this.ticksToTurn = angleToNormal * TICKS_PER_RADIAN;
					this.datalogger.writeLine("Angle: " + angleToNormal);
					this.datalogger.writeLine("Distance: " + lateralDistance);
					this.datalogger.writeLine("Going: " + distanceToTravel);
					this.nextStage();
				}
				break;
			case DRIVE_TO_IN_FRONT:
				this.direction.drive(0.0, -0.4);
				if (Math.abs(this.configuration.bl.encoderValue()) > this.ticksToTravel) {
					this.direction.stopDrive();
					this.nextStage();
				}
				break;
			case WAIT_BEFORE_TURN:
				this.direction.stopDrive();
				if (this.getRuntime() > 0.2) {
					resetEncoder = true;
					this.nextStage();
				}
				break;
			case TURN:
				this.direction.turn(0.4);
				if (Math.abs(this.configuration.bl.encoderValue()) > this.ticksToTurn) {
					this.direction.stopDrive();
					this.nextStage();
				}
				break;
			case WAIT_BEFORE_BEACON:
				if (this.getRuntime() > 1.5) this.nextStage();
				break;
			case VUFORIA_TO_BEACON:
				pose = this.imageListener.getPose();
				if (pose == null) this.direction.stopDrive();
				else {
					final VectorF translation = pose.getTranslation();
					final float lateralDistance = translation.get(1);
					final float normalDistance = -translation.get(2);
					if (normalDistance < 300F) this.nextStage();
					else {
						this.direction.drive(0.0, -0.15);
						double turnPower = lateralDistance / 200.0;
						if (Math.abs(turnPower) > 0.2) turnPower = Math.signum(turnPower) * 0.2;
						this.direction.turn(turnPower);
					}
					this.telemetry.addData("Target", translation);
				}
				break;
			case STOPPED:
				this.direction.stopDrive();
		}
		this.configuration.move(this.direction, MotorWrapper.NO_ACCEL_LIMIT);
		if (resetEncoder) {
			this.configuration.bl.setRunMode(RunMode.STOP_AND_RESET_ENCODER);
			this.configuration.bl.setRunMode(RunMode.RUN_WITHOUT_ENCODER);
		}
		this.telemetry.addData("Stage", this.stage);
	}
	protected void matchEnd() {
		this.datalogger.close();
	}
}