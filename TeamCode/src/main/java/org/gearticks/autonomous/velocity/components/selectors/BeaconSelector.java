package org.gearticks.autonomous.velocity.components.selectors;

import android.support.annotation.NonNull;
import android.util.Log;

import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.joystickoptions.BeaconOption;
import org.gearticks.opmodes.utility.Utils;

public class BeaconSelector extends AutonomousComponentHardware<VelocityConfiguration> {
	public static final int NO_BEACONS = newTransition(), NEAR_BEACON = newTransition();
	public static final int FAR_BEACON = newTransition(), BOTH_BEACONS = newTransition();
	/**
	 *
	 * @param configuration
	 */
	public BeaconSelector(@NonNull VelocityConfiguration configuration) {
		super(configuration, "Switch based on position");
	}

	@Override
	public int run() {
		final int superTransition = super.run();
		if (superTransition != NOT_DONE) return superTransition;

		switch (BeaconOption.beaconOption.getRawSelectedOption()) {
			case NO_BEACONS:
				Log.d(Utils.TAG, "No beacons");
				return NO_BEACONS;
			case NEAR_BEACON:
				Log.d(Utils.TAG, "Near beacon only");
				return NEAR_BEACON;
			case FAR_BEACON:
				Log.d(Utils.TAG, "Far beacon");
				return FAR_BEACON;
			case BOTH_BEACONS:
				Log.d(Utils.TAG, "Both beacons");
				return BOTH_BEACONS;
			default: throw new RuntimeException("Beacon option is not none, near, far or both");
		}
	}
}
