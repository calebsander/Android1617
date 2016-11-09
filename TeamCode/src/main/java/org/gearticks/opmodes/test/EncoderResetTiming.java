package org.gearticks.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.gearticks.opmodes.BaseOpMode;

//Findings: takes about 140ms to stop and reset the encoders
//Not sure whether it changes if you don't ask for the new value
@Autonomous
@Disabled
public class EncoderResetTiming extends BaseOpMode {
	private DcMotor motor;
	private double averageResetTime;
	private int resetTimes;

	protected void initialize() {
		this.motor = this.hardwareMap.dcMotor.iterator().next();
		this.resetTimes = 0;
	}
	protected void loopAfterStart() {
		if (this.getRuntime() < 1.0) this.motor.setPower(1.0);
		else {
			this.telemetry.clear();
			this.telemetry.addData("Encoder", this.motor.getCurrentPosition());
			final ElapsedTime resetTimer = new ElapsedTime();
			this.motor.setMode(RunMode.STOP_AND_RESET_ENCODER);
			this.motor.setMode(RunMode.RUN_WITHOUT_ENCODER);
			this.motor.getCurrentPosition();
			final double resetTime = resetTimer.seconds();
			if (this.resetTimes == 0) this.averageResetTime = resetTime;
			else this.averageResetTime = (this.averageResetTime * this.resetTimes + resetTime) / (this.resetTimes + 1);
			this.resetTimes++;
			this.telemetry.addData("Time", resetTime);
			this.telemetry.addData("Average", this.averageResetTime);
			this.resetStartTime();
		}
	}
}