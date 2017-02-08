package org.gearticks.autonomous.velocity.components.generic;

import android.util.Log;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.opmodes.utility.Utils;

/**
 * Please add some comments on this component.
 */
public class GiroDriveToLine extends AutonomousComponentHardware<VelocityConfiguration> {
    private static final int LINE_FOUND = newTransition(), ENCODER_TIMEOUT = newTransition();
    private final DriveDirection direction;
    private final double power;
    private final double targetHeading;
    private double angleMultiplier;
    private final long maxEncoderTarget;

    /**
     *
     * @param targetHeading - between 0 and 360, input to DriveDirection.gyroCorrect
     * @param power - between 0 and 1, input for DriveDirection
     * @param maxEncoderTarget - maximum limit for the encoder. If the encoderPositive exceeds this target, the component transitions
     * @param opModeContext - the OpModeContext this is running in
     * @param id - descriptive name for logging
     */
    public GiroDriveToLine(double targetHeading, double power, long maxEncoderTarget, OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(opModeContext.configuration, id);
        this.direction = new DriveDirection();
        this.power = power;
        this.targetHeading = targetHeading;
        this.maxEncoderTarget = maxEncoderTarget;
    }

    @Override
    public void setup() {
        super.setup();
        final boolean allianceColorIsBlue = AllianceOption.allianceOption.getRawSelectedOption() == AllianceOption.BLUE;
        if (allianceColorIsBlue) this.angleMultiplier = 1.0; //angles were calculated for blue side
        else this.angleMultiplier = -1.0; //invert all angles for red side
        this.configuration.resetEncoder();
//        this.configuration.activateWhiteLineColor();
    }

    @Override
    public int run() {
        final int superTransition = super.run();
        if (superTransition != NOT_DONE) return superTransition;

        //control giro drive
        this.direction.drive(0.0, this.power);
        this.direction.gyroCorrect(this.targetHeading * this.angleMultiplier, 1.0, this.configuration.imu.getRelativeYaw(), 0.05, 0.1);
        this.configuration.move(this.direction, 0.06);

        Log.v(Utils.TAG, "white line sensor = " + this.configuration.isWhiteLineIR());
        if (this.configuration.isWhiteLineIR()){
            Log.d(Utils.TAG, "Transitioning because found white line");
            Log.d(Utils.TAG, "Heading = " + this.configuration.imu.getRelativeYaw());
            return LINE_FOUND;
        }
        if (this.configuration.encoderPositive() > this.maxEncoderTarget) {
            Log.d(Utils.TAG, "Transitioning because encoder limit reached = " + this.configuration.encoderPositive());
            Log.d(Utils.TAG, "Heading = " + this.configuration.imu.getRelativeYaw());
            return ENCODER_TIMEOUT;
        }
        return NOT_DONE;
    }

    @Override
    public void tearDown() {
        super.tearDown();
//        this.configuration.deactivateWhiteLineColor();
        //stop motors
        this.configuration.stopMotion();
    }
}
