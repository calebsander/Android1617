package org.gearticks.autonomous.velocity.components.experimental;

import android.support.annotation.NonNull;
import android.util.Log;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.gearticks.GamepadWrapper;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.opmodes.utility.Utils;

public class AutonomousSideSelecter extends AutonomousComponentHardware<VelocityConfiguration> {
	public static final int BLUE = newTransition(), RED = newTransition();
	public boolean allianceColorIsBlue;
	/**
	 *
	 * @param configuration
	 */
	public AutonomousSideSelecter(@NonNull VelocityConfiguration configuration) {
		super(configuration);
	}

	@Override
	public void onMatchStart() {
		this.allianceColorIsBlue = AllianceOption.allianceOption.getRawSelectedOption() == AllianceOption.BLUE;
	}

	@Override
	public int run() {
		final int superTransition = super.run();
		if (superTransition != NOT_DONE) return superTransition;

		if (allianceColorIsBlue) {
			Log.d(Utils.TAG, "Blue Side");
			return BLUE;
		}
		else {
			Log.d(Utils.TAG, "Red Side");
			return RED;
		}
	}
}
