package org.gearticks.autonomous.velocity.components.velocity.single;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.vuforia.VuforiaConfiguration;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.vuforia.VuforiaImages;

public class FacePicture extends AutonomousComponentHardware<VelocityConfiguration> {
    private final boolean isNearBeacon;
    private final DriveDirection direction;
    private final VuforiaConfiguration vuforiaConfiguration;
    private double angleMultiplier;
    private ElapsedTime angleCorrectionTimer;
    private VuforiaTrackableDefaultListener firstTargetListener;

    /**
     * @param opModeContext - the OpModeContext this is running in
     * @param id - descriptive name for logging
     */
    public FacePicture(boolean isNearBeacon, OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(opModeContext, id);
        this.isNearBeacon = isNearBeacon;
        this.direction = new DriveDirection();
        this.vuforiaConfiguration = opModeContext.getVuforiaConfiguration();
        this.angleCorrectionTimer = new ElapsedTime();
    }

    @Override
    public void setup() {
        super.setup();
        final boolean allianceColorIsBlue = AllianceOption.allianceOption.getRawSelectedOption() == AllianceOption.BLUE;
        if (allianceColorIsBlue) this.angleMultiplier = 1.0; //angles were calculated for blue side
        else this.angleMultiplier = -1.0; //invert all angles for red side
        this.vuforiaConfiguration.activate();
        this.firstTargetListener = this.vuforiaConfiguration.getTargetListener(VuforiaImages.getImageName(allianceColorIsBlue, this.isNearBeacon));
    }

    @Override
    public Transition run() {
        final Transition superTransition = super.run();
        if (superTransition != null) return superTransition;

        final OpenGLMatrix firstTargetPose = this.firstTargetListener.getPose();
        if (firstTargetPose == null) {
            if (this.direction.gyroCorrect(90.0 * angleMultiplier, 1.0, this.configuration.imu.getRelativeYaw(), 0.08, 0.1) < 10) {
                this.angleCorrectionTimer.reset();
            }
        }
        else {
            if ((this.angleCorrectionTimer.seconds() % 0.3) < 0.1) { //for .1 second out of each .3 seconds, correct using Vuforia
                if (!this.vuforiaTurn(firstTargetPose.getTranslation(), 0.05)) this.angleCorrectionTimer.reset();
            }
            else this.direction.stopDrive(); //then pause for .2 seconds
        }
        this.configuration.move(this.direction, 0.06);
        if (this.angleCorrectionTimer.seconds() > 0.4) return NEXT_STATE; //if we have been on target for .4 seconds in a row, take the picture
        return null;
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
