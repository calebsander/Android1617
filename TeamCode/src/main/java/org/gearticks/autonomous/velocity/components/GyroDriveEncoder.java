package org.gearticks.autonomous.velocity.components;

import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;

public class GyroDriveEncoder extends AutonomousComponentVelocityBase {
	private final DriveDirection direction;
	private final double power;
	private final double targetHeading;
	private final long encoderTarget;

	/**
	 *
	 * @param targetHeading - between 0 and 360, input to DriveDirection.gyroCorrect
	 * @param power - between 0 and 1, input for DriveDirection
	 * @param encoderTarget - target for the encoder. If the encoderPositive exceeds this target, the component transitions
	 * @param configuration
	 * @param id - descriptive name for logging
	 */
	public GyroDriveEncoder(double targetHeading, double power, long encoderTarget, VelocityConfiguration configuration, String id) {
		super(configuration, id);
		this.direction = new DriveDirection();
		this.power = power;
		this.targetHeading = targetHeading;
		this.encoderTarget = encoderTarget;
	}

	@Override
	public void setup() {
		super.setup();
		this.configuration.resetEncoder();
	}

	@Override
	public int run() {
		final int superTransition = super.run();
		if (superTransition != NOT_DONE) return superTransition;

		//control giro drive
		this.direction.drive(0.0, this.power);
		this.direction.gyroCorrect(this.targetHeading, 1.0, this.configuration.imu.getRelativeYaw(), 0.05, 0.1);
		this.configuration.move(this.direction, 0.06);

		if (this.configuration.encoderPositive() > this.encoderTarget) return NEXT_STATE;
		else return NOT_DONE;
	}
}
