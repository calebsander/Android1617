package org.gearticks.hardware.configurations;

import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.hardware.drive.MotorWrapper;

public interface HardwareConfiguration {
	/**
	 * What to do to stop the robot moving
	 * (for when match has ended but OpMode hasn't)
	 */
	void stopMotion();
	/**
	 * Carries out all necessary actions to clean up at end of OpMode,
	 * e.g. terminating I2C sensors
	 */
	void tearDown();
	/**
	 * Sets the motor powers necessary to move in the necessary direction,
	 * with an acceleration limit.
	 * Should handle all necessary power scaling
	 * @param direction the desired movement direction
	 * @param accelLimit the acceleration limit value (or {@link MotorWrapper#NO_ACCEL_LIMIT}
	 */
	void move(DriveDirection direction, double accelLimit);
}