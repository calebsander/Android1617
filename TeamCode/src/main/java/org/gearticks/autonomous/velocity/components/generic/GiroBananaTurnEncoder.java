package org.gearticks.autonomous.velocity.components.generic;

import android.util.Log;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.opmodes.utility.Utils;

public class GiroBananaTurnEncoder extends AutonomousComponentHardware<VelocityConfiguration> {
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
	public GiroBananaTurnEncoder(double startHeading, double endHeading, double power, long encoderTarget, OpModeContext<VelocityConfiguration> opModeContext, String id) {
		super(opModeContext, id);
		this.direction = new DriveDirection();
		this.power = power;
		this.turnPower = 0.05;
		this.turnAccel = 0.1;
		this.turnRange = 1.0;
		this.startHeading = startHeading;
		this.endHeading = endHeading;
		this.encoderTarget = Math.abs(encoderTarget);
		this.headingSlope = (this.endHeading - this.startHeading) / this.encoderTarget;
	}
	public GiroBananaTurnEncoder(double startHeading, double endHeading, double power, long encoderTarget, double turnPower, double turnAccel, double turnRange, OpModeContext<VelocityConfiguration> opModeContext, String id) {
		super(opModeContext, id);
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
	public Transition run() {
		final Transition superTransition = super.run();
		if (superTransition != null) return superTransition;

		final int distance = this.configuration.encoderPositive();

		final double targetHeading = getHeading(distance);

		double currentHeading = this.configuration.imu.getRelativeYaw();
		this.direction.gyroCorrect(targetHeading * this.angleMultiplier, this.turnRange, this.configuration.imu.getRelativeYaw(), this.turnPower, this.turnAccel);

		final Transition transition;
		if (distance > this.encoderTarget) {
			this.direction.drive(0.0, 0.0);
			if (this.direction.isStopped()) {
				transition = NEXT_STATE;
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

	@Override
	public void tearDown() {
		super.tearDown();
		//stop motors
		this.configuration.stopMotion();
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
