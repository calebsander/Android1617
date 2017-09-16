package org.gearticks.opmodes.test.oled;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.I2cDevice;
import java.util.ArrayList;
import org.gearticks.dimsensors.i2c.MonochromeDisplay;
import org.gearticks.opmodes.BaseOpMode;

@Autonomous
@Disabled
public class OLEDSnickTest extends BaseOpMode {
	private MonochromeDisplay display;
	private int x, y;

	private enum Direction {
		UP,
		DOWN,
		LEFT,
		RIGHT
	}

	protected void initialize() {
		this.display = new MonochromeDisplay((I2cDevice)this.hardwareMap.get("oled"));
		this.x = MonochromeDisplay.WIDTH / 2;
		this.y = MonochromeDisplay.HEIGHT / 2;
	}
	protected void matchStart() {
		this.display.clear();
		this.display.display();
	}
	protected void loopAfterStart() {
		if (this.display.isReady()) {
			this.display.setPixel(this.x, this.y, true);
			final ArrayList<Direction> possibleDirections = new ArrayList<>(Direction.values().length);
			if (this.x > 0) possibleDirections.add(Direction.LEFT);
			if (this.x < MonochromeDisplay.WIDTH) possibleDirections.add(Direction.RIGHT);
			if (this.y > 0) possibleDirections.add(Direction.UP);
			if (this.y < MonochromeDisplay.HEIGHT) possibleDirections.add(Direction.DOWN);
			final Direction direction = possibleDirections.get((int)(Math.random() * possibleDirections.size()));
			switch (direction) {
				case UP:
					this.y--;
					break;
				case DOWN:
					this.y++;
					break;
				case LEFT:
					this.x--;
					break;
				case RIGHT:
					this.x++;
			}
			this.display.display();
		}
	}
}