package org.gearticks.autonomous.velocity.components.experimental;

import android.util.Log;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.joystickoptions.PositionOption;
import org.gearticks.opmodes.utility.Utils;

public class AutonomousPositionSelector extends AutonomousComponentHardware<VelocityConfiguration> {
	public static final Transition POSITION_1 = new Transition("Position 1"), POSITION_2 = new Transition("Position 2");
	/**
	 *
	 * @param opModeContext - the OpModeContext this is running in
	 */
	public AutonomousPositionSelector(OpModeContext<VelocityConfiguration> opModeContext) {
		super(opModeContext, "Switch based on position");
	}

	@Override
	public Transition run() {
		final Transition superTransition = super.run();
		if (superTransition != null) return superTransition;

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
