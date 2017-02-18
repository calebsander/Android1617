package org.gearticks.joystickoptions;

public enum BeaconOption {
	NONE,
	NEAR,
	FAR,
	BOTH;

	public static final ValuesJoystickOption<BeaconOption> beaconOption = new ValuesJoystickOption<>("Beacons", values());
}