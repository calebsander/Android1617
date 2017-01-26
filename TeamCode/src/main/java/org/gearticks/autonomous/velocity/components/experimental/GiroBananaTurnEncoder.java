package org.gearticks.autonomous.velocity.components.experimental;

import android.support.annotation.NonNull;
import android.util.Log;

import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.opmodes.utility.Utils;

public class GiroBananaTurnEncoder extends AutonomousComponentHardware<VelocityConfiguration> {
	private final DriveDirection direction;
	private final double power;
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
     * @param configuration
     * @param id - descriptive name for logging
     */
	public GiroBananaTurnEncoder(double startHeading, double endHeading, double power, long encoderTarget, @NonNull VelocityConfiguration configuration, String id) {
		super(configuration, id);
		this.direction = new DriveDirection();
		this.power = power;
		this.startHeading = startHeading;
		this.endHeading = endHeading;
		this.encoderTarget = encoderTarget;
		if (this.encoderTarget != 0) {
			this.headingSlope = (this.endHeading - this.startHeading) / this.encoderTarget;
		}
		else {
			this.headingSlope = 0;
		}
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
	public int run() {
		final int superTransition = super.run();
		if (superTransition != NOT_DONE) return superTransition;

		int transition = NOT_DONE;

		final double distance = this.configuration.signedEncoder();

		final double targetHeading = getHeading(distance);


		this.direction.drive(0.0, this.power);
		double currentHeading = this.configuration.imu.getRelativeYaw();
		this.direction.gyroCorrect(targetHeading * this.angleMultiplier, 1.0, this.configuration.imu.getRelativeYaw(), 0.05, 0.1);
		this.configuration.move(this.direction, 0.06);

		if (distance > this.encoderTarget) {
			this.direction.drive(0.0, 0);
			if (Math.abs(currentHeading - this.endHeading) < 10){
				transition = NEXT_STATE;
			}
			else{
				//keep rotating until turn completed
				transition = NOT_DONE;
			}
		}
		else transition = NOT_DONE;
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

	private double getHeading(double distance){
		if (this.endHeading == 0){
			return this.endHeading;
		}
		else {
			return this.startHeading + distance * this.headingSlope;
		}
	}
}
