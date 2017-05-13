package org.gearticks.autonomous.velocity.components.generic;

import android.util.Log;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent.DefaultTransition;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.OrientableConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.Utils;

public class GiroTurn extends AutonomousComponentHardware<OrientableConfiguration, DefaultTransition> {
	private final DriveDirection direction;
	private final double targetHeading;
	private double angleMultiplier;
	private final double power;
	private final double range;
	private final int correctTimes;

	/**
	 * @param targetHeading - between 0 and 360, input to DriveDirection.gyroCorrect
	 * @param opModeContext - the OpModeContext this is running in
	 * @param id - descriptive name for logging
	 */
	public GiroTurn(double targetHeading, OpModeContext<? extends OrientableConfiguration> opModeContext, String id) {
		this(targetHeading, 0.05, 10, opModeContext, id);
	}
	public GiroTurn(double targetHeading, double power, int correctTimes, OpModeContext<? extends OrientableConfiguration> opModeContext, String id) {
		this(targetHeading, power, correctTimes, 1.0, opModeContext, id);
	}
	public GiroTurn(double targetHeading, double power, int correctTimes, double range, OpModeContext<? extends OrientableConfiguration> opModeContext, String id) {
		super(opModeContext, DefaultTransition.class, id);
		this.direction = new DriveDirection();
		this.targetHeading = targetHeading;
		this.power = power;
		this.range = range;
		this.correctTimes = correctTimes;
	}

	@Override
	public void setup() {
		super.setup();
		final boolean allianceColorIsBlue = AllianceOption.allianceOption.getRawSelectedOption() == AllianceOption.BLUE;
		if (allianceColorIsBlue) angleMultiplier = 1.0; //angles were calculated for blue side
		else angleMultiplier = -1.0; //invert all angles for red side
		this.configuration.resetEncoder();
	}

	@Override
	public DefaultTransition run() {
		final DefaultTransition superTransition = super.run();
		if (superTransition != null) return superTransition;

		final DefaultTransition transition;
		if (this.direction.gyroCorrect(this.targetHeading * this.angleMultiplier, this.range, this.configuration.getHeading(), this.power, 0.1) > this.correctTimes) {
			Log.d(Utils.TAG, "Heading = " + this.configuration.getHeading());
			transition = DefaultTransition.DEFAULT;
		}
		else transition = null;
		this.configuration.move(this.direction, 0.06);

		return transition;
	}
}
