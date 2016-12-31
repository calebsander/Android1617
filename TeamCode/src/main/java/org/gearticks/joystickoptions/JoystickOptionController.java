package org.gearticks.joystickoptions;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.gearticks.GamepadWrapper;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * An interface for selecting a list of settings with the joystick,
 * such as autonomous configuration options
 */
public class JoystickOptionController {
	/**
	 * The options to select
	 */
	private final List<JoystickOption> options;
	/**
	 * Keeps track of the current position in the options list
	 */
	private ListIterator<JoystickOption> optionIterator;
	/**
	 * The current option being selected
	 */
	private JoystickOption currentOption;
	/**
	 * The previous selections concatenated
	 * (so it is possible to see what has been selected before).
	 * This string is displayed on the Drive Station telemetry.
	 */
	private String previousSelections;
	/**
	 * Keeps track of the current gamepad settings and the ones from the previous loop
	 */
	private GamepadWrapper gamepad;

	public JoystickOptionController() {
		this.options = new LinkedList<>();
		this.resetIterator();
		this.gamepad = null;
	}

	/**
	 * Resets the iterator based on a new options list
	 * and removes the last currentOption selection
	 */
	private void resetIterator() {
		this.optionIterator = this.options.listIterator();
		this.currentOption = null;
		this.previousSelections = null;
	}
	/**
	 * Advances to the next option (updating the previous selection string as well)
	 */
	private void nextOption() {
		if (this.currentOption != null) {
			if (this.previousSelections == null) this.previousSelections = this.currentOption.getSelectedOption();
			else this.previousSelections += ", " + this.currentOption.getSelectedOption();
		}
		if (this.optionIterator.hasNext()) this.currentOption = this.optionIterator.next();
		else this.currentOption = null;
	}

	/**
	 * Adds an option to the end of the option list
	 * @param option the option to add
	 */
	public void addOption(JoystickOption option) {
		this.options.add(option);
		this.resetIterator();
		this.nextOption();
	}

	/**
	 * Updates the state of the option selector based on the gamepad readings
	 * and outputs the current information to telemetry.
	 * This should be called once each time through init_loop().
	 * @param inputGamepad the gamepad to control the options selection
	 * @param telemetry the telemetry object to send information to
	 */
	public void update(Gamepad inputGamepad, Telemetry telemetry) {
		//Gamepad input response
		if (this.gamepad == null) {
			if (inputGamepad != null) this.gamepad = new GamepadWrapper(inputGamepad);
		}
		else {
			if (this.gamepad.getGamepad() != inputGamepad) this.gamepad = new GamepadWrapper(inputGamepad);
			if (this.gamepad.getA() && !this.gamepad.getLast().getA()) this.nextOption(); //check if the next option has been requested
			if (this.currentOption != null) {
				if (this.gamepad.getX() && !this.gamepad.getLast().getX()) this.currentOption.decrementOption();
				if (this.gamepad.getB() && !this.gamepad.getLast().getB()) this.currentOption.incrementOption();
			}
			this.gamepad.updateLast();
		}
		//Output state information
		if (this.previousSelections != null) telemetry.addLine("Previous choices: " + this.previousSelections);
		if (this.currentOption == null) telemetry.addLine("READY");
		else {
			telemetry.addLine("Setting: " + this.currentOption.getTitle());
			telemetry.addLine("Currently selected: " + this.currentOption.getSelectedOption());
		}
	}
	/**
	 * Returns whether a given option has had its value selected yet
	 * @param option the option in question
	 * @return true iff the option being selected comes after the first instance of the specified option
	 */
	public boolean hasSelectedOption(JoystickOption option) {
		final int optionIndex = this.options.indexOf(option);
		return optionIndex != -1 && optionIndex < this.optionIterator.previousIndex();
	}
}