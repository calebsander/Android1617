//A joystick option with a set number of string values
package org.gearticks.joystickoptions;

public class ValuesJoystickOption<E> implements JoystickOption {
	//The name of the setting
	private final String title;
	//The possible options (in order)
	private final E[] options;
	//The index of the currently selected option in this.options
	private int selectedOption;

	public ValuesJoystickOption(String title, E[] options) {
		this.title = title;
		this.options = options;
		this.selectedOption = 0;
	}

	//Select the specified option (useful for setting defaults)
	public void selectOption(E selected) {
		if (selected == null) return;
		for (int option = 0; option < this.options.length; option++) {
			if (this.options[option].equals(selected)) {
				this.selectedOption = option;
				return;
			}
		}
		this.selectedOption = 0; //if no matching option is found
	}
	//Move upwards in the option list unless at the end
	public void incrementOption() {
		if (this.selectedOption != this.options.length - 1) this.selectedOption++;
	}
	//Move downwards in the option list unless at the start
	public void decrementOption() {
		if (this.selectedOption != 0) this.selectedOption--;
	}
	//Get the title of the option selector
	public String getTitle() {
		return this.title;
	}
	//Get the string value of the currently selected option
	public String getSelectedOption() {
		return String.valueOf(this.getRawSelectedOption());
	}
	//Get the raw value of the currently selected option
	public E getRawSelectedOption() {
		return this.options[this.selectedOption];
	}
}