package org.gearticks.joystickoptions;

/**
 * A joystick option with values that range from a min to a max with a certain step size.
 * Typically used for setting delays in autonomous.
 */
public class IncrementOption implements JoystickOption {
	/**
	 * The minimum value for any instance: 0
	 */
	private static final double MIN_VALUE = 0.0;
	/**
	 * The default amount to increment by: 1
	 */
	private static final double DEFAULT_INCREMENT = 1.0;
	/**
	 * The display name of this option
	 */
	private final String title;
	/**
	 * The maximum value of this option
	 */
	private final double maxValue;
	/**
	 * The amount to add to or subtract from {@link #currentAmount}
	 * when changing the value with the joysticks.
	 */
	private final double incrementAmount;
	/**
	 * The current value of the option
	 */
	private double currentAmount;

	/**
	 * @param title the display name of this option
	 * @param maxValue the maximum reachable value
	 * @param incrementAmount the amount to increment or decrement the current value by with each button press
	 */
	public IncrementOption(String title, double maxValue, double incrementAmount) {
		this.title = title;
		this.maxValue = maxValue;
		this.incrementAmount = incrementAmount;
		this.currentAmount = MIN_VALUE;
	}

	/**
	 * Sets the increment amount to 1
	 * @param title the display name of this option
	 * @param maxValue the maximum reachable value
	 */
	public IncrementOption(String title, double maxValue) {
		this(title, maxValue, DEFAULT_INCREMENT);
	}

	public void decrementOption() {
		this.currentAmount = Math.max(this.currentAmount - this.incrementAmount, MIN_VALUE);
	}
	public void incrementOption() {
		this.currentAmount = Math.min(this.currentAmount + this.incrementAmount, this.maxValue);
	}
	public String getTitle() {
		return this.title;
	}
	public String getSelectedOption() {
		return Double.toString(this.currentAmount);
	}
	/**
	 * Gets the current selected value as a double
	 * @return the current value
	 */
	public double getValue() {
		return this.currentAmount;
	}
}