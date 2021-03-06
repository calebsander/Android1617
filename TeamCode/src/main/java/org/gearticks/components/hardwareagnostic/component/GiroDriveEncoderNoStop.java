package org.gearticks.components.hardwareagnostic.component;

import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent.DefaultTransition;
import org.gearticks.components.generic.component.OpModeComponentAbstract;
import org.gearticks.hardware.configurations.OrientableConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;

public class GiroDriveEncoderNoStop extends OpModeComponentAbstract<DefaultTransition> {
	private final OrientableConfiguration configuration;
	private final DriveDirection direction;
	private final double power;
	private final double targetHeading;
	private double angleMultiplier;
	private final int encoderTarget;

	/**
	 * @param targetHeading - between 0 and 360, input to DriveDirection.gyroCorrect
	 * @param power - between 0 and 1, input for DriveDirection
	 * @param encoderTarget - target for the encoder. If the encoderPositive exceeds this target, the component transitions
	 * @param opModeContext - the OpModeContext this is running in
	 * @param id - descriptive name for logging
	 */
	public GiroDriveEncoderNoStop(double targetHeading, double power, int encoderTarget, OpModeContext<? extends OrientableConfiguration> opModeContext, String id) {
		super(DefaultTransition.class, id);
		this.configuration = opModeContext.configuration;
		this.direction = new DriveDirection();
		this.power = power;
		this.targetHeading = targetHeading;
		this.encoderTarget = encoderTarget;
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

		//control giro drive
		this.direction.drive(0.0, this.power);
		this.direction.gyroCorrect(this.targetHeading * this.angleMultiplier, 1.0, this.configuration.getHeading(), 0.05, 0.1);
		this.configuration.move(this.direction, 0.06);

		if (this.configuration.encoderPositive() > this.encoderTarget) return DefaultTransition.DEFAULT;
		else return null;
	}
}