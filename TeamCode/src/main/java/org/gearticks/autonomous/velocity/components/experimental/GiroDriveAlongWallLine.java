package org.gearticks.autonomous.velocity.components.experimental;

import android.support.annotation.NonNull;
import android.util.Log;

import org.gearticks.PIDControl.MiniPID;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.opmodes.utility.Utils;

/**
 * drives with gyro and range sensor along the wall at a set distance for encoder ticks
 */
public class GiroDriveAlongWallLine extends AutonomousComponentHardware<VelocityConfiguration> {
    private final DriveDirection direction;
    private final double power;
    private final double distanceFromWall;
    private final double targetHeading;
    private double controlledHeading;
    private final long encoderLimit;
    private final double p = 0.3;
    private final double i = 0;
    private final double d = 0;
    private MiniPID pidController;


    public GiroDriveAlongWallLine(double distanceFromWall, double targetHeading, double power, long encoderLimit, @NonNull VelocityConfiguration configuration, String id) {
        super(configuration, id);
        this.direction = new DriveDirection();
        this.power = power;
        this.distanceFromWall = distanceFromWall;
        this.targetHeading = targetHeading;
        this.encoderLimit = encoderLimit;
    }

    @Override
    public void setup() {
        super.setup();
        this.controlledHeading = this.targetHeading;
        this.configuration.rangeSensor.ultrasonicRequest.startReading();
        this.configuration.resetEncoder();
        this.pidController = new MiniPID(p, i, d);
        this.pidController.setOutputLimits(10);
        this.configuration.activateWhiteLineColor();
    }

    @Override
    public int run() {
        final int superTransition = super.run();
        if (superTransition != NOT_DONE) return superTransition;

        this.direction.drive(0.0, this.power);
        this.direction.gyroCorrect(this.controlledHeading, 1.0, this.configuration.imu.getRelativeYaw(), 0.05, 0.1);
        this.configuration.move(this.direction, 0.06);

        double ultrasonicDistance = this.configuration.rangeSensor.cmUltrasonic();
        Log.v(Utils.TAG, "Ultrasonic distance = " + ultrasonicDistance);

        double distanceError = this.distanceFromWall - ultrasonicDistance;
        Log.v(Utils.TAG, "Distance error = " + distanceError);

        double headingDeviation = this.pidController.getOutput(ultrasonicDistance, this.distanceFromWall);
        Log.v(Utils.TAG, "Heading deviation = " + headingDeviation);

        this.controlledHeading = this.targetHeading - headingDeviation;

        Log.v(Utils.TAG, "white line sensor = " + this.configuration.isWhiteLine());
        if (this.configuration.isWhiteLine()){
            Log.d(Utils.TAG, "Transitioning because found white line");
            return NEXT_STATE; //TODO returning LINE_FOUND
        }
        if (this.configuration.encoderPositive() > this.encoderLimit) {
            Log.d(Utils.TAG, "Transitioning because encoder limit reached = " + this.configuration.encoderPositive());
            return NEXT_STATE; //TODO returning LIMIT_REACHED
        }
        return NOT_DONE;
    }

    @Override
    public void tearDown() {
        super.tearDown();
        this.configuration.deactivateWhiteLineColor();
        //Custom code here control
    }

}
