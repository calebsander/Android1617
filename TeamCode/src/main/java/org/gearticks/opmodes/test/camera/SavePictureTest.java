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

@TeleOp
@Disabled
public class SavePictureTest extends BaseOpMode {
    private static final File SAVE_FILE_NAME = new File(new File(Environment.getExternalStorageDirectory(), "Pictures"), "Out.PNG");
    private VuforiaConfiguration vuforiaConfiguration;

    @Override
    protected void initialize() {
        this.vuforiaConfiguration = new VuforiaConfiguration();
    }

    @SuppressWarnings({"EmptyCatchBlock", "ConstantConditions"})
    @Override
    protected void loopAfterStart() {
        if (this.gamepads[0].getA() && !this.gamepads[0].getLast().getA()) {
            Bitmap bitmap = vuforiaConfiguration.getBitmap();
            try {
                FileOutputStream outputStream = new FileOutputStream(SAVE_FILE_NAME);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
                outputStream.close();
            }
            catch (IOException e) {}
        }
    }
}
