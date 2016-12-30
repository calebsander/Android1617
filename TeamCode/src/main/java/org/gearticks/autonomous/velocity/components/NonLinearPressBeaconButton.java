package org.gearticks.autonomous.velocity.components;

import android.support.annotation.NonNull;
import android.util.Log;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.gearticks.GamepadWrapper;
import org.gearticks.Vuforia.VuforiaConfiguration;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.VelocityNonLinearBaseAutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.StateMachine;
import org.gearticks.autonomous.generic.statemachine.network.InputPort;
import org.gearticks.autonomous.generic.statemachine.network.OutputPort;
import org.gearticks.autonomous.generic.statemachine.network.StateMachineFullImpl;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.opmodes.utility.Utils;

/**
 * Sample to create a composite autonomous component that will consist of a linear state-machine of 2 or more AutonomousComponents
 */
public class NonLinearPressBeaconButton extends VelocityNonLinearBaseAutonomousComponent {
    private final VuforiaConfiguration vuforiaConfiguration;


    public NonLinearPressBeaconButton(@NonNull VuforiaConfiguration vuforiaConfiguration, @NonNull VelocityConfiguration configuration, String id) {
        super(configuration, id);
        this.vuforiaConfiguration = Utils.assertNotNull(vuforiaConfiguration);

        /*
        Problem: initializeStateMachine (in our case) requires this.telemetry not to be null!
        So ideally would like to set this.telemetry before calling super(), but Java doesn't allow this.
         */
        this.sm = this.initializeStateMachine();
    }

    protected StateMachine initializeStateMachine(){
        Log.d(Utils.TAG, "begin full state machine initialization of SimpleNonLinearComposite");
        StateMachineFullImpl sm = new StateMachineFullImpl(1, 1);

        AutonomousComponent selectSide = new SelectBlueSideBeaconButton(this.vuforiaConfiguration, this.getConfiguration(), "Press beacon button");
        AutonomousComponent pushRightButton = new GiroTurnBeacon(10.0, this.getConfiguration(), "Turn right");
        AutonomousComponent pushLeftButton = new GiroTurnBeacon(-10.0, this.getConfiguration(), "Turn left");
        AutonomousComponent driveInRightButton = new GiroDriveEncoderBeacon(10.0, 0.3, 200, this.getConfiguration(), "Press right button");
        AutonomousComponent driveInLeftButton = new GiroDriveEncoderBeacon(-10.0, 0.3, 200, this.getConfiguration(), "Press left button");
        AutonomousComponent squareUp = new GiroTurnBeacon(0.0, this.getConfiguration(), "Square up");


        sm.addComponent(selectSide);
        sm.addComponent(pushRightButton);
        sm.addComponent(pushLeftButton);

        InputPort inputPort = sm.getInputPort(1);
        OutputPort outputPort1 = sm.getOutputPort(1);
        sm.addConnection(inputPort, 1, selectSide, 1);
        sm.addConnection(selectSide, 1, pushRightButton, 1);
        sm.addConnection(selectSide, 2, pushLeftButton, 1);
        sm.addConnection(selectSide, 3, outputPort1, 1);
        sm.addConnection(pushRightButton, 1, driveInRightButton, 1);
        sm.addConnection(pushLeftButton, 1, driveInLeftButton, 1);
        sm.addConnection(driveInRightButton, 1, squareUp, 1);
        sm.addConnection(driveInLeftButton, 1, squareUp, 1);
        sm.addConnection(squareUp, 1, outputPort1, 1);


        Log.d(Utils.TAG, "end full state machine initialization of SimpleNonLinearComposite");
        return sm;

    }
}
