package org.gearticks.opmodes.test;


import org.gearticks.hardware.configurations.ResqConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.opmodes.BaseOpMode;

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
    }
}
