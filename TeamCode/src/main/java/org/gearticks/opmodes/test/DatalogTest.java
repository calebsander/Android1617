package org.gearticks.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.gearticks.AutonomousDatalogger;
import org.gearticks.opmodes.BaseOpMode;

@Autonomous
@Disabled
public class DatalogTest extends BaseOpMode {
	private AutonomousDatalogger logger;
	private ElapsedTime time;
	private int loopCount;

	protected void initialize() {
		this.logger = new AutonomousDatalogger();
		this.time = new ElapsedTime();
		this.loopCount = 0;
	}
	protected void matchStart() {
		this.time.reset();
	}
	protected void loopAfterStart() {
		this.loopCount++;
		this.logger.writeLine(this.loopCount, this.time.seconds());
	}
	protected void matchEnd() {
		this.logger.close();
	}
}