package org.gearticks.autonomous.velocity.components.generic;

import android.util.Log;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent.DefaultTransition;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.OrientableConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.Utils;

public class GiroBananaTurnEncoder extends AutonomousComponentHardware<OrientableConfiguration, DefaultTransition> {
	private final DriveDirection direction;
	private final double power;
	private final double turnPower;
	private final double turnAccel;
	private final double turnRange;
	private final double startHeading;
	private final double endHeading;
	private final double headingSlope;
	private double angleMultiplier;
	private final long encoderTarget;

    /**
     *
	 * @param startHeading - between 0 and 360, input to DriveDirection.gyroCorrect
	 * @param endHeading - between 0 and 360, input to DriveDirection.gyroCorrect
     * @param power - between 0 and 1, input for DriveDirection
     * @param encoderTarget - target for the encoder. If the encoderPositive exceeds this target, the component transitions
     * @param opModeContext - the OpModeContext this is running in
     * @param id - descriptive name for logging
     */
	public GiroBananaTurnEncoder(double startHeading, double endHeading, double power, long encoderTarget, OpModeContext<? extends OrientableConfiguration> opModeContext, String id) {
		this(startHeading, endHeading, power, encoderTarget, 0.05, 0.1, 1.0, opModeContext, id);
	}
	public GiroBananaTurnEncoder(double startHeading, double endHeading, double power, long encoderTarget, double turnPower, double turnAccel, double turnRange, OpModeContext<? extends OrientableConfiguration> opModeContext, String id) {
		super(opModeContext, DefaultTransition.class, id);
		this.direction = new DriveDirection();
		this.power = power;
		this.turnPower = turnPower;
		this.turnAccel = turnAccel;
		this.turnRange = turnRange;
		this.startHeading = startHeading;
		this.endHeading = endHeading;
		this.encoderTarget = Math.abs(encoderTarget);
		this.headingSlope = (this.endHeading - this.startHeading) / this.encoderTarget;
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

		final int distance = this.configuration.encoderPositive();

		final double targetHeading = getHeading(distance);

		double currentHeading = this.configuration.getHeading();
		this.direction.gyroCorrect(targetHeading * this.angleMultiplier, this.turnRange, currentHeading, this.turnPower, this.turnAccel);

		final DefaultTransition transition;
		if (distance > this.encoderTarget) {
			this.direction.drive(0.0, 0.0);
			if (this.direction.isStopped()) {
				transition = DefaultTransition.DEFAULT;
			}
			else { //keep rotating until turn completed
				transition = null;
			}
		}
		else {
			this.direction.drive(0.0, this.power);
			transition = null;
		}
		this.configuration.move(this.direction, 0.06);

		Log.v(Utils.TAG, "Target heading = " + targetHeading + " Distance = " + distance + " Current Heading = " + currentHeading);
		return transition;
	}

	private double getHeading(int distance) {
		if (this.encoderTarget == 0) {
			return this.endHeading;
		}
		else {
			return this.startHeading + Math.min(distance, this.encoderTarget) * this.headingSlope;
		}
	}
}
