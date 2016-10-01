package org.gearticks.hardware.configurations;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsDigitalTouchSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.hardware.drive.TankDrive;

public class ResqConfiguration implements HardwareConfiguration {
    public final MotorWrapper screw;
    public final MotorWrapper lift;
    public final MotorWrapper door;
    public final CRServo bucketLift;
    public final MotorWrapper fl, fr, bl, br;
    public final TankDrive drive;
    public ResqConfiguration(HardwareMap hardwareMap) {
        this.lift = new MotorWrapper((DcMotor)hardwareMap.get("lift"), MotorWrapper.MotorType.NEVEREST_40);
        this.screw = new MotorWrapper((DcMotor)hardwareMap.get("screw"), MotorWrapper.MotorType.NEVEREST_20);
        this.door = new MotorWrapper((DcMotor)hardwareMap.get("door"), MotorWrapper.MotorType.NEVEREST_40);
        this.bucketLift = (CRServo)hardwareMap.get("bucketLift");
        this.fl = new MotorWrapper((DcMotor)hardwareMap.get("fl"), MotorWrapper.MotorType.NEVEREST_40);
        this.fr = new MotorWrapper((DcMotor)hardwareMap.get("fr"), MotorWrapper.MotorType.NEVEREST_40);
        this.bl = new MotorWrapper((DcMotor)hardwareMap.get("bl"), MotorWrapper.MotorType.NEVEREST_40);
        this.br = new MotorWrapper((DcMotor)hardwareMap.get("br"), MotorWrapper.MotorType.NEVEREST_40);
        this.drive = new TankDrive();
        this.drive.addLeftMotor(this.fl);
        this.drive.addLeftMotor(this.bl);
        this.drive.addRightMotor(this.fr);
        this.drive.addRightMotor(this.br);
    }
    public void teardown() {

    }
    public void stopMotion() {

    }

    public static abstract class MotorConstants {
        public static final double SCREW_BACKWARD = 1.0;
        public static final double SCREW_FORWARD = -SCREW_BACKWARD;

        public static final double DOOR_LEFT = 1.0;
        public static final double DOOR_RIGHT = -DOOR_LEFT;

        public static final double BUCKET_UP = 1.0;
        public static final double BUCKET_DOWN = -BUCKET_UP;

        public static final double LIFT_UP = 1.0;
        public static final double LIFT_DOWN = -LIFT_UP;
    }
}
