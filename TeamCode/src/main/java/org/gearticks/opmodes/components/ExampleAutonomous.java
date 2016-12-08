package org.gearticks.opmodes.components;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.joystickoptions.IncrementOption;
import org.gearticks.opmodes.BaseOpMode;

@Autonomous
public class ExampleAutonomous extends BaseOpMode {
	private VelocityConfiguration configuration;
	private IncrementOption delayOption;
	private enum Stage implements AutonomousComponent<ExampleAutonomous> {
		DELAY {
			private DelayComponent delayComponent;
			public void init(ExampleAutonomous opMode) {
				this.delayComponent = new DelayComponent(opMode.delayOption);
			}
			public void loop(ExampleAutonomous opMode) {}
			public boolean isDone(ExampleAutonomous opMode) {
				return this.delayComponent.isDone();
			}
			public void stop(ExampleAutonomous opMode) {}
		},
		DRIVE_FORWARD {
			private EncoderComponent encoderComponent;
			public void init(ExampleAutonomous opMode) {
				this.encoderComponent = new EncoderComponent(opMode.configuration, 1700, 0.7);
				this.encoderComponent.init();
			}
			public void loop(ExampleAutonomous opMode) {
				this.encoderComponent.loop();
			}
			public boolean isDone(ExampleAutonomous opMode) {
				return this.encoderComponent.isDone();
			}
			public void stop(ExampleAutonomous opMode) {
				this.encoderComponent.stop();
			}
		},
		STOPPED {
			private StoppedComponent stoppedComponent;
			public void init(ExampleAutonomous opMode) {
				this.stoppedComponent = new StoppedComponent(opMode.configuration);
			}
			public void loop(ExampleAutonomous opMode) {
				this.stoppedComponent.loop();
			}
			public boolean isDone(ExampleAutonomous opMode) {
				return this.stoppedComponent.isDone();
			}
			public AutonomousComponent<ExampleAutonomous> getNextComponent(ExampleAutonomous opMode) {
				throw new RuntimeException("Cannot get next stage of STOPPED");
			}
			public void stop(ExampleAutonomous opMode) {}
		};

		//Unless specifically overridden, will always advance to the next stage
		public AutonomousComponent<ExampleAutonomous> getNextComponent(ExampleAutonomous opMode) {
			return opMode.getNextStage();
		}
	}
	private Stage stage;

	protected void initialize() {
		this.configuration = new VelocityConfiguration(this.hardwareMap);
		this.delayOption = new IncrementOption("Delay", 15.0);
		this.stage = null;
		this.switchToComponent(Stage.values()[0]);
	}
	protected void loopAfterStart() {
		this.stage.loop(this);
		if (this.stage.isDone(this)) this.switchToComponent(this.stage.getNextComponent(this));
	}

	private Stage getNextStage() {
		return Stage.values()[this.stage.ordinal() + 1];
	}
	private void switchToComponent(AutonomousComponent<ExampleAutonomous> nextComponent) {
		if (this.stage != null) this.stage.stop(this);
		this.stage = (Stage)nextComponent;
		this.stage.init(this);
	}
}