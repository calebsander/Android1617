/*Stores the various choices for a setting with a certain name
	Usage:
	-Create option
	-Add option to controller
	-Select option with joystick using option controller
	-Get the selected value from the option*/
package org.gearticks.joystickoptions;

public interface JoystickOption {
	//Move downwards in the option list unless at the start
	void decrementOption();
	//Move upwards in the option list unless at the end
	void incrementOption();
	//Get the title of the option selector
	String getTitle();
	//Get the value of the currently selected option
	String getSelectedOption();
}