package org.gearticks.joystickoptions;

/**
 * Stores the various choices for a named setting
 * Usage:<br>
 * -Create option<br>
 * -Add option to controller<br>
 * -Select option with joystick using option controller<br>
 * -Get the selected value from the option
 */
public interface JoystickOption {
	/**
	 * Move to lower indices in the option list unless at the start
	 */
	void decrementOption();
	/**
	 * Move to higher indices in the option list unless at the start
	 */
	void incrementOption();
	/**
	 * Get the display name of this option
	 * @return the title to display on the Drive Station telemetry
	 */
	String getTitle();
	/**
	 * Get the value of the currently selected value as a string
	 * @return a displayable value for the currently selected value
	 */
	String getSelectedOption();
}