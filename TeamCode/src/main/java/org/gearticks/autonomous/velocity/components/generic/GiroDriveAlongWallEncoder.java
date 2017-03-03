package org.gearticks.autonomous.velocity.components.generic;

import android.util.Log;
import org.gearticks.PIDControl.MiniPID;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent.DefaultTransition;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.opmodes.utility.Utils;

/**
 * drives with gyro and range sensor along the wall at a set distance for encoder ticks
 */
public class GiroDriveAlongWallEncoder extends AutonomousComponentHardware<VelocityConfiguration, DefaultTransition> {
    private final DriveDirection direction;
    private final double power;
    private final double distanceFromWall;
    private final double targetHeading;
    private double controlledHeading;
    private final long encoderTarget;
    private final double p = 1.5;
    private final double i = 0;
    private final double d = 0;
    private MiniPID pidController;


    public GiroDriveAlongWallEncoder(double distanceFromWall, double targetHeading, double power, long encoderLimit, OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(opModeContext, DefaultTransition.class, id);
        this.direction = new DriveDirection();
        this.power = power;
        this.distanceFromWall = distanceFromWall;
        this.targetHeading = targetHeading;
        this.encoderTarget = encoderLimit;
    }

    @Override
    public void setup() {
        super.setup();
        this.controlledHeading = this.targetHeading;
        this.configuration.rangeSensor.ultrasonicRequest.startReading();
        this.configuration.resetEncoder();
        this.pidController = new MiniPID(p, i, d);
        this.pidController.setOutputLimits(20);
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

        //this.controlledHeading = this.targetHeading + headingDeviation;

        Log.d(Utils.TAG, "Ultrasonic distance = " + ultrasonicDistance + " Distance error = " + distanceError + " Heading deviation = " + headingDeviation + " Encoder val = " + this.configuration.encoderPositive());
        if (this.configuration.encoderPositive() > this.encoderTarget) return DefaultTransition.DEFAULT;
        else return null;
    }

    @Override
    public void tearDown() {
        super.tearDown();
        this.configuration.rangeSensor.ultrasonicRequest.stopReading();
    }

}
