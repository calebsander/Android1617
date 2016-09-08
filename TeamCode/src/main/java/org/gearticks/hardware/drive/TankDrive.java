//Represents a tank drive - stores the motors on each side and can calculate what power to set them at
package org.gearticks.hardware.drive;

import java.util.HashSet;

public class TankDrive extends DriveSystem {
	//The motors on the left and right sides of the robot
	private HashSet<MotorWrapper> leftMotors, rightMotors;
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

	public void calculatePowers(DriveDirection direction) {
		//Each motor on the same side can be set the same way
		for (MotorWrapper leftMotor  : this.leftMotors ) this.setMotorPower(leftMotor,  + direction.getY() + direction.getS());
		for (MotorWrapper rightMotor : this.rightMotors) this.setMotorPower(rightMotor, - direction.getY() + direction.getS());
	}
}
