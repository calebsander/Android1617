package org.gearticks.hardware.configurations;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.hardware.drive.ServoWrapper;
import org.gearticks.hardware.drive.TankDrive;

/**
 * Created by Calvin on 11/13/2016.
 */
public class VelocityConfiguration implements HardwareConfiguration {
    public final MotorWrapper intake, shooter;
    public MotorWrapper driveLeft, driveRight;
    public final TankDrive drive;

    public VelocityConfiguration(HardwareMap hardwareMap) {
        this.intake = new MotorWrapper((DcMotor)hardwareMap.get("intake"), MotorWrapper.MotorType.NEVEREST_40, true);
        this.shooter = new MotorWrapper((DcMotor)hardwareMap.get("shooter"), MotorWrapper.MotorType.NEVEREST_40);
        this.driveLeft = new MotorWrapper((DcMotor)hardwareMap.get("left"), MotorWrapper.MotorType.NEVEREST_20, true);
        this.driveRight = new MotorWrapper((DcMotor)hardwareMap.get("right"), MotorWrapper.MotorType.NEVEREST_20, true);
        this.drive = new TankDrive();
        this.drive.addLeftMotor(this.driveLeft);
        this.drive.addRightMotor(this.driveRight);

        this.driveLeft.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.driveRight.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void teardown() {}
    public void stopMotion() {
        this.intake.setPower(MotorWrapper.STOPPED);
        this.driveLeft.setPower(MotorWrapper.STOPPED);
        this.driveRight.setPower(MotorWrapper.STOPPED);
    }
    public void move(DriveDirection direction) {
        this.drive.calculatePowers(direction);
        this.drive.scaleMotorsDown();
        this.drive.commitPowers();
    }

    public static abstract class MotorConstants {
        public static final double INTAKE_IN = 1.0;
        public static final double INTAKE_OUT = -INTAKE_IN;

        public static final double SHOOTER_IN = 0.5;
        public static final double SHOOTER_OUT = -SHOOTER_IN;

        //Flips a servo to the other range
        private static double invert(double pos) {
            return 1.0 - pos;
        }
    }
}
