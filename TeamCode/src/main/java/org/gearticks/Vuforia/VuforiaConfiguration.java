package org.gearticks.Vuforia;

import android.graphics.Bitmap;
import android.graphics.Color;
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


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by irene on 12/27/2016.
 */

public class VuforiaConfiguration {

    private BlockingQueue<VuforiaLocalizer.CloseableFrame> frameQueue;
    private VuforiaTrackables beaconImages;

    private static final Map<String, Integer> IMAGE_IDS = new HashMap<>();
    static {
        IMAGE_IDS.put("Wheels", 0);
        IMAGE_IDS.put("Tools", 1);
        IMAGE_IDS.put("Legos", 2);
        IMAGE_IDS.put("Gears", 3);
    }


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

    public VuforiaTrackableDefaultListener getTargetListener(String targetName){
        return (VuforiaTrackableDefaultListener)this.beaconImages.get(IMAGE_IDS.get(targetName)).getListener();
    }

    public void activate(){
        this.beaconImages.activate();
    }

    public void deactivate(){
        this.beaconImages.deactivate();
    }

    /**
     *
     * @return can be null if Vuforia didn't return a usable frame
     */
    public @Nullable Bitmap getBitmap(){
        Bitmap bitmap = null;
        CloseableFrame frame = null;
        try {
            frame = this.frameQueue.take();
        } catch (InterruptedException e) {
            System.out.println("Error: Vuforia frameQue take failed");
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
            System.out.println("Error: Vuforia frameQue returned null");
        }
        return bitmap;
    }

    private static final int CROP_WIDTH = 600, CROP_HEIGHT = 350;
    private static final int CROP_LEFT_X = 275;
    private static final double SCALE_FACTOR = 0.4;
    private static final int SCALED_WIDTH = (int)(CROP_WIDTH * SCALE_FACTOR), SCALED_HEIGHT = (int)(CROP_HEIGHT * SCALE_FACTOR);
    private static final int HALF_WIDTH = SCALED_WIDTH / 2;
    public SideOfButton getBeaconBlueSide(){
        SideOfButton sideOfButton = SideOfButton.UNKNOWN;
        Bitmap bitmap = getBitmap();

        if (bitmap != null){
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
