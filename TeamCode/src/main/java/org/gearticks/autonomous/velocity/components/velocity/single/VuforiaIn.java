package org.gearticks.autonomous.velocity.components.velocity.single;

import android.support.annotation.NonNull;
import android.util.Log;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.vuforia.VuforiaConfiguration;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.opmodes.utility.Utils;
import org.gearticks.vuforia.VuforiaImages;

public class VuforiaIn extends AutonomousComponentHardware<VelocityConfiguration> {
    private final DriveDirection direction;
    private final float finalDistance;
    private final VuforiaConfiguration vuforiaConfiguration;
    private VuforiaTrackableDefaultListener firstTargetListener;
    private final boolean isNearBeacon;

    /**
     * @param finalDistance - distance from picture the robot stops
     * @param isNearBeacon - what beacon it's looking for
     * @param opModeContext - the OpModeContext this is running in
     * @param id - descriptive name for logging
     */
    public VuforiaIn(float finalDistance, boolean isNearBeacon, OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(opModeContext, id);
        this.direction = new DriveDirection();
        this.finalDistance = finalDistance;
        this.vuforiaConfiguration = opModeContext.getVuforiaConfiguration();
        this.isNearBeacon = isNearBeacon;
    }

    @Override
    public void setup() {
        super.setup();
        this.vuforiaConfiguration.activate();
        Log.d(Utils.TAG, "running vuforia in setup : create first target listener");
        final boolean allianceColorIsBlue = AllianceOption.allianceOption.getRawSelectedOption() == AllianceOption.BLUE;
        this.firstTargetListener = this.vuforiaConfiguration.getTargetListener(VuforiaImages.getImageName(allianceColorIsBlue, this.isNearBeacon));
    }

    @Override
    public Transition run() {
        final Transition superTransition = super.run();
        if (superTransition != null) return superTransition;

        Log.v(Utils.TAG, "get pose from listener");
        final OpenGLMatrix pose = this.firstTargetListener.getPose();
        final Transition transition;
        if (pose == null) {
            Log.v(Utils.TAG, "pose == null");

            this.direction.drive(0.0, 0.05);
            this.direction.turn(0.0);
            transition = null;
        }
        else {
            Log.v(Utils.TAG, "pose != null");

            final VectorF translation = pose.getTranslation();
            final float normalDistance = -translation.get(2);
            Log.v(Utils.TAG, "normal distance = " + normalDistance);

            if (normalDistance < this.finalDistance) {
                Log.v(Utils.TAG, "stop drive");
                this.direction.stopDrive();
                transition = NEXT_STATE;
            }
            else {
                Log.v(Utils.TAG, "drive and turn to beacon");
                this.direction.drive(0.0, 0.12);
                this.vuforiaTurn(translation, 0.0);
                transition = null;
            }
        }
        this.configuration.move(this.direction, 0.06);

        return transition;
    }

    @Override
    public void tearDown() {
        super.tearDown();
        this.vuforiaConfiguration.deactivate();
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
