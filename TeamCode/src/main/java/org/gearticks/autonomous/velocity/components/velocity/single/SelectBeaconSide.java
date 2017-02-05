package org.gearticks.autonomous.velocity.components.velocity.single;

import android.support.annotation.NonNull;
import android.util.Log;
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
	 * @param configuration
	 * @param id - descriptive name for logging
	 */
	public SelectBeaconSide(PictureResult pictureResult, @NonNull VuforiaConfiguration vuforiaConfiguration, @NonNull VelocityConfiguration configuration, String id) {
		super(configuration, id);
		this.vuforiaConfiguration = Utils.assertNotNull(vuforiaConfiguration);
		this.pictureResult = pictureResult;
	}
	public SelectBeaconSide(@NonNull VuforiaConfiguration vuforiaConfiguration, @NonNull VelocityConfiguration configuration, String id) {
		this(new PictureResult(), vuforiaConfiguration, configuration, id);
	}

	@Override
	public void onMatchStart() {
		this.allianceColorIsBlue = AllianceOption.allianceOption.getRawSelectedOption() == AllianceOption.BLUE;
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
