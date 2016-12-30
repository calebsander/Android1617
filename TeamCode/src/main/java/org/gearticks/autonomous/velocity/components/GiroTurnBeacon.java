package org.gearticks.autonomous.velocity.components;

import android.support.annotation.NonNull;

import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;

public class GiroTurnBeacon extends AutonomousComponentVelocityBase {
	private final DriveDirection direction = new DriveDirection();
	private final double targetHeading;
	private boolean allianceColorIsBlue;
	private final double angleMultiplier;
	private double buttonAngle;



	/**
	 *
	 * @param targetHeading - between 0 and 360, input to DriveDirection.gyroCorrect
	 * @param configuration - config file
	 * @param id - descriptive name for logging
	 */
	public GiroTurnBeacon(double targetHeading, @NonNull VelocityConfiguration configuration, String id) {
		super(configuration, id);
		this.targetHeading = targetHeading;
		this.allianceColorIsBlue = AllianceOption.allianceOption.getRawSelectedOption() == AllianceOption.BLUE;
		if (this.allianceColorIsBlue) angleMultiplier = 1.0; //angles were calculated for blue side
		else angleMultiplier = -1.0; //invert all angles for red side
	}

	@Override
	public void setup(int inputPort) {
		super.setup(inputPort);
		this.getConfiguration().resetEncoder();
	}

	@Override
	public int run() {
		int transition = 0;
		super.run();

		this.buttonAngle = 90.0 * angleMultiplier;
		if (this.direction.gyroCorrect(this.buttonAngle += this.targetHeading, angleMultiplier, this.getConfiguration().imu.getRelativeYaw(), 0.05, 0.1) > 10) {
			transition = 1;
		}
		this.getConfiguration().move(this.direction, 0.06);

		return transition;
	}

	@Override
	public void tearDown() {
		super.tearDown();
		//stop motors
		this.direction.stopDrive();
		this.getConfiguration().move(this.direction, 0.06);
	}




}
