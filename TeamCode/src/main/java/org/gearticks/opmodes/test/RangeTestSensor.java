package org.gearticks.opmodes.test;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.opmodes.BaseOpMode;

@Autonomous(name = "RangeTestSensor", group = "test")

public class RangeTestSensor extends BaseOpMode {
    private ModernRoboticsI2cRangeSensor sensor;
    protected void initialize() {
        this.sensor = (ModernRoboticsI2cRangeSensor)hardwareMap.get("range");
    }
    protected void loopAfterStart() {
        this.telemetry.addData("Ultrasonic", this.sensor.cmUltrasonic());
        this.telemetry.addData("Optical", this.sensor.cmOptical());
        this.telemetry.update();
    }
}
