//An interface for selecting a list of settings with the joystick, such as an autonomous configuration
package org.gearticks.joystickoptions;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.gearticks.GamepadWrapper;
import java.util.LinkedList;
import java.util.ListIterator;

public class JoystickOptionController {
	//The options to select
	private LinkedList<JoystickOption> options;
	//Keeps track of the current position in options
	private ListIterator<JoystickOption> optionIterator;
	//The current option being selected
	private JoystickOption currentOption;
	//The previous selections concatenated (so it is possible to see what has been selected before)
	private String previousSelections;

	public JoystickOptionController() {
		this.options = new LinkedList<>();
		this.resetIterator();
		this.previousSelections = null;
	}

	//Resets the iterator based on a new options list and removes the last currentOption selection
	private void resetIterator() {
		this.optionIterator = this.options.listIterator(0);
		this.currentOption = null;
	}
	//Goes onto the next option (updating the previous selection string as well)
	private void nextOption() {
		if (this.currentOption != null) {
			if (this.previousSelections == null) this.previousSelections = this.currentOption.getSelectedOption();
			else this.previousSelections += ", " + this.currentOption.getSelectedOption();
		}
		if (this.optionIterator.hasNext()) this.currentOption = this.optionIterator.next();
		else this.currentOption = null;
	}
	//Adds an option to the end of the option list
	public void addOption(JoystickOption option) {
		this.options.add(option);
		this.resetIterator();
		this.nextOption();
	}
	/*Updates the state of the option selector based on the gamepad readings and outputs the current information to telemetry
		This should be called once in init_loop()*/
	public void update(GamepadWrapper gamepad, Telemetry telemetry) {
		//Gamepad input response
		if (gamepad.getA() && !gamepad.getLast().getA()) this.nextOption(); //check if the next option has been requested
		if (this.currentOption != null) {
			if (gamepad.getX() && !gamepad.getLast().getX()) this.currentOption.decrementOption();
			if (gamepad.getB() && !gamepad.getLast().getB()) this.currentOption.incrementOption();
		}
		//Output state information
		if (this.previousSelections != null) telemetry.addLine("Previous choices: " + this.previousSelections);
		if (this.currentOption == null) telemetry.addLine("READY");
		else {
			telemetry.addLine("Setting: " + this.currentOption.getTitle());
			telemetry.addLine("Currently selected: " + this.currentOption.getSelectedOption());
		}
	}
	//Returns whether a given option has had its value selected yet
	public boolean hasSelectedOption(JoystickOption option) {
		final int optionIndex = this.options.indexOf(option);
		return !(optionIndex == -1 || optionIndex >= this.optionIterator.previousIndex());
	}
}