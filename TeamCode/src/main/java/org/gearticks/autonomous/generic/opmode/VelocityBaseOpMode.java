package org.gearticks.autonomous.generic.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.opmodes.BaseOpMode;

@Autonomous
public class VelocityBaseOpMode extends BaseOpMode {
	protected VelocityConfiguration configuration;

	protected void initialize() {
        this.configuration = new VelocityConfiguration(this.hardwareMap);
        this.configuration.imu.eulerRequest.startReading();
	}

    protected void loopBeforeStart() {
        this.telemetry.addData("Heading", this.configuration.imu.getHeading());
    }

    protected void matchStart() {
        this.telemetry.clear();
        this.configuration.imu.resetHeading();
    }

    protected void matchEnd() {
        this.configuration.imu.eulerRequest.stopReading();
    }

}