package org.gearticks.autonomous.sample;

import java.util.ArrayList;
import java.util.List;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.GyroDriveEncoder;
import org.gearticks.autonomous.velocity.components.Wait;

/**
 * This is a sample of a 'traditional' switch-based staging when using AutonomousComponents for some stages
 *
 */
public class SampleOpModeLinear extends OpModeTest {

	private LinearStateMachine sm;

	@Override
	public void initialize() {
		final List<AutonomousComponent> components = new ArrayList<>();
		components.add(new Wait(2.0, "Wait for 2 sec"));
		components.add(new GyroDriveEncoder(2000, 100, 2000, null, "Drive for 2 sec heading 100"));
		components.add(new Wait(2.0, "Wait for 2 sec"));
		this.sm = new LinearStateMachine(components);
	}

	public void setup() {
		this.sm.setup();
	}

	public void loop() {
		this.sm.run();
	}


	public static void main(String[] args) {
		SampleOpModeLinear opMode = new SampleOpModeLinear();

		runOpMode(opMode, 10);
	}

}
