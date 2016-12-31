package org.gearticks;

import android.util.Log;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.gearticks.opmodes.utility.Utils;

/**
 * Wraps an FTC Gamepad object. Notable changes:<br>
 * - Stores the gamepad state from the previous loop cycle for comparison<br>
 * - Getter methods rather than read/write fields<br>
 * - Utility methods (like triggers as booleans, dpadNone(), etc.)
 */
public class GamepadWrapper {
	/**
	 * The size of the deadzone (none seems necessary on top of the FTC default)
	 */
	private static final float DEADZONE = 0F;

	/**
	 * The FTC gamepad being wrapped
	 */
	private final Gamepad gamepad;
	/**
	 * A copy of the gamepad from the previous loop
	 */
	private Gamepad last;

	public GamepadWrapper(Gamepad gamepad) {
		this.gamepad = gamepad;
		this.configGamepad();
		this.last = new Gamepad();
	}

	/**
	 * Sets the deadzone on the gamepad
	 */
	private void configGamepad() {
		this.gamepad.setJoystickDeadzone(DEADZONE);
	}
	/**
	 * Returns whether the D-pad is in the up-right position
	 * @return whether the D-pad is pressed up and right
	 */
	public boolean dpadUpRight() {
		return this.gamepad.dpad_up && this.gamepad.dpad_right;
	}
	/**
	 * Returns whether the D-pad is in the down-right position
	 * @return whether the D-pad is pressed down and right
	 */
	public boolean dpadDownRight() {
		return this.gamepad.dpad_down && this.gamepad.dpad_right;
	}
	/**
	 * Returns whether the D-pad is in the down-left position
	 * @return whether the D-pad is pressed down and left
	 */
	public boolean dpadDownLeft() {
		return this.gamepad.dpad_down && this.gamepad.dpad_left;
	}
	/**
	 * Returns whether the D-pad is in the up-left position
	 * @return whether the D-pad is pressed up and left
	 */
	public boolean dpadUpLeft() {
		return this.gamepad.dpad_up && this.gamepad.dpad_left;
	}
	/**
	 * Returns whether the D-pad is not being pressed
	 * @return whether the D-pad is in the equilibrium position
	 */
	public boolean dpadNone() {
		return !(this.gamepad.dpad_up || this.gamepad.dpad_right || this.gamepad.dpad_down || this.gamepad.dpad_left);
	}
	/**
	 * Returns whether the left trigger is pressed
	 * @return true iff the left trigger is pressed at all
	 */
	public boolean getLeftTrigger() {
		return this.getLeftTriggerValue() > 0.0;
	}
	/**
	 * Returns whether the right trigger is pressed
	 * @return true iff the right trigger is pressed at all
	 */
	public boolean getRightTrigger() {
		return this.getRightTriggerValue() > 0.0;
	}
	/**
	 * Returns whether the left stick isn't being moved
	 * @return true iff the left stick is in the middle position
	 */
	public boolean leftStickAtRest() {
		return this.gamepad.left_stick_x == 0F && this.gamepad.left_stick_y == 0F;
	}
	/**
	 * Returns whether the right stick isn't being moved
	 * @return true iff the right stick is in the middle position
	 */
	public boolean rightStickAtRest() {
		return this.gamepad.right_stick_x == 0F && this.gamepad.right_stick_y == 0F;
	}

	//THE FOLLOWING ARE ESSENTIALLY WRAPPER METHODS FOR Gamepad
	/**
	 * Returns whether the D-pad is in the up position
	 * @return whether the D-pad is in the up position
	 */
	public boolean dpadUp() {
		return this.gamepad.dpad_up;
	}
	/**
	 * Returns whether the D-pad is in the right position
	 * @return whether the D-pad is in the right position
	 */
	public boolean dpadRight() {
		return this.gamepad.dpad_right;
	}
	/**
	 * Returns whether the D-pad is in the down position
	 * @return whether the D-pad is in the down position
	 */
	public boolean dpadDown() {
		return this.gamepad.dpad_down;
	}
	/**
	 * Returns whether the D-pad is in the left position
	 * @return whether the D-pad is in the left position
	 */
	public boolean dpadLeft() {
		return this.gamepad.dpad_left;
	}
	/**
	 * Returns the value of the left gamepad's X value (-1 in left position to 1 in right position)
	 * @return the value of the left gamepad's horizontal value
	 */
	public float getLeftX() {
		return this.gamepad.left_stick_x;
	}
	/**
	 * Returns the value of the right gamepad's X value (-1 in left position to 1 in right position)
	 * @return the value of the right gamepad's horizontal value
	 */
	public float getRightX() {
		return this.gamepad.right_stick_x;
	}
	/**
	 * Returns the value of the left gamepad's Y value (-1 in down position to 1 in up position)
	 * @return the value of the left gamepad's vertical value
	 */
	public float getLeftY() {
		return -this.gamepad.left_stick_y;
	}
	/**
	 * Returns the value of the right gamepad's Y value (-1 in down position to 1 in up position)
	 * @return the value of the right gamepad's vertical value
	 */
	public float getRightY() {
		return -this.gamepad.right_stick_y;
	}
	/**
	 * Returns the value of the A button
	 * @return the value of the A button
	 */
	public boolean getA() {
		return this.gamepad.a;
	}
	/**
	 * Returns the value of the B button
	 * @return the value of the B button
	 */
	public boolean getB() {
		return this.gamepad.b;
	}
	/**
	 * Returns the value of the X button
	 * @return the value of the X button
	 */
	public boolean getX() {
		return this.gamepad.x;
	}
	/**
	 * Returns the value of the Y button
	 * @return the value of the Y button
	 */
	public boolean getY() {
		return this.gamepad.y;
	}
	/**
	 * Returns the value of the left bumper
	 * @return the value of the left bumper (the top button on the left front)
	 */
	public boolean getLeftBumper() {
		return this.gamepad.left_bumper;
	}
	/**
	 * Returns the value of the right bumper
	 * @return the value of the right bumper (the top button on the right front)
	 */
	public boolean getRightBumper() {
		return this.gamepad.right_bumper;
	}
	/**
	 * Returns the value of the back button
	 * @return the value of the back button
	 */
	public boolean getBack() {
		return this.gamepad.back;
	}
	/**
	 * Returns the value of the center button
	 * @return the value of the center button
	 */
	public boolean getCenter() {
		return this.gamepad.guide;
	}
	/**
	 * Returns the value of the start button
	 * @return the value of the start button
	 */
	public boolean getStart() {
		return this.gamepad.start;
	}
	/**
	 * Returns the value of the left trigger (0 when unpressed to 1 when fully pressed)
	 * @return the value of the left trigger
	 */
	public float getLeftTriggerValue() {
		final float triggerValue = this.gamepad.left_trigger;
		if (triggerValue == 0.5F) return 0F; //there used to be a bug where the triggers could return exactly 0.5 before they were touched
		return triggerValue;
	}
	/**
	 * Returns the value of the right trigger (0 when unpressed to 1 when fully pressed)
	 * @return the value of the right trigger
	 */
	public float getRightTriggerValue() {
		final float triggerValue = this.gamepad.right_trigger;
		if (triggerValue == 0.5F) return 0F; //there used to be a bug where the triggers could return exactly 0.5 before they were touched
		return triggerValue;
	}

	/**
	 * Copies over current gamepad information to the last gamepad information.
	 * This should be called at the end of each loop cycle.
	 */
	public void updateLast() {
		try {
			this.last.copy(this.gamepad);
		}
		catch (RobotCoreException e) {
			Log.w(Utils.TAG, "Unable to copy gamepad");
		}
	}
	/**
	 * Returns the native gamepad object being wrapped
	 * @return the FTC Gamepad object this object refers to
	 */
	public Gamepad getGamepad() {
		return this.gamepad;
	}

	/**
	 * Wraps the previous gamepad and returns it
	 * @return a GamepadWrapper around the gamepad state from the previous loop cycle
	 */
	public GamepadWrapper getLast() {
		return new GamepadWrapper(this.last);
	}
}