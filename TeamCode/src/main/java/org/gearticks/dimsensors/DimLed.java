package org.gearticks.dimsensors;

import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;

/**
 * Stores the ids of the indicator LEDs on the DIM.
 * There is a red LED and a blue LED.
 */
public enum DimLed {
	BLUE(0),
	RED(1);

	/**
	 * The channel value to pass into {@link DeviceInterfaceModule#setLED(int, boolean)}
	 */
	public final int id;
	DimLed(int id) {
		this.id = id;
	}
}