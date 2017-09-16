//Allows for easier addressing of the LEDs on the DIM
package org.gearticks.dimsensors;

public enum DimLed {
	BLUE(0),
	RED(1);

	public final int id;
	DimLed(int id) {
		this.id = id;
	}
}