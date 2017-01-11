package org.gearticks.opmodes.components;

import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;

public class GyroTurnComponent extends VelocityMovementComponent {
	private final VelocityMovementComponent mainMovement;
	private final double gyroTarget, gyroRange;
	private final double minSpeed, addSpeed;
	private final int correctThreshold;
	private int correctCount;

	private GyroTurnComponent(VelocityConfiguration configuration, VelocityMovementComponent mainMovement, double gyroTarget, double gyroRange, double minSpeed, double addSpeed, int correctThreshold) {
		super(configuration);
		this.mainMovement = mainMovement;
		this.gyroTarget = gyroTarget;
		this.gyroRange = gyroRange;
		this.minSpeed = minSpeed;
		this.addSpeed = addSpeed;
		this.correctThreshold = correctThreshold;
		this.correctCount = 0;
	}
	public GyroTurnComponent(VelocityConfiguration configuration, VelocityMovementComponent mainMovement, double gyroTarget, double gyroRange, double minSpeed, double addSpeed) {
		this(configuration, mainMovement, gyroTarget, gyroRange, minSpeed, addSpeed, 0); //correctThreshold doesn't matter
	}
	public GyroTurnComponent(VelocityConfiguration configuration, double gyroTarget, double gyroRange, double minSpeed, double addSpeed, int correctThreshold) {
		this(configuration, null, gyroTarget, gyroRange, minSpeed, addSpeed, correctThreshold);
	}

	public void init() {
		if (this.mainMovement != null) this.mainMovement.init();
	}
	protected void setMovementDirection(DriveDirection direction) {
		if (this.mainMovement != null) this.mainMovement.setMovementDirection(direction);
		this.correctCount = direction.gyroCorrect(this.gyroTarget, this.gyroRange, this.configuration.imu.getRelativeYaw(), this.minSpeed, this.addSpeed);
	}
	protected boolean isDone() {
		if (this.mainMovement == null) {
			return this.correctCount >= this.correctThreshold;
		}
		else {
			return this.mainMovement.isDone();
		}
	}
}