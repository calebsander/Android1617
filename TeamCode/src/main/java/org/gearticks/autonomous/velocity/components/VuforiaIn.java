package org.gearticks.autonomous.velocity.components;

import android.support.annotation.NonNull;

import com.vuforia.HINT;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.gearticks.Vuforia.VuforiaConfiguration;
import org.gearticks.VuforiaKey;
import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.opmodes.utility.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by irene on 12/26/2016.
 */

public class VuforiaIn extends AutonomousComponentVelocityBase {
    private final DriveDirection direction = new DriveDirection();
    private final float finalDistance;
    private final VuforiaConfiguration vuforiaConfiguration;


    // Vuforia properties
    private VuforiaTrackableDefaultListener firstTargetListener;
    private VuforiaTrackables beaconImages;
    @NonNull
    final String firstTargetName;
    private static final Map<String, Integer> IMAGE_IDS = new HashMap<>();
    static {
        IMAGE_IDS.put("Wheels", 0);
        IMAGE_IDS.put("Tools", 1);
        IMAGE_IDS.put("Legos", 2);
        IMAGE_IDS.put("Gears", 3);
    }

    private boolean allianceColorIsBlue;


    /**
     *
     * @param finalDistance - distance from picture the robot stops
     * @param configuration - config file
     * @param id - descriptive name for logging
     */
    public VuforiaIn(float finalDistance, @NonNull VuforiaConfiguration vuforiaConfiguration, @NonNull VelocityConfiguration configuration, String id) {
        super(configuration, id);

        this.finalDistance = finalDistance;
        this.vuforiaConfiguration = Utils.assertNotNull(vuforiaConfiguration);

        this.allianceColorIsBlue = AllianceOption.allianceOption.getRawSelectedOption() == AllianceOption.BLUE;
        if (this.allianceColorIsBlue) firstTargetName = "Wheels";
        else firstTargetName = "Gears";


    }

    @Override
    public void setup(int inputPort) {
        super.setup(inputPort);
        this.getLogger().info("Running Vuforia in setup");
        this.firstTargetListener = Utils.assertNotNull(this.vuforiaConfiguration.getTargetListener(firstTargetName));

    }

    @Override
    public int run() {
        int transition = super.run();

        Utils.assertNotNull(this.firstTargetListener);
        final OpenGLMatrix pose = this.firstTargetListener.getPose();


        if (pose == null) {
            this.direction.drive(0.0, 0.05);
            this.direction.turn(0.0);
        }
        else {
            final VectorF translation = pose.getTranslation();
            final float normalDistance = -translation.get(2);
            if (normalDistance < this.finalDistance) {
                this.direction.stopDrive();
                transition = 1;
            }
            else {
                this.direction.drive(0.0, 0.12);
                this.vuforiaTurn(translation, 0.0);
            }
        }

        return transition;
    }

    @Override
    public void tearDown() {
        super.tearDown();
    }

    //Gets distance we are to the left of the center of the image
    private static float getLateralDistance(@NonNull VectorF translation) {
        final int target = 0;
        return -(translation.get(1) - target);
    }
    private boolean vuforiaTurn(@NonNull VectorF translation, double minPower) {
        final float lateralDistance = getLateralDistance(translation);
        if (Math.abs(lateralDistance) > 50) {
            double turnPower = lateralDistance * 0.0007;
            if (Math.abs(turnPower) > 0.05) turnPower = Math.signum(turnPower) * 0.05; //max out the turn power
            this.direction.turn(turnPower + minPower * Math.signum(turnPower));
            return false;
        }
        else {
            this.direction.turn(0.0);
            return true;
        }
    }
}
