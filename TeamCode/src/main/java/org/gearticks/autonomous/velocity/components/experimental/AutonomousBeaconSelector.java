package org.gearticks.autonomous.velocity.components.experimental;

import android.util.Log;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.joystickoptions.BeaconOption;
import org.gearticks.opmodes.utility.Utils;

public class AutonomousBeaconSelector extends AutonomousComponentHardware<VelocityConfiguration> {
	public static final Transition NONE = new Transition("No beacons"), NEAR = new Transition("Near Beacon");
	public static final Transition FAR = new Transition("Far beacon"), BOTH = new Transition("Both Beacons");
	/**
	 *
	 * @param opModeContext - the OpModeContext this is running in
	 */
	public AutonomousBeaconSelector(OpModeContext<VelocityConfiguration> opModeContext) {
		super(opModeContext, "Switch based on beacon");
	}

	@Override
	public Transition run() {
		final Transition superTransition = super.run();
		if (superTransition != null) return superTransition;

		switch (BeaconOption.beaconOption.getRawSelectedOption()) {
			case NONE:
				Log.d(Utils.TAG, "No beacons");
				return NONE;
			case NEAR:
				Log.d(Utils.TAG, "Near beacon");
				return NEAR;
			case FAR:
				Log.d(Utils.TAG, "Far beacon");
				return FAR;
			case BOTH:
				Log.d(Utils.TAG, "Both beacons");
				return BOTH;
			default: throw new RuntimeException("Beacon option is not none, near, far, or both");
		}
	}
}
