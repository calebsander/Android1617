package org.gearticks.opmodes.test.oled;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.I2cDevice;
import org.gearticks.dimsensors.i2c.MonochromeDisplay;
import org.gearticks.opmodes.BaseOpMode;

@Autonomous
@Disabled
public class OLEDDisplayTest extends BaseOpMode {
	private MonochromeDisplay display;
	private boolean on = false;

	protected void initialize() {
		this.display = new MonochromeDisplay((I2cDevice)this.hardwareMap.get("oled"));
		this.on = false;
	}
	protected void loopAfterStart() {
		if (this.display.isReady()) {
			this.on = !this.on;
			this.display.clear();
			if (this.on) {
				for (int x = 0; x < MonochromeDisplay.WIDTH; x++) {
					for (int y = 0; y < MonochromeDisplay.HEIGHT; y++) {
						if ((x + y) % 2 == 0) this.display.setPixel(x, y, true);
					}
				}
			}
			this.display.display();
		}
	}
}