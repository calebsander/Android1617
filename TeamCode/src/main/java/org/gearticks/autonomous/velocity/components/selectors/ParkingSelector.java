package org.gearticks.autonomous.velocity.components.selectors;

import android.support.annotation.NonNull;
import android.util.Log;

import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.joystickoptions.ParkingOption;
import org.gearticks.opmodes.utility.Utils;

public class ParkingSelector extends AutonomousComponentHardware<VelocityConfiguration> {
	public static final int CENTER = newTransition(), CORNER = newTransition();
	/**
	 *
	 * @param configuration
	 */
	public ParkingSelector(@NonNull VelocityConfiguration configuration) {
		super(configuration, "Switch based on position");
	}

	@Override
	public int run() {
		final int superTransition = super.run();
		if (superTransition != NOT_DONE) return superTransition;

		switch (ParkingOption.parkingOption.getRawSelectedOption()) {
			case CENTER:
				Log.d(Utils.TAG, "Center Vortex");
				return CENTER;
			case CORNER:
				Log.d(Utils.TAG, "Corner Vortex");
				return CORNER;
			default: throw new RuntimeException("Parking option is not center or corner");
		}
	}
}
