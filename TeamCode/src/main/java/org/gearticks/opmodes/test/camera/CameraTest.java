package org.gearticks.opmodes.test.camera;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.gearticks.InternalCamera;
import org.gearticks.InternalCamera.Image;
import org.gearticks.opmodes.BaseOpMode;

@Autonomous(name = "Camera Test", group = "test")
@Disabled
public class CameraTest extends BaseOpMode {
	private InternalCamera camera;

	protected void initialize() {
		this.camera = new InternalCamera();
	}
	protected void loopAfterStart() {
		if (this.camera.hasImage()) {
			final Image image = this.camera.getImage();
			final int width = image.getWidth(), height = image.getHeight();
			int blueSum = 0;
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) blueSum += image.getPixel(x, y).getBlue();
			}
			this.telemetry.addData("Blue", Integer.toString(blueSum));
		}
	}
	protected void matchEnd() {
		this.camera.release();
	}
}