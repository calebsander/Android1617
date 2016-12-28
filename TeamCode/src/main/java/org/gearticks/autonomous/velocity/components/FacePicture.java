package org.gearticks.autonomous.velocity.components;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.util.ElapsedTime;
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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by irene on 12/26/2016.
 */

public class FacePicture extends AutonomousComponentVelocityBase {
    private final DriveDirection direction = new DriveDirection();
    //private final float finalDistance;
    private VuforiaConfiguration vuforiaConfiguration;


    // Vuforia properties
    private VuforiaTrackableDefaultListener firstTargetListener;
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
    final double angleMultiplier;
    private ElapsedTime angleCorrectionTimer;


    /**
     *
     * @param configuration - config file
     * @param id - descriptive name for logging
     */
    public FacePicture(VuforiaConfiguration vuforiaConfiguration, @NonNull VelocityConfiguration configuration, String id) {
        super(configuration, id);
        this.vuforiaConfiguration = vuforiaConfiguration;

        this.allianceColorIsBlue = AllianceOption.allianceOption.getRawSelectedOption() == AllianceOption.BLUE;
        if (this.allianceColorIsBlue) firstTargetName = "Wheels";
        else firstTargetName = "Gears";
        if (this.allianceColorIsBlue) angleMultiplier = 1.0; //angles were calculated for blue side
        else angleMultiplier = -1.0; //invert all angles for red side
        this.angleCorrectionTimer = new ElapsedTime();

    }


    @Override
    public void setup(int inputPort) {
        super.setup(inputPort);
        this.firstTargetListener = this.vuforiaConfiguration.getTargetListener(firstTargetName);

    }

    @Override
    public int run() {
        int transition = super.run();

        final OpenGLMatrix firstTargetPose = this.firstTargetListener.getPose();

        if (firstTargetPose == null) {
            if (this.direction.gyroCorrect(90.0 * angleMultiplier, 1.0, this.getConfiguration().imu.getRelativeYaw(), 0.08, 0.1) < 10) {
                this.getStageTimer().reset();
            }
        }
        else {
            if ((this.angleCorrectionTimer.seconds() % 0.3) < 0.1) { //for .1 second out of each .3 seconds, correct using Vuforia
                if (!this.vuforiaTurn(firstTargetPose.getTranslation(), 0.05)) this.getStageTimer().reset();
            }
            else this.direction.stopDrive(); //then pause for .2 seconds
        }
        if (this.getStageTimer().seconds() > 0.4) { //if we have been on target for .4 seconds in a row, take the picture
            transition = 1; //wait until getting a frame
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
