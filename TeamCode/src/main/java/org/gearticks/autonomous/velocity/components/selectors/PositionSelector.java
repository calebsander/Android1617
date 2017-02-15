package org.gearticks.autonomous.velocity.components.selectors;

import android.support.annotation.NonNull;
import android.util.Log;

import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.joystickoptions.PositionOption;
import org.gearticks.opmodes.utility.Utils;

public class PositionSelector extends AutonomousComponentHardware<VelocityConfiguration> {
	public static final int POSITION_1 = newTransition(), POSITION_2 = newTransition();
	/**
	 *
	 * @param configuration
	 */
	public PositionSelector(@NonNull VelocityConfiguration configuration) {
		super(configuration, "Switch based on position");
	}

	@Override
	public int run() {
		final int superTransition = super.run();
		if (superTransition != NOT_DONE) return superTransition;

		switch (PositionOption.positionOption.getRawSelectedOption()) {
			case POSITION_1:
				Log.d(Utils.TAG, "Position 1");
				return POSITION_1;
			case POSITION_2:
				Log.d(Utils.TAG, "Position 2");
				return POSITION_2;
			default: throw new RuntimeException("Position option is not 1 or 2");
		}
	}
}
