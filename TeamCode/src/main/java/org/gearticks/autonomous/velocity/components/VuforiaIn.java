package org.gearticks.autonomous.velocity.components;

import android.support.annotation.NonNull;
import android.util.Log;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.gearticks.Vuforia.VuforiaConfiguration;
import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.opmodes.utility.Utils;

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
    private boolean allianceColorIsBlue;


    /**
     *
     * @param finalDistance - distance from picture the robot stops
     * @param isNearBeacon - what beacon it's looking for
     * @param configuration - config file
     * @param id - descriptive name for logging
     */
    public VuforiaIn(float finalDistance, boolean isNearBeacon, @NonNull VuforiaConfiguration vuforiaConfiguration, @NonNull VelocityConfiguration configuration, String id) {
        super(configuration, id);

        this.finalDistance = finalDistance;
        this.vuforiaConfiguration = Utils.assertNotNull(vuforiaConfiguration);

        this.allianceColorIsBlue = AllianceOption.allianceOption.getRawSelectedOption() == AllianceOption.BLUE;
        if (isNearBeacon) {
            if (this.allianceColorIsBlue) firstTargetName = "Wheels";
            else firstTargetName = "Gears";
        }
        else {
            if (this.allianceColorIsBlue) firstTargetName = "Legos";
            else firstTargetName = "Tools";
        }


    }

    @Override
    public void setup(int inputPort) {
        super.setup(inputPort);
        this.vuforiaConfiguration.activate();
        Log.d(Utils.TAG, "running vuforia in setup : create first target listener");
        this.firstTargetListener = Utils.assertNotNull(this.vuforiaConfiguration.getTargetListener(firstTargetName));

    }

    @Override
    public int run() {
        int transition = super.run();

        Utils.assertNotNull(this.firstTargetListener);
        Log.v(Utils.TAG, "get pose from listener");
        final OpenGLMatrix pose = this.firstTargetListener.getPose();


        if (pose == null) {
            Log.v(Utils.TAG, "pose == null");

            this.direction.drive(0.0, 0.05);
            this.direction.turn(0.0);
        }
        else {
            Log.v(Utils.TAG, "pose != null");

            final VectorF translation = pose.getTranslation();
            final float normalDistance = -translation.get(2);
            Log.v(Utils.TAG, "normal distance = " + normalDistance);

            if (normalDistance < this.finalDistance) {
                Log.v(Utils.TAG, "stop drive");
                this.direction.stopDrive();
                transition = 1;
            }
            else {
                Log.v(Utils.TAG, "drive and turn to beacon");
                this.direction.drive(0.0, 0.12);
                this.vuforiaTurn(translation, 0.0);
            }
        }

        this.getConfiguration().move(this.direction, 0.06);

        return transition;
    }

    @Override
    public void tearDown() {
        this.vuforiaConfiguration.deactivate();
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
