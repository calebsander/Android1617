package org.gearticks.autonomous.sample.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.gearticks.AutonomousDatalogger;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.generic.Stopped;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.joystickoptions.ValuesJoystickOption;

@Autonomous
@Disabled
public class SequencesAutonomousTest extends VelocityBaseOpMode {
	private static final int
		BRANCH_ONE_TRANSITION = AutonomousComponentAbstractImpl.newTransition(),
		BRANCH_TWO_TRANSITION = AutonomousComponentAbstractImpl.newTransition(),
		BRANCH_FOUR_TRANSITION = AutonomousComponentAbstractImpl.newTransition(),
		END_TRANSITION = AutonomousComponentAbstractImpl.newTransition();

	private enum FirstBranchOption {
		BRANCH_ONE,
		BRANCH_TWO
	}
	private ValuesJoystickOption<FirstBranchOption> firstBranchOption;
	private enum BranchFourOption {
		RUN_BRANCH_FOUR,
		NO_BRANCH_FOUR
	}
	private ValuesJoystickOption<BranchFourOption> branchFourOption;

	private class FirstBranchChoice extends AutonomousComponentAbstractImpl {
		@Override
		public int run() {
			final int superTransition = super.run();
			if (superTransition != NOT_DONE) return superTransition;

			switch (SequencesAutonomousTest.this.firstBranchOption.getRawSelectedOption()) {
				case BRANCH_ONE: return BRANCH_ONE_TRANSITION;
				case BRANCH_TWO: return BRANCH_TWO_TRANSITION;
				default: throw new RuntimeException("No first option selected");
			}
		}
	}
	private class BranchFourChoice extends AutonomousComponentAbstractImpl {
		@Override
		public int run() {
			final int superTransition = super.run();
			if (superTransition != NOT_DONE) return superTransition;

			switch (SequencesAutonomousTest.this.branchFourOption.getRawSelectedOption()) {
				case RUN_BRANCH_FOUR: return BRANCH_FOUR_TRANSITION;
				case NO_BRANCH_FOUR: return END_TRANSITION;
				default: throw new RuntimeException("No fourth branch option selected");
			}
		}
	}

	private class SimulatedStage extends AutonomousComponentAbstractImpl {
		private final int number;
		private int runCount;

		public SimulatedStage(int number) {
			super("Simulated stage " + number);
			this.number = number;
		}

		@Override
		public void onMatchStart() {
			super.onMatchStart();
			SequencesAutonomousTest.this.datalogger.writeLine(this.number, "Initialize");
		}
		@Override
		public void setup() {
			super.setup();
			this.runCount = 0;
			SequencesAutonomousTest.this.datalogger.writeLine(this.number, "Setup");
		}
		@Override
		public int run() {
			final int superTransition = super.run();
			if (superTransition != NOT_DONE) return superTransition;

			this.runCount++;
			SequencesAutonomousTest.this.datalogger.writeLine(this.number, "Run", this.runCount);
			if (this.runCount >= this.number) return NEXT_STATE;
			else return NOT_DONE;
		}
		@Override
		public void tearDown() {
			super.tearDown();
			SequencesAutonomousTest.this.datalogger.writeLine(this.number, "Teardown");
		}
	}

	private AutonomousDatalogger datalogger;
	private NetworkedStateMachine sm;

	protected AutonomousComponent getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
		/*
			Branch 1: 1, 2, 3
			Branch 2: 4, 5, 6
			Branch 3: 7
			Branch 4: 8

			Either branch 1 or branch 2 is run
			Branch 3 is run
			Branch 4 may or may not be run
		*/
		final AutonomousComponent entranceComponent = new FirstBranchChoice();
		final LinearStateMachine branch1 = new LinearStateMachine("branch1");
		branch1.addComponent(new SimulatedStage(1));
		branch1.addComponent(new SimulatedStage(2));
		branch1.addComponent(new SimulatedStage(3));
		final LinearStateMachine branch2 = new LinearStateMachine("branch2");
		branch2.addComponent(new SimulatedStage(4));
		branch2.addComponent(new SimulatedStage(5));
		branch2.addComponent(new SimulatedStage(6));
		final LinearStateMachine branch3 = new LinearStateMachine("branch3");
		branch3.addComponent(new SimulatedStage(7));
		branch3.addComponent(new BranchFourChoice());
		final LinearStateMachine branch4 = new LinearStateMachine("branch4");
		branch4.addComponent(new SimulatedStage(8));
		final AutonomousComponent stopped = new Stopped(opModeContext);

		this.sm = new NetworkedStateMachine("sm");
		this.sm.setInitialComponent(entranceComponent);
		this.sm.addConnection(entranceComponent, BRANCH_ONE_TRANSITION, branch1);
		this.sm.addConnection(entranceComponent, BRANCH_TWO_TRANSITION, branch2);
		this.sm.addConnection(branch1, AutonomousComponentAbstractImpl.NEXT_STATE, branch3);
		this.sm.addConnection(branch2, AutonomousComponentAbstractImpl.NEXT_STATE, branch3);
		this.sm.addConnection(branch3, BRANCH_FOUR_TRANSITION, branch4);
		this.sm.addConnection(branch3, END_TRANSITION, stopped);
		this.sm.addConnection(branch4, AutonomousComponentAbstractImpl.NEXT_STATE, stopped);
		return this.sm;
	}
	protected boolean isV2() {
		return false;
	}

	@Override
	protected void initialize() {
		this.datalogger = new AutonomousDatalogger();
		this.datalogger.writeLine("Init");
		super.initialize();
		this.firstBranchOption = new ValuesJoystickOption<>("First Branch", FirstBranchOption.values());
		this.addOption(this.firstBranchOption);
		this.branchFourOption = new ValuesJoystickOption<>("Branch 4?", BranchFourOption.values());
		this.addOption(this.branchFourOption);
	}

	@Override
	protected void matchStart() {
		this.datalogger.writeLine("Start");
		super.matchStart();
	}

	@Override
	protected void loopAfterStart() {
		this.datalogger.writeLine("Looping", this.sm);
		super.loopAfterStart();
	}

	@Override
	protected void matchEnd() {
		this.datalogger.writeLine("End");
		this.datalogger.close();
		super.matchEnd();
	}
}