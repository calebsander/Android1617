//Represents a desired set of movement with a variety of input methods that compute to x, y, and spin powers
package org.gearticks.hardware.drive;

import org.gearticks.MathSupplement;

public class DriveDirection {
	//Horizontal movement relative to the robot (positive is to the right)
	private double x;
	//Vertical movement relative to the robot (positive is forwards)
	private double y;
	//Spin movement (positive is clockwise)
	private double s;

	//For gyroCorrect()
	private double numOffGyro;
	private int correctCount;

	public DriveDirection() {
		this.x = 0.0;
		this.y = 0.0;
		this.s = 0.0;

		this.numOffGyro = 0.0;
		this.correctCount = 0;
	}

	//Sets desired direction as coefficients of unit vectors
	public void drive(double x, double y) {
		this.x = x;
		this.y = y;
	}
	//Adds another movement vector to the drive vector
	public void incrementDrive(double x, double y) {
		this.x += x;
		this.y += y;
	}
	//Sets desired spin power
	public void turn(double s) {
		this.s = s;
	}
	//Adds spin to the spin power
	public void incrementTurn(double s) {
		this.s += s;
	}
	//Indicates no desired movement
	public void stopDrive() {
		this.x = 0.0;
		this.y = 0.0;
		this.s = 0.0;
	}
	//Rotates x and y values based on desired movement direction and current heading
	public void correctView(double curDir) {
		this.x = this.x * MathSupplement.cosDegrees(curDir) - this.y * MathSupplement.sinDegrees(curDir);
		this.y = this.x * MathSupplement.sinDegrees(curDir) + this.y * MathSupplement.cosDegrees(curDir);
	}
	//Increments the spin variable to try to correct to a certain heading, returns how many times in a row the robot has been sufficiently close to it
	public int gyroCorrect(double gyroTarget, double gyroRange, double gyroActual, double minSpeed, double addSpeed) {
		double delta = (gyroTarget - gyroActual + 360.0) % 360.0; //the difference between target and actual mod 360
		if (delta > 180.0) delta -= 360.0; //makes delta between -180 and 180
		final double gyroMod;
		if (Math.abs(delta) > 45) gyroMod = Math.signum(delta); //set gyromod to 1 or -1 if the error is more than 45 degrees
		else gyroMod = delta / 45.0; //or set it to scale up to 1 if it's less than 45 degrees

		if (Math.abs(delta) > gyroRange) { //checks if delta is out of range
			this.numOffGyro += 0.25; //increase this correction the longer we are out of range
			if (this.numOffGyro > 25.0) this.numOffGyro = 25.0; //don't let this correction get too big
			this.correctCount = 0;
			this.turn(minSpeed * Math.signum(gyroMod) + addSpeed * gyroMod/* + this.numOffGyro * Math.signum(gyroMod) / 200.0*/);
		}
		else {
			this.numOffGyro = 0.0; //reset the time-based correction
			this.correctCount++;
			this.turn(0.0);
		}
		return this.correctCount;
	}
	//Returns the calculated x value
	public double getX() {
		return this.x;
	}
	//Returns the calculated y value
	public double getY() {
		return this.y;
	}
	//Returns the calculated s value
	public double getS() {
		return this.s;
	}
	//Returns whether no movement has been requested
	public boolean isStopped() {
		return this.x == 0.0 && this.y == 0.0 && this.s == 0.0;
	}
	public String toString() {
		return String.format("%.2f, %.2f, %.2f", this.x, this.y, this.s);
	}
}