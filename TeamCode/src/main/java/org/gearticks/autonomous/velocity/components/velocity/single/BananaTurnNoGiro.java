package org.gearticks.autonomous.velocity.components.velocity.single;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;

public class BananaTurnNoGiro extends AutonomousComponentHardware<VelocityConfiguration> {
	//Width of robot in encoder ticks
	private static final double HALF_W = 2100 / 2.0;
	//Proportionality of encoder error to s_additional/s_0
	private static final double K = 0.25;
	//Maximum absolute value of s_additional/s_0 (K * error)
	private static final double MAX_S_OVER_S0 = 1.0;

	private double theta;
	private final double endHeading;
	private final double y_0;
	private final double d;
	private double angleMultiplier;
	private double s_0;
	private final DriveDirection direction;

	public BananaTurnNoGiro(double endHeading, double power, int encoderTarget, OpModeContext<VelocityConfiguration> opModeContext, String id) {
		super(opModeContext, id);
		if (encoderTarget == 0) throw new RuntimeException("encoderTarget == 0; use GiroTurn instead");
		this.endHeading = endHeading;
		this.y_0 = power;
		this.d = Math.abs(encoderTarget);
		this.direction = new DriveDirection();
	}

	@Override
	public void setup() {
		super.setup();
		final boolean allianceColorIsBlue = AllianceOption.allianceOption.getRawSelectedOption() == AllianceOption.BLUE;
		if (allianceColorIsBlue) this.angleMultiplier = 1.0; //angles were calculated for blue side
		else this.angleMultiplier = -1.0; //invert all angles for red side
		this.configuration.resetEncoder();
		//Correction is based off actual start heading, so even if we are off at the start,
		//We'll still end up at the right heading
		final double startHeading = this.configuration.imu.getRelativeYaw();
		this.theta = Math.toRadians(this.endHeading - startHeading);
		this.s_0 = this.y_0 * HALF_W * this.theta / this.d;
	}

	@Override
	public Transition run() {
		final Transition superTransition = super.run();
		if (superTransition != null) return superTransition;

		final int dPrime = this.configuration.encoderPositive();
		if (dPrime > this.d) {
			this.direction.drive(0.0, 0.0);
			this.direction.gyroCorrect(this.endHeading * this.angleMultiplier, 1.0, this.configuration.imu.getRelativeYaw(), 0.1, 0.1);
		}
		else {
			this.direction.drive(0.0, this.y_0);
			final double thetaPrime = (dPrime / this.d) * this.theta * this.angleMultiplier;
			final double dPrimeLeft = dPrime + HALF_W * thetaPrime;
			final double error = dPrimeLeft - this.configuration.driveLeft.encoderValue();
			double sOverS_0 = K * error;
			if (Math.abs(sOverS_0) > 1.0) sOverS_0 = MAX_S_OVER_S0 * Math.signum(sOverS_0); //cap at +/- MAX_S_OVER_S0
			final double s = this.s_0 + sOverS_0 * this.s_0;
			this.direction.turn(s);
		}
		this.configuration.move(this.direction, 0.06);
		if (this.direction.isStopped()) return NEXT_STATE;
		else return null;
	}
}
