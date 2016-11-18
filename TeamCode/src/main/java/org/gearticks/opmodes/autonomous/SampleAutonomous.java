package org.gearticks.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.gearticks.AutonomousDatalogger;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.joystickoptions.IncrementOption;
import org.gearticks.opmodes.BaseOpMode;

@Autonomous(name = "Sample Autonomous")
@Disabled
public class SampleAutonomous extends BaseOpMode {
	protected VelocityConfiguration configuration;
	private enum Stage {
		DELAY,
		STOPPED
	}
	private Stage stage;
	private IncrementOption delayOption;
	private AutonomousDatalogger datalogger;

	private void nextStage() {
		this.datalogger.writeLine(this.stage);
		this.resetStartTime();
		this.stage = Stage.values()[this.stage.ordinal() + 1];
	}
	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap);
		this.delayOption = new IncrementOption("Delay", 20.0);
		this.addOption(this.delayOption);
		this.datalogger = new AutonomousDatalogger();
		this.stage = Stage.values()[0];
	}
	protected void loopAfterStart() {
		switch (this.stage) {
			case DELAY:
				if (this.getRuntime() > this.delayOption.getValue()) this.nextStage();
				break;
			case STOPPED:
				this.configuration.stopMotion();
		}
		if (this.matchTime.seconds() > 30.0) this.stage = Stage.STOPPED;
		this.datalogger.writeLine(this.matchTime.seconds());
	}
	protected void matchEnd() {
		this.datalogger.close();
	}
}