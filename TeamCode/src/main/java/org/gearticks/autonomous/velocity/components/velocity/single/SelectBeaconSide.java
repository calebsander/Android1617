package org.gearticks.autonomous.velocity.components.velocity.single;

import android.support.annotation.NonNull;
import android.util.Log;
import org.gearticks.vuforia.VuforiaConfiguration;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.opmodes.units.SideOfButton;
import org.gearticks.opmodes.utility.Utils;

public class SelectBeaconSide extends AutonomousComponentHardware<VelocityConfiguration> {
	public static final int LEFT_TRANSITION = newTransition(), RIGHT_TRANSITION = newTransition(), UNKNOWN_TRANSITION = newTransition();

	private final VuforiaConfiguration vuforiaConfiguration;
	private boolean allianceColorIsBlue;

	/**
	 * @param configuration
	 * @param id - descriptive name for logging
	 */
	public SelectBeaconSide(@NonNull VuforiaConfiguration vuforiaConfiguration, @NonNull VelocityConfiguration configuration, String id) {
		super(configuration, id);
		this.vuforiaConfiguration = Utils.assertNotNull(vuforiaConfiguration);

	}

	@Override
	public void onMatchStart() {
		this.allianceColorIsBlue = AllianceOption.allianceOption.getRawSelectedOption() == AllianceOption.BLUE;
	}

	@Override
	public int run() {
		final int superTransition = super.run();
		if (superTransition != NOT_DONE) return superTransition;

		final SideOfButton sideOfButton = getButtonToPress(this.vuforiaConfiguration.getBeaconBlueSide());
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

}