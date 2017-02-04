package org.gearticks.opmodes.test.sensors;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp
@Disabled
public class AnalogInputLogging extends BaseOpMode {
	private AnalogInput in;

	protected void initialize() {
		this.in = hardwareMap.analogInput.iterator().next();
	}
	protected void loopAfterStart() {
		this.telemetry.addData("Voltage", this.in.getVoltage());
	}
}