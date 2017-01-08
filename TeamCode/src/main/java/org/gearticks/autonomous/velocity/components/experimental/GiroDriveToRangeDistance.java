package org.gearticks.autonomous.velocity.components.experimental;

import android.support.annotation.NonNull;
import android.util.Log;

import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.opmodes.utility.Utils;

public class GiroDriveToRangeDistance extends AutonomousComponentHardware<VelocityConfiguration> {
    private final DriveDirection direction;
    private final double power;
    private final double targetHeading;
    private final double distanceFromWall;
    private final long encoderTarget;


    public GiroDriveToRangeDistance(double distanceFromWall, double targetHeading, double power, long encoderLimit, @NonNull VelocityConfiguration configuration, String id) {
        super(configuration, id);
        this.direction = new DriveDirection();
        this.power = power;
        this.targetHeading = targetHeading;
        this.distanceFromWall = distanceFromWall;
        this.encoderTarget = encoderLimit;
    }

    @Override
    public void setup() {
        super.setup();
        this.configuration.rangeSensor.ultrasonicRequest.startReading();
        this.configuration.resetEncoder();
    }

    @Override
    public int run() {
        final int superTransition = super.run();
        if (superTransition != NOT_DONE) return superTransition;

        //control giro drive
        this.direction.drive(0.0, this.power);
        this.direction.gyroCorrect(this.targetHeading, 1.0, this.configuration.imu.getRelativeYaw(), 0.05, 0.1);
        this.configuration.move(this.direction, 0.06);

        double ultrasonicDistance = this.configuration.rangeSensor.cmUltrasonic();
        Log.d(Utils.TAG, "Ultrasonic distance = " + ultrasonicDistance);


        if(ultrasonicDistance < this.distanceFromWall) return NEXT_STATE;
        if (this.configuration.encoderPositive() > this.encoderTarget) return NEXT_STATE;
        else return NOT_DONE;
    }

    @Override
    public void tearDown() {
        super.tearDown();
        //Custom code here
    }
}
