package org.gearticks.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.gearticks.hardware.configurations.ResQConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp(name = "Resq Drive", group = "test")
public class ResQDriveTest extends BaseOpMode {
    private ResQConfiguration configuration;
    private DriveDirection direction;

    protected void initialize() {
        this.configuration = new ResQConfiguration(this.hardwareMap);
        this.direction = new DriveDirection();
    }
    protected void loopAfterStart() {
        this.direction.drive(0.0, this.gamepads[0].getLeftY());
        this.direction.turn(this.gamepads[0].getRightX());
        this.configuration.drive.calculatePowers(this.direction);
        this.configuration.drive.scaleMotorsDown();
        this.configuration.drive.commitPowers();
        final double screwPower;
        if (this.gamepads[1].getRightTrigger()) screwPower = ResQConfiguration.MotorConstants.SCREW_FORWARD;
        else if (this.gamepads[1].getRightBumper()) screwPower = ResQConfiguration.MotorConstants.SCREW_BACKWARD;
        else screwPower = MotorWrapper.STOPPED;
        this.configuration.screw.setPower(screwPower);
        this.configuration.lift.setPower(this.gamepads[1].getRightY() * ResQConfiguration.MotorConstants.LIFT_UP);
        this.configuration.door.setPower(this.gamepads[1].getLeftX() * ResQConfiguration.MotorConstants.DOOR_RIGHT);
        this.configuration.bucketLift.setPower(this.gamepads[1].getLeftY() * ResQConfiguration.MotorConstants.BUCKET_UP);
    }
}
