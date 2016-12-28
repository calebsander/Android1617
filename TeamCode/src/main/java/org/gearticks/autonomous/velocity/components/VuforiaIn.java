package org.gearticks.autonomous.velocity.components;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.gearticks.vuforia.VuforiaConfiguration;
import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.opmodes.utility.Utils;

public class VuforiaIn extends AutonomousComponentVelocityBase {
    private final DriveDirection direction;
    private final float finalDistance;
    private final VuforiaConfiguration vuforiaConfiguration;
    private VuforiaTrackableDefaultListener firstTargetListener;

    /**
     *
     * @param finalDistance - distance from picture the robot stops
     * @param configuration - config file
     * @param id - descriptive name for logging
     */
    public VuforiaIn(float finalDistance, VuforiaConfiguration vuforiaConfiguration, VelocityConfiguration configuration, String id) {
        super(configuration, id);
        this.direction = new DriveDirection();
        this.finalDistance = finalDistance;
        this.vuforiaConfiguration = vuforiaConfiguration;
    }

    @Override
    public void setup() {
        super.setup();
        this.getLogger().info("Running Vuforia in setup");
        final boolean allianceColorIsBlue = AllianceOption.allianceOption.getRawSelectedOption() == AllianceOption.BLUE;
        final String firstTargetName;
        if (allianceColorIsBlue) firstTargetName = "Wheels";
        else firstTargetName = "Gears";
        this.firstTargetListener = this.vuforiaConfiguration.getTargetListener(firstTargetName);
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

    //Gets distance we are to the left of the center of the image
    private static float getLateralDistance(VectorF translation) {
        final int target = 0;
        return -(translation.get(1) - target);
    }
    private boolean vuforiaTurn(VectorF translation, double minPower) {
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
