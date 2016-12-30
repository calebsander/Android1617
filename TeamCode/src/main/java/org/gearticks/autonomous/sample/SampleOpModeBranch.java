package org.gearticks.autonomous.sample;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.sample.components.ObserveColor;
import org.gearticks.autonomous.velocity.components.GyroDriveEncoder;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import static org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl.NEXT_STATE;

/**
 * This is a sample of a state-machine with branching.
 * Need to use StateMachineFullImpl with connections
 *
 */
public class SampleOpModeBranch extends OpModeTest {

	private NetworkedStateMachine stateMachine;

	@Override
	public void initialize() {
		this.stateMachine = this.createSampleStateMachine();
	}

	/**
	 * Creates a StateMachineFullImpl with a branch.
	 * Step 1: drive forward and stop
	 * Step 2: observe color, if red then Step 3-red, else Step3-blue
	 * Step 3-red: turn and drive to red
	 * Step3-blue: turn and drive to blue
	 * @return
	 */
	public NetworkedStateMachine createSampleStateMachine() {
		final VelocityConfiguration configuration = new VelocityConfiguration(null);
		final AutonomousComponent drive1 = new GyroDriveEncoder(0.0, 1.0, 2000, configuration, "Drive for 2 sec heading 100");
		final AutonomousComponent observeColor = new ObserveColor(configuration, "Observe color");
		final AutonomousComponent driveRed = new GyroDriveEncoder(-90.0, 1.0, 2000, configuration, "Drive for 3 sec left to red");
		final AutonomousComponent driveBlue = new GyroDriveEncoder(90.0, 1.0, 2000, configuration, "Drive for 3 sec right to blue");

		final NetworkedStateMachine sm = new NetworkedStateMachine(drive1);
		sm.addConnection(drive1, NEXT_STATE, observeColor);
		sm.addConnection(observeColor, ObserveColor.RED, driveRed);
		sm.addConnection(observeColor, ObserveColor.BLUE, driveBlue);
		sm.addExitConnection(driveRed, NEXT_STATE, ObserveColor.RED);
		sm.addExitConnection(driveBlue, NEXT_STATE, ObserveColor.BLUE);
		return sm;
	}

	public void setup(){
		this.stateMachine.setup();
	}

	public void loop(){
		this.stateMachine.run();

	}


	public static void main(String[] args) {
		SampleOpModeBranch opMode = new SampleOpModeBranch();

		runOpMode(opMode, 10);
	}

}
