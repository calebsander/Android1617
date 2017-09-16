package org.gearticks.opmodes.test.oled;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.I2cDevice;
import org.gearticks.dimsensors.i2c.MonochromeDisplay;
import org.gearticks.opmodes.BaseOpMode;

@Autonomous
@Disabled
public class OLEDPixelSpeed extends BaseOpMode {
	private MonochromeDisplay display;
	private int x, y;

	protected void initialize() {
		this.display = new MonochromeDisplay((I2cDevice)this.hardwareMap.get("oled"));
		this.x = this.y = 0;
	}
	protected void matchStart() {
		this.display.clear();
		this.display.display();
	}
	protected void loopAfterStart() {
		if (this.display.isReady()) {
			if (this.y == MonochromeDisplay.HEIGHT) this.requestOpModeStop();
			else {
				this.display.setPixel(this.x, this.y, true);
				this.display.display();
				this.x++;
				if (this.x == MonochromeDisplay.WIDTH) {
					this.x = 0;
					this.y++;
				}
			}
		}
	}
}
