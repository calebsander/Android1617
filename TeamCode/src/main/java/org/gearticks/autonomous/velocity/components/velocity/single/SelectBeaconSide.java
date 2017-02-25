package org.gearticks.autonomous.velocity.components.velocity.single;

import android.util.Log;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.vuforia.VuforiaConfiguration;
import org.gearticks.vuforia.VuforiaConfiguration.BeaconColorCounts;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.opmodes.units.SideOfButton;
import org.gearticks.opmodes.utility.Utils;

public class SelectBeaconSide extends AutonomousComponentHardware<VelocityConfiguration> {
	public static final Transition
		LEFT_TRANSITION = new Transition("Left"),
		RIGHT_TRANSITION = new Transition("Right"),
		UNKNOWN_TRANSITION = new Transition("Unknown");

	private final VuforiaConfiguration vuforiaConfiguration;
	private boolean allianceColorIsBlue;
	private final PictureResult pictureResult;

	/**
	 * @param isBlue
	 * @param opModeContext - the OpModeContext this is running in
	 * @param id - descriptive name for logging
	 * @param pictureResult - the object to store the beacon color counts in
	 */
	public SelectBeaconSide(boolean isBlue, OpModeContext<VelocityConfiguration> opModeContext, String id, PictureResult pictureResult) {
		super(opModeContext, id);
		this.vuforiaConfiguration = opModeContext.getVuforiaConfiguration();
		this.pictureResult = pictureResult;
		this.allianceColorIsBlue = isBlue;
	}
	public SelectBeaconSide(boolean isBlue, String id, OpModeContext<VelocityConfiguration> opModeContext) {
		this(isBlue, opModeContext, id, new PictureResult());
	}

	@Override
	public Transition run() {
		final Transition superTransition = super.run();
		if (superTransition != null) return superTransition;

		final BeaconColorCounts colorCounts = this.vuforiaConfiguration.getColorCounts();
		this.pictureResult.colorCounts = colorCounts;
		final SideOfButton sideOfButton = getButtonToPress(this.vuforiaConfiguration.getBeaconBlueSide(colorCounts));
		switch (sideOfButton) {
			case LEFT:
				Log.i(Utils.TAG, "Going left");
				return LEFT_TRANSITION;
			case RIGHT:
				Log.i(Utils.TAG, "Going right");
				return RIGHT_TRANSITION;
			default:
				Log.i(Utils.TAG, "Button color could not be detected");
				return UNKNOWN_TRANSITION;
		}
	}


	private SideOfButton getButtonToPress(SideOfButton sideOfBlue) {
		if (this.allianceColorIsBlue) {
			return sideOfBlue;
		}
		else {
			return sideOfBlue.getInverse();
		}
	}

	public static class PictureResult {
		BeaconColorCounts colorCounts;
	}

}
