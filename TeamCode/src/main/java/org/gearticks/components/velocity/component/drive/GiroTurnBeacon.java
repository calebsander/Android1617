package org.gearticks.components.velocity.component.drive;

import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent.DefaultTransition;
import org.gearticks.components.generic.component.OpModeComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;

@Deprecated
public class GiroTurnBeacon extends OpModeComponentHardware<VelocityConfiguration, DefaultTransition> {
	private final DriveDirection direction = new DriveDirection();
	private final double targetHeading;
	private double angleMultiplier;

	/**
	 * @param targetHeading - between 0 and 360, input to DriveDirection.gyroCorrect
	 * @param opModeContext - the OpModeContext this is running in
	 * @param id - descriptive name for logging
	 */
	public GiroTurnBeacon(double targetHeading, OpModeContext<VelocityConfiguration> opModeContext, String id) {
		super(opModeContext, DefaultTransition.class, id);
		this.targetHeading = targetHeading;
	}

	@Override
	public void setup() {
		super.setup();
		final boolean allianceColorIsBlue = AllianceOption.allianceOption.getRawSelectedOption() == AllianceOption.BLUE;
		if (allianceColorIsBlue) this.angleMultiplier = 1.0; //angles were calculated for blue side
		else this.angleMultiplier = -1.0; //invert all angles for red side
		this.configuration.resetEncoder();
	}

	@Override
	public DefaultTransition run() {
		final DefaultTransition superTransition = super.run();
		if (superTransition != null) return superTransition;

		final boolean done = this.direction.gyroCorrect(90.0 * this.angleMultiplier + this.targetHeading, 1.0, this.configuration.imu.getRelativeYaw(), 0.05, 0.1) > 10;
		this.configuration.move(this.direction, 0.06);

		if (done) return DefaultTransition.DEFAULT;
		else return null;
	}
}
