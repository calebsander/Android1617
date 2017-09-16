package org.gearticks.components.velocity.component.drive;

import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent.DefaultTransition;
import org.gearticks.components.generic.component.OpModeComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;

@Deprecated
public class GiroDriveEncoderBeacon extends OpModeComponentHardware<VelocityConfiguration, DefaultTransition> {
	private final DriveDirection direction;
	private double power;
	private final double targetHeading;
	private long encoderTarget;
	private double angleMultiplier;

	/**
	 *
	 * @param targetHeading - between 0 and 360, input to DriveDirection.gyroCorrect
	 * @param power - between 0 and 1, input for DriveDirection
	 * @param encoderTarget - target for the encoder. If the encoderPositive exceeds this target, the component transitions
	 * @param opModeContext - the OpModeContext this is running
	 * @param id - descriptive name for logging
	 */
	public GiroDriveEncoderBeacon(double targetHeading, double power, long encoderTarget, OpModeContext<VelocityConfiguration> opModeContext, String id) {
		super(opModeContext, DefaultTransition.class, id);
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
		final double buttonAngle = 90.0 * this.angleMultiplier;
		this.direction.drive(0.0, this.power);
		this.direction.gyroCorrect(buttonAngle + this.targetHeading, 1.0, this.configuration.imu.getRelativeYaw(), 0.05, 0.1);
		this.configuration.move(this.direction, 0.06);

		if (this.configuration.encoderPositive() > this.encoderTarget) return DefaultTransition.DEFAULT;
		else return null;
	}
}
