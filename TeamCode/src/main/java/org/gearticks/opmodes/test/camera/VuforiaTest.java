package org.gearticks.opmodes.test.camera;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.vuforia.HINT;
import com.vuforia.Vuforia;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.gearticks.vuforia.VuforiaKey;
import org.gearticks.joystickoptions.ValuesJoystickOption;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp
public class VuforiaTest extends BaseOpMode {
	private static final String[] IMAGE_NAMES = new String[]{"Pictograph"};
	private VuforiaTrackables pictographs;
	private ValuesJoystickOption<CameraDirection> frontOption;

	public void initialize() {
		this.frontOption = new ValuesJoystickOption<>("Camera", CameraDirection.values());
		this.addOption(this.frontOption);
	}
	public void matchStart() {
		final VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
		parameters.vuforiaLicenseKey = VuforiaKey.KEY;
		//parameters.cameraDirection = this.frontOption.getRawSelectedOption();
		parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
		final VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(parameters);
		//Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 4);
		this.pictographs = vuforia.loadTrackablesFromAsset("RelicVuMark");
		for (int i = 0; i < IMAGE_NAMES.length; i++) this.pictographs.get(i).setName(IMAGE_NAMES[i]);
		this.pictographs.activate();
	}
	public void loopAfterStart() {
		for (final VuforiaTrackable pictograph : this.pictographs) {
			RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(pictograph);
			final VuforiaTrackableDefaultListener listener = (VuforiaTrackableDefaultListener)pictograph.getListener();
			final OpenGLMatrix pose = listener.getPose();
			final String telemetryText;
			if (pose == null) telemetryText = "Not found";
			else telemetryText = pose.getTranslation().toString() + " " + Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYX, AngleUnit.DEGREES).toString();
			final String name;
			if (vuMark == RelicRecoveryVuMark.LEFT) {
				name = "Left";
			} else if (vuMark == RelicRecoveryVuMark.CENTER) {
				name = "Center";
			} else if (vuMark == RelicRecoveryVuMark.RIGHT) {
				name = "Right";
			} else {
				name = "Not Found";
			}
			this.telemetry.addData(name, telemetryText);
		}
	}
}