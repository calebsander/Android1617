package org.gearticks.autonomous.velocity.components;

import android.support.annotation.NonNull;
import android.util.Log;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.gearticks.GamepadWrapper;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.opmodes.utility.Utils;

/**
 * Sample to create a composite autonomous component that will consist of a linear state-machine of 2 or more AutonomousComponents
 */
public class SimpleNonLinearComposite extends NetworkedStateMachine {

    public SimpleNonLinearComposite(@NonNull GamepadWrapper[] gamepads, @NonNull Telemetry telemetry, @NonNull VelocityConfiguration configuration) {
        super();
        Utils.assertNotNull(gamepads);
        Utils.assertNotNull(telemetry);
        Log.d(Utils.TAG, "begin full state machine initialization of SimpleNonLinearComposite");
        final AutonomousComponent debugDecision = new DecisionDebugPause(gamepads, telemetry, configuration, "Wait until X of Y are pressed");
        final AutonomousComponent driveForward = new GiroDriveEncoder(0.0, 0.7, 1000, configuration, "Drive forward");
        final AutonomousComponent driveBackward = new GiroDriveEncoder(0.0, -0.7, 1000, configuration, "Drive backward");

        this.setInitialComponent(debugDecision);
        this.addConnection(debugDecision, DecisionDebugPause.X_TRANSITION, driveForward);
        this.addConnection(debugDecision, DecisionDebugPause.Y_TRANSITION, driveBackward);
        this.addExitConnection(driveForward, NEXT_STATE);
        this.addExitConnection(driveBackward, NEXT_STATE);

        Log.d(Utils.TAG, "end full state machine initialization of SimpleNonLinearComposite");
    }
}
