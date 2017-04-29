//Provides several utility functions for calculating the result of a Gamepad and comparing values with the last measured ones
package org.gearticks;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;

public class GamepadWrapper {
	//Desired deadzone size
	private static final float DEADZONE = 0F;

	//Contains a reference to the current Gamepad object
	private Gamepad gamepad;
	//Contains a copy of the Gamepad object from the last loop
	private Gamepad last;

	public GamepadWrapper(Gamepad gamepad) {
		this.gamepad = gamepad;
		this.configGamepad();
		this.last = new Gamepad();
	}

	//Sets the threshold on the gamepad
	private void configGamepad() {
		this.gamepad.setJoystickDeadzone(DEADZONE);
	}
	//Returns whether the D-pad is in the up-right position
	public boolean dpadUpRight() {
		return this.gamepad.dpad_up && this.gamepad.dpad_right;
	}
	//Returns whether the D-pad is in the down-right position
	public boolean dpadDownRight() {
		return this.gamepad.dpad_down && this.gamepad.dpad_right;
	}
	//Returns whether the D-pad is in the down-left position
	public boolean dpadDownLeft() {
		return this.gamepad.dpad_down && this.gamepad.dpad_left;
	}
	//Returns whether the D-pad is in the up-left position
	public boolean dpadUpLeft() {
		return this.gamepad.dpad_up && this.gamepad.dpad_left;
	}
	//Returns whether the D-pad is not being pressed
	public boolean dpadNone() {
		return !this.gamepad.dpad_up && !this.gamepad.dpad_right && !this.gamepad.dpad_down && !this.gamepad.dpad_left;
	}
	//Returns whether the left trigger is pressed
	public boolean getLeftTrigger() {
		return this.getLeftTriggerValue() > 0.0;
	}
	//Returns whether the right trigger is pressed
	public boolean getRightTrigger() {
		return this.getRightTriggerValue() > 0.0;
	}
	//Returns whether the left stick isn't being moved
	public boolean leftStickAtRest() {
		return this.gamepad.left_stick_x == 0.0F && this.gamepad.left_stick_y == 0.0F;
	}
	//Returns whether the right stick isn't being moved
	public boolean rightStickAtRest() {
		return this.gamepad.right_stick_x == 0.0F && this.gamepad.right_stick_y == 0.0F;
	}

	//THE FOLLOWING ARE ESSENTIALLY WRAPPER METHODS FOR Gamepad
	//Returns whether the D-pad is in the up position
	public boolean dpadUp() {
		return this.gamepad.dpad_up;
	}
	//Returns whether the D-pad is in the right position
	public boolean dpadRight() {
		return this.gamepad.dpad_right;
	}
	//Returns whether the D-pad is in the down position
	public boolean dpadDown() {
		return this.gamepad.dpad_down;
	}
	//Returns whether the D-pad is in the left position
	public boolean dpadLeft() {
		return this.gamepad.dpad_left;
	}
	//Returns the value of the left gamepad's X value
	public float getLeftX() {
		return this.gamepad.left_stick_x;
	}
	//Returns the value of the right gamepad's X value
	public float getRightX() {
		return this.gamepad.right_stick_x;
	}
	//Returns the value of the left gamepad's Y value (corrected to have + mean up)
	public float getLeftY() {
		return -this.gamepad.left_stick_y;
	}
	//Returns the value of the left gamepad's Y value (corrected to have + mean up)
	public float getRightY() {
		return -this.gamepad.right_stick_y;
	}
	//Returns the value of the A button
	public boolean getA() {
		return this.gamepad.a;
	}
	//Returns the value of the B button
	public boolean getB() {
		return this.gamepad.b;
	}
	//Returns the value of the X button
	public boolean getX() {
		return this.gamepad.x;
	}
	//Returns the value of the Y button
	public boolean getY() {
		return this.gamepad.y;
	}
	//Returns the value of the left bumper
	public boolean getLeftBumper() {
		return this.gamepad.left_bumper;
	}
	//Returns the value of the right bumper
	public boolean getRightBumper() {
		return this.gamepad.right_bumper;
	}
	//Returns the value of the back button
	public boolean getBack() {
		return this.gamepad.back;
	}
	//Returns the value of the center button
	public boolean getCenter() {
		return this.gamepad.guide;
	}
	//Returns the value of the start button
	public boolean getStart() {
		return this.gamepad.start;
	}
	//Returns the value of the left trigger
	public float getLeftTriggerValue() {
		final float triggerValue = this.gamepad.left_trigger;
		if (triggerValue == 0.5F) return 0.0F;
		else return triggerValue;
	}
	//Returns the value of the right trigger
	public float getRightTriggerValue() {
		final float triggerValue = this.gamepad.right_trigger;
		if (triggerValue == 0.5F) return 0.0F;
		else return triggerValue;
	}

	//Copies over current gamepad information to the last gamepad information - should be called at the end of each loop cycle
	public void updateLast() {
		try {
			this.last.copy(this.gamepad);
		}
		catch (RobotCoreException e) {
			DbgLog.error("Unable to copy gamepad");
		}
	}
	//Returns the native gamepad object being wrapped
	public Gamepad getGamepad() {
		return this.gamepad;
	}
	//Wraps the previous gamepad and returns it
	public GamepadWrapper getLast() {
		return new GamepadWrapper(this.last);
	}
	public boolean newly(GamepadCondition condition) {
		return condition.getState(this) && !condition.getState(this.getLast());
	}

	public interface GamepadCondition {
		boolean getState(GamepadWrapper gamepad);
	}
}