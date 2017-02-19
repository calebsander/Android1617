package org.gearticks.autonomous.velocity.components.experimental;

import android.util.Log;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.joystickoptions.ParkingOption;
import org.gearticks.opmodes.utility.Utils;

public class AutonomousParkingSelector extends AutonomousComponentHardware<VelocityConfiguration> {
	public static final Transition CENTER = new Transition("Center vortex"), CORNER = new Transition("Corner vortex");
	/**
	 *
	 * @param opModeContext - the OpModeContext this is running in
	 */
	public AutonomousParkingSelector(OpModeContext<VelocityConfiguration> opModeContext) {
		super(opModeContext, "Switch based on parking");
	}

	@Override
	public Transition run() {
		final Transition superTransition = super.run();
		if (superTransition != null) return superTransition;

		switch (ParkingOption.parkingOption.getRawSelectedOption()) {
			case CENTER:
				Log.d(Utils.TAG, "Center vortex");
				return CENTER;
			case CORNER:
				Log.d(Utils.TAG, "Corner vortex");
				return CORNER;
			default: throw new RuntimeException("Parking option is not center or corner");
		}
	}
}
