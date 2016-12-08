package org.gearticks.opmodes.components;

import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;

public abstract class VelocityMovementComponent {
	protected final VelocityConfiguration configuration;
	private final DriveDirection direction;

	public VelocityMovementComponent(VelocityConfiguration configuration) {
		this.configuration = configuration;
		this.direction = new DriveDirection();
	}

	public void loop() {
		this.setMovementDirection(this.direction);
		this.configuration.move(this.direction, 0.06);
	}
	public void stop() {
		this.configuration.stopMotion();
	}

	protected abstract void setMovementDirection(DriveDirection direction);
	protected abstract boolean isDone();
}