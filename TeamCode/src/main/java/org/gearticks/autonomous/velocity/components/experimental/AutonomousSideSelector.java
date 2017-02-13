package org.gearticks.autonomous.velocity.components.experimental;

import android.util.Log;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.opmodes.utility.Utils;

public class AutonomousSideSelector extends AutonomousComponentHardware<VelocityConfiguration> {
	public static final Transition BLUE = new Transition("Blue"), RED = new Transition("Red");
	/**
	 *
	 * @param opModeContext - the OpModeContext this is running in
	 */
	public AutonomousSideSelector(OpModeContext<VelocityConfiguration> opModeContext) {
		super(opModeContext, "Switch based on alliance");
	}

	@Override
	public Transition run() {
		final Transition superTransition = super.run();
		if (superTransition != null) return superTransition;

		switch (AllianceOption.allianceOption.getRawSelectedOption()) {
			case BLUE:
				Log.d(Utils.TAG, "Blue Side");
				return BLUE;
			case RED:
				Log.d(Utils.TAG, "Red Side");
				return RED;
			default: throw new RuntimeException("Alliance option is not red or blue");
		}
	}
}
