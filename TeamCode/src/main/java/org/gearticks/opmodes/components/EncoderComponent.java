package org.gearticks.opmodes.components;

import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;

public class EncoderComponent extends VelocityMovementComponent {
	private final int distance;
	private final double yPower;

	public EncoderComponent(VelocityConfiguration configuration, int distance, double yPower) {
		super(configuration);
		this.distance = distance;
		this.yPower = yPower;
	}

	public void init() {
		this.configuration.resetEncoder();
	}
	protected void setMovementDirection(DriveDirection direction) {
		direction.drive(0.0, this.yPower);
	}
	protected boolean isDone() {
		return Math.abs(this.configuration.encoderPositive()) > this.distance;
	}
}