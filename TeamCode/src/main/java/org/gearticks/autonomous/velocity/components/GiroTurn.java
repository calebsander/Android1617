package org.gearticks.autonomous.velocity.components;

import android.support.annotation.NonNull;
import android.util.Log;

import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.opmodes.utility.Utils;

public class GiroTurn extends AutonomousComponentHardware<VelocityConfiguration> {
	private final DriveDirection direction = new DriveDirection();
	private final double targetHeading;
	private double angleMultiplier;

	/**
	 * @param targetHeading - between 0 and 360, input to DriveDirection.gyroCorrect
	 * @param configuration - config file
	 * @param id - descriptive name for logging
	 */
	public GiroTurn(double targetHeading, @NonNull VelocityConfiguration configuration, String id) {
		super(configuration, id);
		this.targetHeading = targetHeading;
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
		if (this.direction.gyroCorrect(this.targetHeading * this.angleMultiplier, 1.0, this.configuration.imu.getRelativeYaw(), 0.05, 0.1) > 10) {
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
