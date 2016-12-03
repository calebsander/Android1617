package org.gearticks.joystickoptions;

public enum AllianceOption {
	BLUE,
	RED;

	public static final ValuesJoystickOption<AllianceOption> allianceOption = new ValuesJoystickOption<>("Alliance", values());
}