package org.gearticks.autonomous.velocity.components.velocity.single;

import android.util.Log;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.opmodes.utility.Utils;

public class BananaTurnNoGiro extends AutonomousComponentHardware<VelocityConfiguration> {
	//Width of robot in encoder ticks
	private static final double W = 1000;
	//Proportionality of encoder error to s/y
	private static final double K = 1.0 / 50;
	//Minimum s power to move
	private static final double MIN_S = 0.05;

	private final double theta;
	private final double endHeading;
	private final double power;
	private final double d;
	private double angleMultiplier;
	private final DriveDirection direction;

	public BananaTurnNoGiro(double startHeading, double endHeading, double power, int encoderTarget, OpModeContext<VelocityConfiguration> opModeContext, String id) {
		super(opModeContext, id);
		if (encoderTarget == 0) throw new RuntimeException("encoderTarget == 0; use GiroTurn instead");
		this.theta = Math.toRadians(endHeading - startHeading);
		this.endHeading = endHeading;
		this.power = power;
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
	}

	@Override
	public Transition run() {
		final Transition superTransition = super.run();
		if (superTransition != null) return superTransition;

		final int dPrime = this.configuration.encoderPositive();
		final double thetaPrime = (dPrime / this.d) * this.theta * this.angleMultiplier;
		final double dPrimeLeft = dPrime + (W / 2.0) * thetaPrime;
		final double error = dPrimeLeft - this.configuration.driveLeft.encoderValue();
		final double sOverY = Math.min(K * error, 1.0);
		Log.d(Utils.TAG, "dPrimeLeft: " + dPrimeLeft);
		Log.d(Utils.TAG, "error: " + error);

		if (dPrime > this.d) {
			this.direction.drive(0.0, 0.0);
			this.direction.gyroCorrect(this.endHeading * this.angleMultiplier, 1.0, this.configuration.imu.getRelativeYaw(), MIN_S, 0.1);
		}
		else {
			this.direction.drive(0.0, this.power);
			final double s = sOverY * this.power; //TODO: should not default to 0
			this.direction.turn(s + Math.signum(s) * MIN_S);
		}
		this.configuration.move(this.direction, 0.06);
		if (this.direction.isStopped()) return NEXT_STATE;
		else return null;
	}
}
