package org.gearticks.opmodes.test.camera;

import android.graphics.Bitmap;
import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.gearticks.opmodes.BaseOpMode;
import org.gearticks.vuforia.VuforiaConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Kevin on 1/14/2017.
 */
@TeleOp

public class SavePictureTest extends BaseOpMode {
    private static final File saveFileName = new File(new File(Environment.getExternalStorageDirectory(), "Pictures"), "Out.PNG");
    private VuforiaConfiguration vuforiaConfiguration;

    @Override
    protected void initialize() {
        vuforiaConfiguration = new VuforiaConfiguration();

    }

    @Override
    protected void loopAfterStart() {
        if (gamepads[0].getA() && !gamepads[0].getLast().getA()) {
            Bitmap bitmap = vuforiaConfiguration.getBitmap();
            try {
                FileOutputStream outputStream = new FileOutputStream(saveFileName);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
                outputStream.close();
            }
            catch (IOException e) {

            }
        }
    }
}
