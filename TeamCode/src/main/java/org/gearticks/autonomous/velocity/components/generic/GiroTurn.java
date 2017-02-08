package org.gearticks.autonomous.velocity.components.generic;

import android.util.Log;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.opmodes.utility.Utils;

public class GiroTurn extends AutonomousComponentHardware<VelocityConfiguration> {
	private final DriveDirection direction = new DriveDirection();
	private final double targetHeading;
	private double angleMultiplier;
	private final double power;
	private final int range;

	/**
	 * @param targetHeading - between 0 and 360, input to DriveDirection.gyroCorrect
	 * @param opModeContext - the OpModeContext this is running in
	 * @param id - descriptive name for logging
	 */
	public GiroTurn(double targetHeading, OpModeContext<VelocityConfiguration> opModeContext, String id) {
		this(targetHeading, 0.05, 10, opModeContext, id);
	}
	public GiroTurn(double targetHeading, double power, int range, OpModeContext<VelocityConfiguration> opModeContext, String id) {
		super(opModeContext.configuration, id);
		this.targetHeading = targetHeading;
		this.power = power;
		this.range = range;
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
	public int run() {
		final int superTransition = super.run();
		if (superTransition != NOT_DONE) return superTransition;

		final int transition;
		if (this.direction.gyroCorrect(this.targetHeading * this.angleMultiplier, 1.0, this.configuration.imu.getRelativeYaw(), this.power, 0.1) > this.range) {
			Log.d(Utils.TAG, "Heading = " + this.configuration.imu.getRelativeYaw());
			transition = NEXT_STATE;
		}
		else transition = NOT_DONE;
		this.configuration.move(this.direction, 0.06);

		return transition;
	}

	@Override
	public void tearDown() {
		super.tearDown();
		//stop motors
		this.configuration.stopMotion();
	}
}
