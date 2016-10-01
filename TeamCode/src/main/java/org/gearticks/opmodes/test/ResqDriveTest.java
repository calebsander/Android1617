package org.gearticks.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.gearticks.hardware.configurations.ResqConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp(name = "Resq Drive", group = "test")
public class ResqDriveTest extends BaseOpMode {
    private ResqConfiguration configuration;
    private DriveDirection direction;
    protected void initialize() {
        this.configuration = new ResqConfiguration(this.hardwareMap);
        this.direction = new DriveDirection();
    }
    protected void loopAfterStart() {
        this.direction.drive(0.0, this.gamepads[0].getLeftY());
        this.direction.turn(this.gamepads[0].getRightX());
        this.configuration.drive.calculatePowers(this.direction);
        this.configuration.drive.scaleMotorsDown();
        this.configuration.drive.commitPowers();
        final double screwPower;
        if (gamepads[1].getRightTrigger()) screwPower = ResqConfiguration.MotorConstants.SCREW_FORWARD;
        else if (gamepads[1].getRightBumper()) screwPower = ResqConfiguration.MotorConstants.SCREW_BACKWARD;
        else screwPower = 0.0;
        this.configuration.screw.setPower(screwPower);
        this.configuration.lift.setPower(this.gamepads[1].getRightY() * ResqConfiguration.MotorConstants.LIFT_UP);
        this.configuration.door.setPower(this.gamepads[1].getLeftX() * ResqConfiguration.MotorConstants.DOOR_RIGHT);
        this.configuration.bucketLift.setPower(this.gamepads[1].getLeftY() * ResqConfiguration.MotorConstants.BUCKET_UP);
    }
}
