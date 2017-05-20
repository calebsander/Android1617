package org.gearticks.autonomous.velocity.components.generic;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;

/**
 * Created by BenMorris on 5/9/2017.
 */

public class BananaTurn extends AutonomousComponentHardware<VelocityConfiguration> {
	private final double startHeading;
	private final double endHeading;
	private final double theta;
	private final double power;
	private final int distance;
	private final int width = 1800;
	private double angleMultiplier;
	private final DriveDirection direction;

	public BananaTurn(double endHeading, double power, int distance, OpModeContext<VelocityConfiguration> opModeContext, String id) {
		super(opModeContext, id);
		this.startHeading = this.configuration.imu.getRelativeYaw();
		this.endHeading = endHeading;
		this.theta = Math.toRadians(((this.endHeading - this.startHeading) % 360 + 540) % 360 - 180);
		this.power = power;
		this.distance = distance;
		this.direction = new DriveDirection();
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
	public Transition run() {
		final Transition superTransition = super.run();
		if (superTransition != null) return superTransition;

		int currentDistance = this.configuration.encoderPositive();
		if(currentDistance >= this.distance) {
			this.direction.drive(0.0, 0.0);
			this.direction.gyroCorrect(this.endHeading * this.angleMultiplier, 1.0, this.configuration.imu.getRelativeYaw(), 0.1, 0.1);
			return NEXT_STATE;
		} else {
			this.direction.drive(0.0, this.power);
			this.direction.turn(this.angleMultiplier * this.width / 2.0 * this.theta * this.power / this.distance);
			this.configuration.move(this.direction, 0.06);
			return null;
		}
	}
}
