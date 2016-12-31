package org.gearticks.hardware.drive;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Represents a particular drive system with methods
 * for calculating motor powers and operating on them
 */
public abstract class DriveSystem {
	/**
	 * Maps motors to the powers they are going to be assigned
	 */
	private final Map<MotorWrapper, Double> motorPowers;

	public DriveSystem() {
		this.motorPowers = new HashMap<>();
	}
	/**
	 * Can pass in a list of motors to start with
	 * @param motors a preliminary set of motors in the drive system
	 */
	public DriveSystem(Collection<MotorWrapper> motors) {
		this();
		for (final MotorWrapper motor : motors) this.addMotor(motor);
	}

	/**
	 * Adds another motor to be tracked by the drive system
	 * @param motor the motor to add
	 */
	protected void addMotor(MotorWrapper motor) {
		this.motorPowers.put(motor, MotorWrapper.STOPPED);
	}
	/**
	 * Set the desired power of a specific motor
	 * @param motor the motor whose power to request
	 * @param power the power to request
	 */
	protected void setMotorPower(MotorWrapper motor, double power) {
		this.motorPowers.put(motor, power);
	}
	/**
	 * Calculates necessary motor powers from a desired movement,
	 * must be implemented by every subclass
	 * @param direction the desired movement direction
	 */
	public abstract void calculatePowers(DriveDirection direction);
	/**
	 * Sends calculated motor powers to the motors
	 */
	public void commitPowers() {
		for (final Entry<MotorWrapper, Double> entry : this.motorPowers.entrySet()) {
			entry.getKey().setPower(entry.getValue());
		}
	}
	/**
	 * Scales all motor powers to make the maximum power
	 * in absolute value have a specified value
	 * @param maxPower the maximum power
	 */
	public void scaleMotors(double maxPower) {
		double maxRequested = MotorWrapper.STOPPED;
		for (final Entry<MotorWrapper, Double> entry : this.motorPowers.entrySet()) {
			if (Math.abs(entry.getValue()) > maxRequested) {
				maxRequested = Math.abs(entry.getValue());
			}
		}
		for (final Entry<MotorWrapper, Double> entry : this.motorPowers.entrySet()) {
			entry.setValue(entry.getValue() * maxPower / maxRequested);
		}
	}
	/**
	 * Scales all motor powers to make the maximum power
	 * in absolute value have a specified value if it would have been exceeded
	 * @param maxPower the maximum power
	 */
	public void scaleMotorsDown(double maxPower) {
		double maxRequested = MotorWrapper.STOPPED;
		for (final Entry<MotorWrapper, Double> entry : this.motorPowers.entrySet()) {
			if (Math.abs(entry.getValue()) > maxRequested) maxRequested = Math.abs(entry.getValue());
		}
		if (maxRequested > maxPower) this.scaleMotors(maxPower);
	}
	/**
	 * Scales all motor powers down so none exceed power 1 in absolute value
	 */
	public void scaleMotorsDown() {
		this.scaleMotorsDown(1.0);
	}
	/**
	 * Prevents any motor power from changing more than a specified value since the last loop cycle
	 * @param maxDiff the maximum allowable difference in power in absolute value
	 */
	public void accelLimit(double maxDiff) {
		for (final Entry<MotorWrapper, Double> entry : this.motorPowers.entrySet()) {
			entry.setValue(MotorWrapper.accelLimit(entry.getKey().getPower(), entry.getValue(), maxDiff));
		}
	}
	/**
	 * Scales all motor powers by a constant
	 * @param scalar the constant
	 */
	public void multiply(double scalar) {
		for (final Entry<MotorWrapper, Double> entry : this.motorPowers.entrySet()) {
			entry.setValue(entry.getValue() * scalar);
		}
	}
}