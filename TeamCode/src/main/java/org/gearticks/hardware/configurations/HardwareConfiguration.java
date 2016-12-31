package org.gearticks.hardware.configurations;

import org.gearticks.hardware.drive.DriveDirection;

/**
 * Represents a collection of hardware devices on a robot,
 * with the ability to move around the field
 */
public interface HardwareConfiguration {
	/**
	 * Stops the robot from moving.
	 * Usually this only affects the drive motors.
	 * This is useful when match has ended but OpMode hasn't.
	 */
	void stopMotion();
	/**
	 * Cleans up after the end of the OpMode.
	 * This primarily entails tearing down I2CSensors.
	 */
	void teardown();
	/**
	 * Moves the robot in the desired direction
	 * with the specified acceleration limit.
	 * Usually entails calculating powers on a {@link org.gearticks.hardware.drive.DriveSystem},
	 * scaling them down, acceleration-limiting them,
	 * and committing them.
	 * @param direction the desired movement direction
	 * @param accelLimit the acceleration limit value (see {@link org.gearticks.hardware.drive.MotorWrapper#accelLimit(double, double)})
	 * @see DriveDirection
	 */
	void move(DriveDirection direction, double accelLimit);
}