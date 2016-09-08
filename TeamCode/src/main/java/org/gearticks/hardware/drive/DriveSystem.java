//Represents a particular drive system with methods for calculating motor powers and operating on them
package org.gearticks.hardware.drive;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public abstract class DriveSystem {
	//Maps motors to the powers they are going to be assigned
	private HashMap<MotorWrapper, Double> motorPowers;

	public DriveSystem() {
		this.motorPowers = new HashMap<>();
	}
	//Can pass in a list of motors to start with
	public DriveSystem(HashSet<MotorWrapper> motors) {
		this.motorPowers = new HashMap<>();
		for (MotorWrapper motor : motors) this.motorPowers.put(motor, MotorWrapper.STOPPED);
	}

	//Add another motor to be tracked
	protected void addMotor(MotorWrapper motor) {
		this.motorPowers.put(motor, MotorWrapper.STOPPED);
	}
	//Set the power of a specific motor
	protected void setMotorPower(MotorWrapper motor, double power) {
		this.motorPowers.put(motor, power);
	}
	//Calculates necessary motor powers from a desired movement, must be implemented by every subclass
	public abstract void calculatePowers(DriveDirection direction);
	//Applies calculated motor powers
	public void commitPowers() {
		for (Entry<MotorWrapper, Double> entry : this.motorPowers.entrySet()) entry.getKey().setPower(entry.getValue());
	}
	//Scales all motor powers to get the maximum power in absolute value at a specified value
	public void scaleMotors(double maxPower) {
		double maxRequested = MotorWrapper.STOPPED;
		for (Entry<MotorWrapper, Double> entry : this.motorPowers.entrySet()) {
			if (Math.abs(entry.getValue()) > maxRequested) maxRequested = Math.abs(entry.getValue());
		}
		for (Entry<MotorWrapper, Double> entry : this.motorPowers.entrySet()) entry.setValue(entry.getValue() * maxPower / maxRequested);
	}
	//Scales all motor powers to get the maximum power in absolute value at a specified value if it was more
	public void scaleMotorsDown(double maxPower) {
		double maxRequested = MotorWrapper.STOPPED;
		for (Entry<MotorWrapper, Double> entry : this.motorPowers.entrySet()) {
			if (Math.abs(entry.getValue()) > maxRequested) maxRequested = Math.abs(entry.getValue());
		}
		if (maxRequested > maxPower) this.scaleMotors(maxPower);
	}
	//Scales all motor powers down to 0
	public void scaleMotorsDown() {
		this.scaleMotorsDown(1.0);
	}
	//Prevents motor powers from changing more than a specified value in one calculation
	public void accelLimit(double maxDiff) {
		for (Entry<MotorWrapper, Double> entry : this.motorPowers.entrySet()) entry.setValue(MotorWrapper.accelLimit(entry.getKey().getPower(), entry.getValue(), maxDiff));
	}
	//Scales all motor powers by a constant
	public void multiply(double scalar) {
		for (Entry<MotorWrapper, Double> entry : this.motorPowers.entrySet()) entry.setValue(entry.getValue() * scalar);
	}
}