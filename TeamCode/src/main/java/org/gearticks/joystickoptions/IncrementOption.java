//A joystick option with values that range from a min to a max with a certain step size
package org.gearticks.joystickoptions;

public class IncrementOption implements JoystickOption {
	private static final double MIN_VALUE = 0.0;
	private static final double DEFAULT_INCREMENT = 1.0;
	private final String title;
	private final double maxSeconds;
	private final double incrementAmount;
	private double currentAmount;

	public IncrementOption(String title, double maxSeconds, double incrementAmount) {
		this.title = title;
		this.maxSeconds = maxSeconds;
		this.incrementAmount = incrementAmount;
		this.currentAmount = MIN_VALUE;
	}
	public IncrementOption(String title, double maxSeconds) {
		this(title, maxSeconds, DEFAULT_INCREMENT);
	}

	public void decrementOption() {
		this.currentAmount = Math.max(this.currentAmount - this.incrementAmount, MIN_VALUE);
	}
	public void incrementOption() {
		this.currentAmount = Math.min(this.currentAmount + this.incrementAmount, this.maxSeconds);
	}
	public String getTitle() {
		return this.title;
	}
	public String getSelectedOption() {
		return Double.toString(this.currentAmount);
	}
	//Gets the current selected value
	public double getValue() {
		return this.currentAmount;
	}
}