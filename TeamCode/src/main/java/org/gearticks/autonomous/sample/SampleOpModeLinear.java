package org.gearticks.autonomous.sample;

import java.util.ArrayList;
import java.util.List;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.velocity.components.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.Wait;
import org.gearticks.autonomous.generic.statemachine.network.LinearStateMachineFullImpl;

/**
 * This is a sample of a 'traditional' switch-based staging when using AutonomousComponents for some stages
 *
 */
public class SampleOpModeLinear extends OpModeTest {
	
	private LinearStateMachineFullImpl sm;


	@Override
	public void initialize(){
		List<AutonomousComponent> components = new ArrayList<>();
		components.add(new Wait(2000, null, "Wait for 2 sec"));
		components.add(new GiroDriveEncoder(2000, 100, 2000, null, "Drive for 2 sec heading 100"));
		components.add(new Wait(2000, null, "Wait for 2 sec"));
		sm = new LinearStateMachineFullImpl(components);
	}
	
	public void setup(){
		this.sm.setup(1);
	}
	
	public void loop(){
		
		this.sm.run();
		
	}
	
	
	public static void main(String[] args) {
		SampleOpModeLinear opMode = new SampleOpModeLinear();
		
		runOpMode(opMode, 10);
	}

}
