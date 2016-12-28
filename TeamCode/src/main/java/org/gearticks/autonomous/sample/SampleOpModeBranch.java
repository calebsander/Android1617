package org.gearticks.autonomous.sample;

import android.support.annotation.NonNull;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.velocity.components.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.Wait;
import org.gearticks.autonomous.generic.statemachine.network.InputPort;
import org.gearticks.autonomous.generic.statemachine.network.OutputPort;
import org.gearticks.autonomous.generic.statemachine.network.StateMachineFullImpl;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * This is a sample of a state-machine with branching.
 * Need to use StateMachineFullImpl with connections
 *
 */
public class SampleOpModeBranch extends OpModeTest {
	
	private StateMachineFullImpl sm;


	@Override
	public void initialize(){
		this.sm = this.createSampleStateMachine();
	}
	
	/**
	 * Creates a StateMachineFullImpl with a branch.
	 * Step 1: drive forward and stop
	 * Step 2: observe color, if red then Step 3-red, else Step3-blue
	 * Step 3-red: turn and drive to red
	 * Step3-blue: turn and drive to blue
	 * @return
	 */
	@NonNull
    public StateMachineFullImpl createSampleStateMachine(){
		StateMachineFullImpl sm = new StateMachineFullImpl(1,1);

		AutonomousComponent drive1 = new GiroDriveEncoder(2000, 100, 2000, new VelocityConfiguration(null), "Drive for 2 sec heading 100");
		AutonomousComponent driveRed = new GiroDriveEncoder(3000, 0, 2000, null, "Drive for 3 sec left to red");
		AutonomousComponent driveBlue = new GiroDriveEncoder(3000, 180, 20000, null, "Drive for 3 sec right to blue");
		AutonomousComponent observeColor = new Wait(2000, null, "Wait for 2 sec");

		sm.addComponent(drive1);
		sm.addComponent(observeColor);
		sm.addComponent(driveRed);
		sm.addComponent(driveBlue);

		InputPort inputPort = sm.getInputPort(1);
		OutputPort outputPort1 = sm.getOutputPort(1);
		OutputPort outputPort2 = sm.getOutputPort(2);
		sm.addConnection(inputPort, 1, observeColor, 1);
		sm.addConnection(drive1, 1, drive1, 1);
		sm.addConnection(drive1, 1, observeColor, 1);
		sm.addConnection(observeColor, 1, driveRed, 1); //1 == red
		sm.addConnection(observeColor, 2, driveBlue, 1); //2 == blue
		sm.addConnection(driveRed, 1, outputPort1, 1);
		sm.addConnection(driveBlue, 1, outputPort2, 1);

		return sm;
	}
	
	public void setup(){
		this.sm.setup(1);
	}
	
	public void loop(){
		
		this.sm.run();
		
	}
	
	
	public static void main(String[] args) {
		SampleOpModeBranch opMode = new SampleOpModeBranch();
		
		runOpMode(opMode, 10);
	}

}
