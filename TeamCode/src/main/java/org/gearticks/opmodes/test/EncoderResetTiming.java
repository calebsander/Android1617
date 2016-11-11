package org.gearticks.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.gearticks.opmodes.BaseOpMode;

/*
Findings:
	-Takes about 125ms to stop and reset the encoders at full power
	-145ms if you ask for the new value too
	-Power doesn't seem to affect timing much
*/
@Autonomous
@Disabled
public class EncoderResetTiming extends BaseOpMode {
	private static final double POWER = 1.0;
	private DcMotor motor;
	private double averageResetTime;
	private int resetTimes;

	protected void initialize() {
		this.motor = this.hardwareMap.dcMotor.iterator().next();
		this.resetTimes = 0;
	}
	protected void loopAfterStart() {
		if (this.getRuntime() < 1.0) this.motor.setPower(POWER);
		else {
			this.telemetry.clear();
			this.telemetry.addData("Encoder", this.motor.getCurrentPosition());
			final ElapsedTime resetTimer = new ElapsedTime();
			this.motor.setMode(RunMode.STOP_AND_RESET_ENCODER);
			this.motor.setMode(RunMode.RUN_WITHOUT_ENCODER);
			this.motor.getCurrentPosition();
			final double resetTime = resetTimer.seconds();
			this.averageResetTime = (this.averageResetTime * this.resetTimes + resetTime) / (this.resetTimes + 1);
			this.resetTimes++;
			this.telemetry.addData("Time", resetTime);
			this.telemetry.addData("Average", this.averageResetTime);
			this.resetStartTime();
		}
	}
}