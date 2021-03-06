package org.gearticks.components.velocity.component.experimental;

import android.util.Log;
import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent.DefaultTransition;
import org.gearticks.components.generic.component.OpModeComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.Utils;

public class GiroDriveToRangeDistance extends OpModeComponentHardware<VelocityConfiguration, DefaultTransition> {
    private final DriveDirection direction;
    private final double power;
    private final double targetHeading;
    private final double distanceFromWall;
    private final long encoderTarget;


    public GiroDriveToRangeDistance(double distanceFromWall, double targetHeading, double power, long encoderLimit, OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(opModeContext, DefaultTransition.class, id);
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
    public DefaultTransition run() {
        final DefaultTransition superTransition = super.run();
        if (superTransition != null) return superTransition;

        //control giro drive
        this.direction.drive(0.0, this.power);
        this.direction.gyroCorrect(this.targetHeading, 1.0, this.configuration.imu.getRelativeYaw(), 0.05, 0.1);
        this.configuration.move(this.direction, 0.06);

        double ultrasonicDistance = this.configuration.rangeSensor.cmUltrasonic();
        Log.d(Utils.TAG, "Ultrasonic distance = " + ultrasonicDistance);


        if(ultrasonicDistance < this.distanceFromWall) {
            Log.d(Utils.TAG, "Transitioning because distance limit reached = " + this.configuration.encoderPositive());
            return DefaultTransition.DEFAULT;
        }
        if (this.configuration.encoderPositive() > this.encoderTarget) {
            Log.d(Utils.TAG, "Transitioning because encoder limit reached = " + this.configuration.encoderPositive());
            return DefaultTransition.DEFAULT;
        }
        else return null;
    }

    @Override
    public void tearDown() {
        super.tearDown();
        this.configuration.rangeSensor.ultrasonicRequest.stopReading();
    }
}
