package org.gearticks.autonomous.velocity.components;

import android.support.annotation.NonNull;
import android.util.Log;

import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.opmodes.utility.Utils;

/**
 * Please add some comments on this component.
 */
public class GiroDriveToLine extends AutonomousComponentVelocityBase {
    private final DriveDirection direction = new DriveDirection();
    private double power = 0;
    private final double targetHeading;
    private long maxEncoderTarget;

    /**
     *
     * @param targetHeading - between 0 and 360, input to DriveDirection.gyroCorrect
     * @param power - between 0 and 1, input for DriveDirection
     * @param maxEncoderTarget - maximum limit for the encoder. If the encoderPositive exceeds this target, the component transitions
     * @param configuration
     * @param id - descriptive name for logging
     */
    public GiroDriveToLine(double targetHeading, double power, long maxEncoderTarget, @NonNull VelocityConfiguration configuration, String id) {
        super(configuration, id);
        this.power = power;
        this.targetHeading = targetHeading;
        this.maxEncoderTarget = maxEncoderTarget;
    }

    @Override
    public void setup(int inputPort) {
        super.setup(inputPort);
        this.getConfiguration().resetEncoder();
        this.getConfiguration().activateWhiteLineColor();


    }

    @Override
    public int run() {
        int transition = 0;
        super.run();

        //control giro drive
        this.direction.drive(0.0, this.power);
        this.direction.gyroCorrect(this.targetHeading, 1.0, this.getConfiguration().imu.getRelativeYaw(), 0.05, 0.1);
        this.getConfiguration().move(this.direction, 0.06);

        Log.v(Utils.TAG, "white line sensor = " + this.getConfiguration().isWhiteLine());
        if (this.getConfiguration().isWhiteLine()){
            Log.d(Utils.TAG, "Transitioning because found white line");
            transition = 1;
        }
        else if (this.getConfiguration().encoderPositive() > this.maxEncoderTarget) {
            Log.d(Utils.TAG, "Transitioning because encoder limit reached = " + this.getConfiguration().encoderPositive());
            transition = 1;
        }

        return transition;
    }

    @Override
    public void tearDown() {
        super.tearDown();
        //stop motors
        this.direction.stopDrive();
        this.getConfiguration().move(this.direction, 0.06);
        this.getConfiguration().deactivateWhiteLineColor();
    }
}
