package org.gearticks.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.I2cDevice;
import org.gearticks.dimsensors.i2c.MonochromeDisplay;
import org.gearticks.opmodes.BaseOpMode;

@Autonomous
public class OLEDDisplayTest extends BaseOpMode {
	private MonochromeDisplay display;

	protected void initialize() {
		this.display = new MonochromeDisplay((I2cDevice)this.hardwareMap.get("oled"));
	}
	protected void matchStart() {
		this.display.clear();
		for (int x = 0; x < MonochromeDisplay.WIDTH; x++) {
			for (int y = 0; y < MonochromeDisplay.HEIGHT; y++) {
				if ((x + y) % 2 == 0) this.display.drawPixel(x, y, true);
			}
		}
		this.display.display();
	}
}