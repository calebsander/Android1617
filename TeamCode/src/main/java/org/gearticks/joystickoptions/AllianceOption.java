package org.gearticks.joystickoptions;

/**
 * An enumeration of the two possible alliance colors
 */
public enum AllianceOption {
	BLUE,
	RED;

	/**
	 * A joystick option for selecting which alliance to run on.
	 * Specifically for use in autonomous for mirroring angles, etc.
	 * Having only a single, static copy is useful
	 * because it allows it not to be passed around,
	 * and makes it "remember" its selected choice.
	 */
	public static final ValuesJoystickOption<AllianceOption> allianceOption = new ValuesJoystickOption<>("Alliance", values());
}