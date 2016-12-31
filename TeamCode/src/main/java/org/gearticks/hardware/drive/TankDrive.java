package org.gearticks.hardware.drive;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a tank drive.
 * Stores the drive motors on each side and calculates what power to send them.
 */
public class TankDrive extends DriveSystem {
	/**
	 * The motors on the left side of the robot
	 */
	private final Set<MotorWrapper> leftMotors;
	/**
	 * The motors on the right side of the robot
	 */
	private final Set<MotorWrapper> rightMotors;

	public TankDrive() {
		super();
		this.leftMotors = new HashSet<>();
		this.rightMotors = new HashSet<>();
	}

	/**
	 * Adds a motor to the left side of the robot
	 * @param leftMotor the motor to add
	 */
	public void addLeftMotor(MotorWrapper leftMotor) {
		this.leftMotors.add(leftMotor);
		this.addMotor(leftMotor);
	}

	/**
	 * Adds a motor to the right side of the robot
	 * @param rightMotor the motor to add
	 */
	public void addRightMotor(MotorWrapper rightMotor) {
		this.rightMotors.add(rightMotor);
		this.addMotor(rightMotor);
	}

	public void calculatePowers(DriveDirection direction) {
		//Each motor on the same side can be set the same way
		for (MotorWrapper leftMotor  : this.leftMotors ) this.setMotorPower(leftMotor,  + direction.getY() + direction.getS());
		for (MotorWrapper rightMotor : this.rightMotors) this.setMotorPower(rightMotor, - direction.getY() + direction.getS());
	}
}
