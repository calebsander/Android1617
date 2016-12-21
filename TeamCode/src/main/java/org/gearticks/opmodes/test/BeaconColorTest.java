package org.gearticks.opmodes.test;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;
import java.util.concurrent.BlockingQueue;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CloseableFrame;
import org.gearticks.VuforiaKey;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp
@Disabled
public class BeaconColorTest extends BaseOpMode {
	private static final int CROP_WIDTH = 600, CROP_HEIGHT = 350;
	private static final int CROP_LEFT_X = 275;
	private static final double SCALE_FACTOR = 0.4;
	private static final int SCALED_WIDTH = (int)(CROP_WIDTH * SCALE_FACTOR), SCALED_HEIGHT = (int)(CROP_HEIGHT * SCALE_FACTOR);
	private static final int HALF_WIDTH = SCALED_WIDTH / 2;
	private BlockingQueue<CloseableFrame> frameQueue;

	protected void initialize() {
		final VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
		parameters.vuforiaLicenseKey = VuforiaKey.KEY;
		parameters.cameraDirection = CameraDirection.FRONT;
		final VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(parameters);
		Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
		vuforia.setFrameQueueCapacity(1);
		this.frameQueue = vuforia.getFrameQueue();
	}
	protected void loopAfterStart() {
		CloseableFrame frame = null;
		try { frame = this.frameQueue.take(); }
		catch (InterruptedException e) {}
		if (frame != null) {
			final long images = frame.getNumImages();
			Bitmap bitmap = null;
			for (int i = 0; i < images; i++) {
				final Image image = frame.getImage(i);
				if (image.getFormat() == PIXEL_FORMAT.RGB565) {
					bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Config.RGB_565);
					bitmap.copyPixelsFromBuffer(image.getPixels());
					break;
				}
			}
			frame.close();
			bitmap = Bitmap.createBitmap(bitmap, CROP_LEFT_X, 0, CROP_WIDTH, CROP_HEIGHT); //crop to beacon
			bitmap = Bitmap.createScaledBitmap(bitmap, SCALED_WIDTH, SCALED_HEIGHT, false); //scale down to decrease processing time
			int leftRed = 0, leftBlue = 0;
			int rightRed = 0, rightBlue = 0;
			for (int y = 0; y < SCALED_HEIGHT; y++) {
				for (int x = 0; x < HALF_WIDTH; x++) {
					final int pixel = bitmap.getPixel(x, y);
					leftRed += Color.red(pixel);
					leftBlue += Color.blue(pixel);
				}
				for (int x = HALF_WIDTH; x < SCALED_WIDTH; x++) {
					final int pixel = bitmap.getPixel(x, y);
					rightRed += Color.red(pixel);
					rightBlue += Color.blue(pixel);
				}
			}
			this.telemetry.addData("Left red", leftRed);
			this.telemetry.addData("Right red", rightRed);
			this.telemetry.addData("Left blue", leftBlue);
			this.telemetry.addData("Right blue", rightBlue);
			final boolean beaconBlueLeft = leftRed + rightBlue < rightRed + leftBlue;
			this.telemetry.addData("Sides", beaconBlueLeft ? "BLUE RED" : "RED BLUE");
		}
	}
}