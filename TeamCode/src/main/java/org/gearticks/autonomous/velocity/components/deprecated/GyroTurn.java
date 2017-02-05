package org.gearticks.autonomous.velocity.components.deprecated;

import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;

@Deprecated
public class GyroTurn extends AutonomousComponentHardware<VelocityConfiguration> {
	private final DriveDirection direction;
	private final double targetHeading;
	private double angleMultiplier;

	/**
	 * @param targetHeading - between 0 and 360, input to DriveDirection.gyroCorrect
	 * @param configuration - config file
	 * @param id - descriptive name for logging
	 */
	public GyroTurn(double targetHeading, VelocityConfiguration configuration, String id) {
		super(configuration, id);
		this.direction = new DriveDirection();
		this.targetHeading = targetHeading;
	}

	@Override
	public void setup() {
		super.setup();
		this.configuration.resetEncoder();
		final boolean allianceColorIsBlue = AllianceOption.allianceOption.getRawSelectedOption() == AllianceOption.BLUE;
		if (allianceColorIsBlue) this.angleMultiplier = 1.0; //angles were calculated for blue side
		else this.angleMultiplier = -1.0; //invert all angles for red side
	}

	@Override
	public Transition run() {
		final Transition superTransition = super.run();
		if (superTransition != null) return superTransition;

		final Transition transition;
		if (this.direction.gyroCorrect(this.targetHeading * this.angleMultiplier, 1.0, this.configuration.imu.getRelativeYaw(), 0.05, 0.1) > 10) transition = NEXT_STATE;
		else transition = null;
		this.configuration.move(this.direction, 0.06);

		return transition;
	}
}
