package org.gearticks.joystickoptions;

public enum PositionOption {
	POSITION_1,
	POSITION_2;

	public static final ValuesJoystickOption<PositionOption> positionOption = new ValuesJoystickOption<>("Position", values());
}