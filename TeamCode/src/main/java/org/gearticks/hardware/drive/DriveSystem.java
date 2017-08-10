//Represents a particular drive system with methods for calculating motor powers and operating on them
package org.gearticks.hardware.drive;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class DriveSystem {
	//Maps motors to the powers they are going to be assigned
	private Map<MotorWrapper, Double> motorPowers;

	public DriveSystem() {
		this.motorPowers = new HashMap<>();
	}
	//Can pass in a list of motors to start with
	public DriveSystem(Collection<MotorWrapper> motors) {
		this();
		motors.forEach(this::addMotor);
	}

	//Add another motor to be tracked
	protected void addMotor(MotorWrapper motor) {
		this.motorPowers.put(motor, MotorWrapper.STOPPED);
	}
	//Calculates necessary motor powers from a desired movement, must be implemented by every subclass
	protected abstract Map<MotorWrapper, Double> powersFromDirection(DriveDirection direction);
	//Stores calculated motor powers
	public void calculatePowers(DriveDirection direction) {
		this.motorPowers = this.powersFromDirection(direction);
	}
	//Applies calculated motor powers
	public void commitPowers() {
		this.motorPowers.entrySet().forEach(entry ->
			entry.getKey().setPower(entry.getValue())
		);
	}
	/**
	 * Gets the maximum power in absolute value currently requested
	 * @return the power with the maximum magnitude, or 0 if no powers are set
	 */
	private double getMaxPower() {
		return this.motorPowers.values().stream()
			.map(Math::abs)
			.max(Double::compare)
			.orElse(0.0);
	}
	//Scales all motor powers to get the maximum power in absolute value at a specified value
	public void scaleMotors(double maxPower) {
		final double maxRequested = this.getMaxPower();
		this.motorPowers.entrySet().forEach(entry ->
      entry.setValue(entry.getValue() * maxPower / maxRequested)
		);
	}
	//Scales all motor powers to get the maximum power in absolute value at a specified value if it was more
	public void scaleMotorsDown(double maxPower) {
		if (this.getMaxPower() > maxPower) this.scaleMotors(maxPower);
	}
	//Scales all motor powers down to 0
	public void scaleMotorsDown() {
		this.scaleMotorsDown(1.0);
	}
	//Prevents motor powers from changing more than a specified value in one calculation
	public void accelLimit(double maxDiff) {
		this.motorPowers.entrySet().forEach(entry -> {
			final double lastPower = entry.getKey().getPower();
			entry.setValue(MotorWrapper.accelLimit(lastPower, entry.getValue(), maxDiff));
		});
	}
	//Scales all motor powers by a constant
	public void multiply(double scalar) {
		this.motorPowers.entrySet().forEach(entry ->
      entry.setValue(scalar * entry.getValue())
		);
	}
}