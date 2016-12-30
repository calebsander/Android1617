package org.gearticks.autonomous.velocity.components;

import android.support.annotation.NonNull;
import android.util.Log;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.gearticks.GamepadWrapper;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.VelocityLinearBaseAutonomousComponent;
import org.gearticks.autonomous.generic.component.VelocityNonLinearBaseAutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.StateMachine;
import org.gearticks.autonomous.generic.statemachine.network.InputPort;
import org.gearticks.autonomous.generic.statemachine.network.LinearStateMachineFullImpl;
import org.gearticks.autonomous.generic.statemachine.network.OutputPort;
import org.gearticks.autonomous.generic.statemachine.network.StateMachineFullImpl;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.opmodes.utility.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Sample to create a composite autonomous component that will consist of a linear state-machine of 2 or more AutonomousComponents
 */
public class SimpleNonLinearComposite extends VelocityNonLinearBaseAutonomousComponent {
    private final GamepadWrapper[] gamepads;
    private final Telemetry telemetry;

    public SimpleNonLinearComposite(@NonNull GamepadWrapper[] gamepads, @NonNull Telemetry telemetry, @NonNull VelocityConfiguration configuration, String id) {
        super(configuration, id);
        this.gamepads = Utils.assertNotNull(gamepads);
        this.telemetry = Utils.assertNotNull(telemetry);
        /*
        Problem: initializeStateMachine (in our case) requires this.telemetry not to be null!
        So ideally would like to set this.telemetry before calling super(), but Java doesn't allow this.
         */
        this.sm = this.initializeStateMachine();
    }

    protected StateMachine initializeStateMachine(){
        Log.d(Utils.TAG, "begin full state machine initialization of SimpleNonLinearComposite");
        StateMachineFullImpl sm = new StateMachineFullImpl(1, 1);

        Utils.assertNotNull(this.telemetry);
        AutonomousComponent debugDecision = new DecisionDebugPause(this.gamepads, this.telemetry, this.getConfiguration(), "Wait until X of Y are pressed");
        AutonomousComponent driveForward = new GiroDriveEncoder(0.0, 0.7, 1000, this.getConfiguration(), "Drive forward");
        AutonomousComponent driveBackward = new GiroDriveEncoder(0.0, -0.7, 1000, this.getConfiguration(), "Drive backward");

        sm.addComponent(debugDecision);
        sm.addComponent(driveForward);
        sm.addComponent(driveBackward);

        InputPort inputPort = sm.getInputPort(1);
        OutputPort outputPort1 = sm.getOutputPort(1);
        sm.addConnection(inputPort, 1, debugDecision, 1);
        sm.addConnection(debugDecision, 1, driveForward, 1);
        sm.addConnection(debugDecision, 2, driveBackward, 1);
        sm.addConnection(driveForward, 1, outputPort1, 1);
        sm.addConnection(driveBackward, 1, outputPort1, 1);


        Log.d(Utils.TAG, "end full state machine initialization of SimpleNonLinearComposite");
        return sm;

    }
}
