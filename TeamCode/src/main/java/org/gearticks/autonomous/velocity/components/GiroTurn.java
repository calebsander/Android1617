package org.gearticks.autonomous.velocity.components;

import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;

public class GiroTurn extends AutonomousComponentVelocityBase {
	private final DriveDirection direction = new DriveDirection();
	private final double targetHeading;

	/**
	 *
	 * @param targetHeading - between 0 and 360, input to DriveDirection.gyroCorrect
	 * @param configuration
	 * @param id - descriptive name for logging
	 */
	public GiroTurn(double targetHeading,  VelocityConfiguration configuration, String id) {
		super(configuration, id);
		this.targetHeading = targetHeading;
	}

	@Override
	public void setup(int inputPort) {
		super.setup(inputPort);
		this.getConfiguration().resetEncoder();
	}

	@Override
	public int run() {
		int transition = 0;
		super.run();

		if (this.direction.gyroCorrect(this.targetHeading, 1.0, this.getConfiguration().imu.getRelativeYaw(), 0.05, 0.1) > 10) {
			transition = 1;
		}
		this.getConfiguration().move(this.direction, 0.06);

		return transition;
	}

	@Override
	public void tearDown() {
		super.tearDown();
		//stop motors
		this.direction.stopDrive();
		this.getConfiguration().move(this.direction, 0.06);
	}




}
