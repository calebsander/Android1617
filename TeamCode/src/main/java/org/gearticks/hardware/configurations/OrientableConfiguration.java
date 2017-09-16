package org.gearticks.hardware.configurations;

public interface OrientableConfiguration extends HardwareConfiguration {
	/**
	 * Resets the heading so the current heading is 0.0
	 */
	void resetHeading();
	/**
	 * Gets the current heading in degrees,
	 * increasing in the clockwise direction
	 * @return the current heading
	 */
	double getHeading();

	/**
	 * Resets the encoders on the drive motors
	 * so the current position is 0
	 */
	void resetEncoder();
	/**
	 * Gets a signed encoder value computed from the drive motor encoders
	 * @return positive values for forward, negative for backwards
	 */
	int signedEncoder();
	/**
	 * Gets the absolute value of the signed encoder value
	 * @return the distance (in ticks) travelled from the reset point
	 */
	int encoderPositive();
}