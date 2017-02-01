package org.gearticks.autonomous.velocity.components.experimental;

import android.support.annotation.NonNull;
import android.util.Log;

import org.gearticks.PIDControl.MiniPID;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.opencv.OpenCvConfiguration;
import org.gearticks.opencv.vision.BeaconColorResult;
import org.gearticks.opencv.vision.ImageProcessorResult;
import org.gearticks.opmodes.units.SideOfButton;
import org.gearticks.opmodes.utility.Utils;
import org.gearticks.vuforia.VuforiaConfiguration;
import org.opencv.core.Mat;

import static org.gearticks.opmodes.utility.Utils.TAG;

public class OpenCVBeacon extends AutonomousComponentHardware<VelocityConfiguration> {
	public static final int LEFT_TRANSITION = newTransition(), RIGHT_TRANSITION = newTransition(), UNKNOWN_TRANSITION = newTransition();

	private final VuforiaConfiguration vuforiaConfiguration;
    private final OpenCvConfiguration openCvConfiguration;
	private boolean allianceColorIsBlue;

	/**
	 * @param configuration
	 * @param id - descriptive name for logging
	 */
	public OpenCVBeacon(@NonNull OpenCvConfiguration openCvConfiguration, @NonNull VuforiaConfiguration vuforiaConfiguration, @NonNull VelocityConfiguration configuration, String id) {
		super(configuration, id);
		this.vuforiaConfiguration = Utils.assertNotNull(vuforiaConfiguration);
        this.openCvConfiguration = openCvConfiguration;

	}

	@Override
	public void onMatchStart() {
		this.allianceColorIsBlue = AllianceOption.allianceOption.getRawSelectedOption() == AllianceOption.BLUE;
	}

	@Override
	public void setup() {
		super.setup();
        this.openCvConfiguration.frameGrabber.grabSingleFrame();
		Mat m = new Mat();

	}

	@Override
	public int run() {
		final int superTransition = super.run();
		if (superTransition != NOT_DONE) return superTransition;

        if (this.openCvConfiguration.frameGrabber.isResultReady()){
            Log.v(TAG, "New Frame is ready. Requesting next frame.");
            ImageProcessorResult<BeaconColorResult> result =  this.openCvConfiguration.frameGrabber.getResult();
            Log.v(TAG, "Result: Left color =  "+ result.getResult().getLeftColor() + ", right color = " +  result.getResult().getRightColor());
            this.openCvConfiguration.frameGrabber.grabSingleFrame();
        }
        else {
            Log.v(TAG, "New Frame is not ready. Waiting for next cycle.");
        }

//		final SideOfButton sideOfButton = getButtonToPress(this.vuforiaConfiguration.getBeaconBlueSide());
//		switch (sideOfButton) {
//			case LEFT:
//				Log.i(Utils.TAG, "Going left");
//				return LEFT_TRANSITION;
//			case RIGHT:
//				Log.i(Utils.TAG, "Going right");
//				return RIGHT_TRANSITION;
//			default:
//				Log.i(Utils.TAG, "Button color could not be detected");
//				return UNKNOWN_TRANSITION;
//		}
        return superTransition;
	}


	private SideOfButton getButtonToPress(SideOfButton sideOfBlue) {
		if (this.allianceColorIsBlue) {
			return sideOfBlue;
		}
		else {
			return sideOfBlue.getInverse();
		}
	}

    @Override
    public void tearDown() {
        super.tearDown();
        this.openCvConfiguration.deactivate();
    }

}
