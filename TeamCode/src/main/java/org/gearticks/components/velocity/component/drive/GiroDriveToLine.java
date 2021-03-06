package org.gearticks.components.velocity.component.drive;

import android.util.Log;
import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponentHardware;
import org.gearticks.components.velocity.component.drive.GiroDriveToLine.LineOrTimeout;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.Utils;

/**
 * Please add some comments on this component.
 */
public class GiroDriveToLine extends OpModeComponentHardware<VelocityConfiguration, LineOrTimeout> {
    public enum LineOrTimeout {
        LINE_FOUND,
        ENCODER_TIMEOUT
    }
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
        super(opModeContext, LineOrTimeout.class, id);
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
    }

    @Override
    public LineOrTimeout run() {
        final LineOrTimeout superTransition = super.run();
        if (superTransition != null) return superTransition;

        //control giro drive
        this.direction.drive(0.0, this.power);
        this.direction.gyroCorrect(this.targetHeading * this.angleMultiplier, 1.0, this.configuration.imu.getRelativeYaw(), 0.05, 0.1);
        this.configuration.move(this.direction, 0.06);

        Log.v(Utils.TAG, "white line sensor = " + this.configuration.isWhiteLineIR());
        if (this.configuration.isWhiteLineIR()){
            Log.d(Utils.TAG, "Transitioning because found white line");
            Log.d(Utils.TAG, "Heading = " + this.configuration.imu.getRelativeYaw());
            return LineOrTimeout.LINE_FOUND;
        }
        if (this.configuration.encoderPositive() > this.maxEncoderTarget) {
            Log.d(Utils.TAG, "Transitioning because encoder limit reached = " + this.configuration.encoderPositive());
            Log.d(Utils.TAG, "Heading = " + this.configuration.imu.getRelativeYaw());
            return LineOrTimeout.ENCODER_TIMEOUT;
        }
        return null;
    }
}
