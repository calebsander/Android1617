package org.gearticks.components.velocity.component.drive;

import android.util.Log;
import org.gearticks.PIDControl.MiniPID;
import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent.DefaultTransition;
import org.gearticks.components.generic.component.OpModeComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.Utils;

/**
 * drives with gyro and range sensor along the wall at a set distance for encoder ticks
 */
public class GiroDriveAlongWallLine extends OpModeComponentHardware<VelocityConfiguration, DefaultTransition> {
    private final DriveDirection direction;
    private final double power;
    private final double distanceFromWall;
    private final double targetHeading;
    private double controlledHeading;
    private final long encoderLimit;
    private final double p = 1.5;
    private final double i = 0.0;
    private final double d = 0.0;
    private MiniPID pidController;


    public GiroDriveAlongWallLine(double distanceFromWall, double targetHeading, double power, long encoderLimit, OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(opModeContext, DefaultTransition.class, id);
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
    }

    @Override
    public DefaultTransition run() {
        final DefaultTransition superTransition = super.run();
        if (superTransition != null) return superTransition;

        this.direction.drive(0.0, this.power);
        this.direction.gyroCorrect(this.controlledHeading, 1.0, this.configuration.imu.getRelativeYaw(), 0.05, 0.1);
        this.configuration.move(this.direction, 0.06);

        double ultrasonicDistance = this.configuration.rangeSensor.cmUltrasonic();
        //Log.v(Utils.TAG, "Ultrasonic distance = " + ultrasonicDistance);

        double distanceError = this.distanceFromWall - ultrasonicDistance;
        //Log.v(Utils.TAG, "Distance error = " + distanceError);

        double headingDeviation = this.pidController.getOutput(ultrasonicDistance, this.distanceFromWall);
        //Log.v(Utils.TAG, "Heading deviation = " + headingDeviation);

        if (this.power > 0){
            this.controlledHeading = this.targetHeading + headingDeviation;
        } else{
            this.controlledHeading = this.targetHeading - headingDeviation;
        }

        Log.d(Utils.TAG, "Ultrasonic distance = " + ultrasonicDistance + " Distance error = " + distanceError + " Heading deviation = " + headingDeviation + " Encoder val = " + this.configuration.encoderPositive());


        Log.v(Utils.TAG, "white line sensor = " + this.configuration.isWhiteLineIR());
        if (this.configuration.isWhiteLineIR()){
            Log.d(Utils.TAG, "Heading = " + this.configuration.imu.getRelativeYaw());
            Log.d(Utils.TAG, "Transitioning because found white line");
            return DefaultTransition.DEFAULT; //TODO returning LINE_FOUND
        }
        if (this.configuration.encoderPositive() > this.encoderLimit) {
            Log.d(Utils.TAG, "Heading = " + this.configuration.imu.getRelativeYaw());
            Log.d(Utils.TAG, "Transitioning because encoder limit reached = " + this.configuration.encoderPositive());
            return DefaultTransition.DEFAULT; //TODO returning LIMIT_REACHED
        }
        return null;
    }

    @Override
    public void tearDown() {
        super.tearDown();
        this.configuration.rangeSensor.ultrasonicRequest.stopReading();
    }

}
