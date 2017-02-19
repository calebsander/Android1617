package org.gearticks.joystickoptions;

public enum PositionOption {
	NEAR,
	FAR;

	public static final ValuesJoystickOption<PositionOption> positionOption = new ValuesJoystickOption<>("Position", values());
}