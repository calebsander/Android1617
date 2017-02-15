package org.gearticks.joystickoptions;

public enum ParkingOption {
	CENTER,
	CORNER;

	public static final ValuesJoystickOption<ParkingOption> parkingOption = new ValuesJoystickOption<>("Parking", values());
}