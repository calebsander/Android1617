package org.gearticks.components.velocity.component.experimental;

import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent;
import org.gearticks.components.generic.component.OpModeComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;

/**
 * Created by BenMorris on 5/9/2017.
 */

public class BananaTurn extends OpModeComponentHardware<VelocityConfiguration, OpModeComponent.DefaultTransition> {
	private final double startHeading;
	private final double endHeading;
	private final double theta;
	private final double power;
	private final int distance;
	private final int robotWidth = 1800;
	private double angleMultiplier;
	private final DriveDirection direction;
	private final OpModeContext<VelocityConfiguration> opModeContext;


	public BananaTurn(double endHeading, double power, int distance, OpModeContext<VelocityConfiguration> opModeContext, String id) {
		super(opModeContext, DefaultTransition.class, id);
		this.startHeading = this.configuration.imu.getRelativeYaw();
		this.endHeading = endHeading;
		this.theta = Math.toRadians(((this.endHeading - this.startHeading) % 360 + 540) % 360 - 180);
		this.power = power;
		this.distance = distance;
		this.direction = new DriveDirection();
		this.opModeContext = opModeContext;
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

		int currentDistance = this.configuration.encoderPositive();
		if(currentDistance >= this.distance) {
			this.direction.drive(0.0, 0.0);
			//this.direction.gyroCorrect(this.endHeading * this.angleMultiplier, 1.0, this.configuration.imu.getRelativeYaw(), 0.1, 0.1);
			this.direction.turn(0.0);
			return DefaultTransition.DEFAULT;
		} else {
			this.direction.drive(0.0, this.power);
			this.direction.turn(this.angleMultiplier * this.robotWidth / 2.0 * this.theta * this.power / this.distance);
			final double ratio = (this.distance + this.robotWidth * this.theta / 2.0) / (this.distance - this.robotWidth * this.theta / 2.0);
			final double mean = 0.06;
			double[] accelLimit = new double[2];
			accelLimit[1] = 2.0 * mean - 2.0 * mean / (ratio + 1.0); //left motor
			accelLimit[0] = accelLimit[1] / ratio;              //right motor
			this.opModeContext.telemetry.addData("accelLimit[0] (left) ", accelLimit[0]);
			this.opModeContext.telemetry.addData("accelLimit[1] (right) ", accelLimit[1]);
			this.opModeContext.telemetry.addData("Ratio ", ratio);

			//double accelLimit = 0.06;
			this.configuration.move(this.direction, accelLimit);
			return null;
		}
	}
}