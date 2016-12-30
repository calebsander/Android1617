package org.gearticks.autonomous.generic.opmode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.opmodes.BaseOpMode;
import org.gearticks.opmodes.utility.Utils;

@Autonomous
public class VelocityBaseOpMode extends BaseOpMode {
	protected VelocityConfiguration configuration;

	protected void initialize() {
        Log.d(Utils.TAG, "Start OpMode initialize");
        this.configuration = new VelocityConfiguration(this.hardwareMap);
        this.configuration.imu.eulerRequest.startReading();
        Log.d(Utils.TAG, "End OpMode initialize");
	}

    protected void loopBeforeStart() {
        this.telemetry.addData("Heading", this.configuration.imu.getHeading());
    }

    protected void matchStart() {
        Log.d(Utils.TAG, "Starting OpMode matchStart");
        this.telemetry.clear();
        this.configuration.imu.resetHeading();
    }

    protected void matchEnd() {
        Log.d(Utils.TAG, "Starting OpMode matchEnd");
        if (this.configuration != null) {
            this.configuration.imu.eulerRequest.stopReading();
        }
    }

}