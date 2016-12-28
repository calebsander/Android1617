package org.gearticks.vuforia;

import android.graphics.Bitmap;
import com.vuforia.HINT;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CloseableFrame;
import java.util.concurrent.BlockingQueue;

public class VuforiaConfiguration {
    private final BlockingQueue<VuforiaLocalizer.CloseableFrame> frameQueue;
    private final VuforiaTrackables beaconImages;


    public VuforiaConfiguration() {
        final VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(/*R.id.cameraMonitorViewId*/);
        parameters.vuforiaLicenseKey = VuforiaKey.KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        final VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 2);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
        this.beaconImages = vuforia.loadTrackablesFromAsset("FTC_2016-17");
        this.frameQueue = vuforia.getFrameQueue();
        vuforia.setFrameQueueCapacity(1);
    }

    public VuforiaTrackableDefaultListener getTargetListener(String targetName) {
        return (VuforiaTrackableDefaultListener)this.beaconImages.get(VuforiaImageIDs.getID(targetName)).getListener();
    }

    public void activate() {
        this.beaconImages.activate();
    }

    /**
     * @return can be null if Vuforia didn't return a usable frame
     */
    public Bitmap getBitmap() {
        Bitmap bitmap = null;
        CloseableFrame frame = null;
        try {
            frame = this.frameQueue.take();
        }
        catch (InterruptedException e) {
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
}