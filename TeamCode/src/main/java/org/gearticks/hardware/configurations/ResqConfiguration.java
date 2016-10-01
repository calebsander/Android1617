package org.gearticks.hardware.configurations;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.hardware.drive.TankDrive;

public class ResqConfiguration implements HardwareConfiguration {
    public final DcMotor fl, fr, bl, br;
    public final TankDrive drive;
    public ResqConfiguration(HardwareMap hardwareMap) {
        this.fl = (DcMotor)hardwareMap.get("fl");
        this.fr = (DcMotor)hardwareMap.get("fr");
        this.bl = (DcMotor)hardwareMap.get("bl");
        this.br = (DcMotor)hardwareMap.get("br");
        this.drive = new TankDrive();
        this.drive.addLeftMotor(new MotorWrapper(this.fl, MotorWrapper.MotorType.NEVEREST_40));
        this.drive.addLeftMotor(new MotorWrapper(this.bl, MotorWrapper.MotorType.NEVEREST_40));
        this.drive.addRightMotor(new MotorWrapper(this.fr, MotorWrapper.MotorType.NEVEREST_40));
        this.drive.addRightMotor(new MotorWrapper(this.br, MotorWrapper.MotorType.NEVEREST_40));
    }
    public void teardown() {

    }
    public void stopMotion() {

    }
}
