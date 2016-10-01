package org.gearticks.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.vuforia.HINT;
import com.vuforia.Vuforia;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.gearticks.VuforiaKey;
import org.gearticks.opmodes.BaseOpMode;

@Autonomous(name = "Vuforia Test", group = "test")
@Disabled
public class VuforiaTest extends BaseOpMode {
	private static final String[] IMAGE_NAMES = new String[]{"Wheels", "Tools", "Legos", "Gears"};
	private VuforiaTrackables beaconImages;

	public void initialize() {
		final VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
		parameters.vuforiaLicenseKey = VuforiaKey.KEY;
		final VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(parameters);
		Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 4);
		this.beaconImages = vuforia.loadTrackablesFromAsset("FTC_2016-17");
		for (int i = 0; i < IMAGE_NAMES.length; i++) this.beaconImages.get(i).setName(IMAGE_NAMES[i]);
	}
	public void matchStart() {
		this.beaconImages.activate();
	}
	public void loopAfterStart() {
		for (final VuforiaTrackable beaconImage : this.beaconImages) {
			final VuforiaTrackableDefaultListener listener = (VuforiaTrackableDefaultListener)beaconImage.getListener();
			final OpenGLMatrix pose = listener.getPose();
			final String telemetryText;
			if (pose == null) telemetryText = "Not found";
			else telemetryText = pose.getTranslation().toString();
			this.telemetry.addData(beaconImage.getName(), telemetryText);
		}
		this.telemetry.update();
	}
}