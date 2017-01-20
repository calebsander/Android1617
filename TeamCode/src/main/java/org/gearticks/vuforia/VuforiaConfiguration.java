package org.gearticks.vuforia;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.util.Log;
import com.vuforia.HINT;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CloseableFrame;
import org.gearticks.opmodes.units.SideOfButton;
import org.gearticks.opmodes.utility.Utils;
import java.util.concurrent.BlockingQueue;

public class VuforiaConfiguration {
    private final BlockingQueue<VuforiaLocalizer.CloseableFrame> frameQueue;
    private final VuforiaTrackables beaconImages;

    public VuforiaConfiguration() {
        Log.d("vuforia", "vuforia configuration construction");

        final VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(/*R.id.cameraMonitorViewId*/);
        parameters.vuforiaLicenseKey = VuforiaKey.KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        final VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 2);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
        Log.i(Utils.TAG, "load beacon images");
        this.beaconImages = vuforia.loadTrackablesFromAsset("FTC_2016-17");
        Log.i(Utils.TAG, "end load beacon images");
        vuforia.setFrameQueueCapacity(1);
        this.frameQueue = vuforia.getFrameQueue();
    }

    public VuforiaTrackableDefaultListener getTargetListener(String targetName) {
        return (VuforiaTrackableDefaultListener)this.beaconImages.get(VuforiaImages.getID(targetName)).getListener();
    }

    public void activate() {
        this.beaconImages.activate();
    }

    public void deactivate(){
        this.beaconImages.deactivate();
    }

    /**
     * Capture an image from Vuforia as a 720p (1280 x 720) bitmap
     * @return can be null if Vuforia didn't return a usable frame
     */
    public @Nullable Bitmap getBitmap() {
        Bitmap bitmap = null;
        CloseableFrame frame = null;
        try {
            frame = this.frameQueue.take();
        }
        catch (InterruptedException e) {
            System.err.println("Error: Vuforia frameQueue take failed");
        }
        if (frame != null) {
            final long images = frame.getNumImages();

            for (int i = 0; i < images; i++) {
                final Image image = frame.getImage(i);
                if (image.getFormat() == PIXEL_FORMAT.RGB565) {
                    bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.RGB_565);
                    bitmap.copyPixelsFromBuffer(image.getPixels());
                    break;
                }
            }
            frame.close();

        }
        else {
            System.err.println("Error: Vuforia frameQueue returned null");
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(180);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),matrix,true);
    }


    //private static final int IMAGE_WIDTH = 1280, IMAGE_HEIGHT = 720;
    //private static final int IMAGE_WIDTH = 320, IMAGE_HEIGHT = 180;
    private static final int IMAGE_WIDTH = 32, IMAGE_HEIGHT = 18;
    private static final double SCALE_FACTOR = 0.4;
    private static final int SCALED_WIDTH = (int)(IMAGE_WIDTH * SCALE_FACTOR), SCALED_HEIGHT = (int)(IMAGE_HEIGHT * SCALE_FACTOR);
    private static final int HALF_WIDTH = SCALED_WIDTH / 2;
    public SideOfButton getBeaconBlueSide(){
        SideOfButton sideOfButton = SideOfButton.UNKNOWN;
        Log.d(Utils.TAG, "Get bitmap");
        Bitmap bitmap = getBitmap();
        Log.d(Utils.TAG, "Scale Image");
        if (bitmap != null) {
            bitmap = Bitmap.createScaledBitmap(bitmap, SCALED_WIDTH, SCALED_HEIGHT, false); //scale down to decrease processing time

            Log.d(Utils.TAG, "Process Image");
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
            Log.d(Utils.TAG, "Image Processed");

            Log.d(Utils.TAG, "leftRed: " + leftRed);
            Log.d(Utils.TAG, "rightRed: " + rightRed);
            Log.d(Utils.TAG,"leftBlue: " + leftBlue);
            Log.d(Utils.TAG, "rightBlue: " + rightBlue);

            final boolean beacon1BlueLeft = leftRed + rightBlue < rightRed + leftBlue;
            if (beacon1BlueLeft){
                sideOfButton = SideOfButton.LEFT;
            }
            else {
                sideOfButton = SideOfButton.RIGHT;
            }
        }

        Log.i(Utils.TAG, "Beacon: Blue is on side " + sideOfButton);
        return sideOfButton;
    }

}
