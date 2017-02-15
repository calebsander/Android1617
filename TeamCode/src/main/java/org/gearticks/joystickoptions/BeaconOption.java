package org.gearticks.joystickoptions;

public enum BeaconOption {
	NO_BEACONS,
	NEAR_BEACON,
	FAR_BEACON,
	BOTH_BEACONS;

	public static final ValuesJoystickOption<BeaconOption> beaconOption = new ValuesJoystickOption<>("Beacon", values());
}