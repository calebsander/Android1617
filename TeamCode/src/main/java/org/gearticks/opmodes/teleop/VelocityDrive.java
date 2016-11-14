package org.gearticks.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.opmodes.BaseOpMode;

/**
 * Created by Calvin on 11/13/2016.
 */

@TeleOp
public class VelocityDrive extends BaseOpMode {
    private VelocityConfiguration configuration;
    private DriveDirection direction;

    protected void initialize() {
        this.configuration = new VelocityConfiguration(this.hardwareMap);
        this.direction = new DriveDirection();
    }

    protected void loopAfterStart() {
        this.direction.drive(0.0, (this.gamepads[0].getLeftY())*(0.8));
        this.direction.turn(scaleStick(this.gamepads[0].getRightX()));
        this.configuration.drive.calculatePowers(this.direction);
        this.configuration.drive.scaleMotorsDown();
        this.configuration.drive.commitPowers();
        final double intakePower;
        if (this.gamepads[0].getRightTrigger()) {
            intakePower = VelocityConfiguration.MotorConstants.INTAKE_IN;
        }
        else if (this.gamepads[0].getRightBumper()) {
            intakePower = VelocityConfiguration.MotorConstants.INTAKE_OUT;
        }
        else {
            intakePower = MotorWrapper.STOPPED;
        }
        this.configuration.intake.setPower(intakePower);
    }

    private static float scaleStick(float stick) {
        return (stick * stick * stick)*(float)(0.5);
    }
}
