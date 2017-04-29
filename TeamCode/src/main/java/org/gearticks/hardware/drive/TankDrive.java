//Represents a tank drive - stores the motors on each side and can calculate what power to set them at
package org.gearticks.hardware.drive;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TankDrive extends DriveSystem {
	//The motors on the left and right sides of the robot
	private Set<MotorWrapper> leftMotors, rightMotors;
	//The motors to coast when we want to only drive with a few motors

	public TankDrive() {
		super();
		this.leftMotors = new HashSet<>();
		this.rightMotors = new HashSet<>();
	}

	//Adds a motor to the left side of the robot
	public void addLeftMotor(MotorWrapper leftMotor) {
		this.leftMotors.add(leftMotor);
		this.addMotor(leftMotor);
	}
	//Adds a motor to the right side of the robot
	public void addRightMotor(MotorWrapper rightMotor) {
		this.rightMotors.add(rightMotor);
		this.addMotor(rightMotor);
	}

	protected Map<MotorWrapper, Double> powersFromDirection(DriveDirection direction) {
		final Map<MotorWrapper, Double> powers = new HashMap<>();
		//Each motor on the same side can be set the same way
		for (MotorWrapper leftMotor  : this.leftMotors ) powers.put(leftMotor,  + direction.getY() + direction.getS());
		for (MotorWrapper rightMotor : this.rightMotors) powers.put(rightMotor, - direction.getY() + direction.getS());
		return powers;
	}
}
