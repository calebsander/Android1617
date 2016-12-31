package org.gearticks.joystickoptions;

/**
 * A joystick option with a discrete number of values of a certain type
 * @param <E> the type of each value
 */
public class ValuesJoystickOption<E> implements JoystickOption {
	/**
	 * The display name of this option
	 */
	private final String title;
	/**
	 * The possible options (in order)
	 */
	private final E[] options;
	/**
	 * The index of the currently selected option in {@link #options}
	 */
	private int selectedOption;

	/**
	 * @param title the display name of this option
	 * @param options the options to select, in order (will default to the first one)
	 */
	public ValuesJoystickOption(String title, E[] options) {
		this.title = title;
		this.options = options;
		this.selectedOption = 0;
	}

	/**
	 * Select a specific option (useful for setting defaults).
	 * If the option is not in the list, selects the first option.
	 * @param selected the option to select
	 */
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
	public void incrementOption() {
		if (this.selectedOption != this.options.length - 1) this.selectedOption++;
	}
	public void decrementOption() {
		if (this.selectedOption != 0) this.selectedOption--;
	}
	public String getTitle() {
		return this.title;
	}
	public String getSelectedOption() {
		return String.valueOf(this.getRawSelectedOption());
	}
	/**
	 * Gets the raw value of the currently selected option
	 * @return the currently selected option
	 */
	public E getRawSelectedOption() {
		return this.options[this.selectedOption];
	}
}