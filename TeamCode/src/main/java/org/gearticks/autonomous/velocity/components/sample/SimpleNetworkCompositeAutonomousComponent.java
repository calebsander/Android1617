package org.gearticks.autonomous.velocity.components.sample;

import android.util.Log;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.experimental.DecisionDebugPause;
import org.gearticks.autonomous.velocity.components.experimental.DecisionDebugPause.XYTransition;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.Utils;

/**
 * Sample to create a composite autonomous component that will consist of a non-linear state-machine of 2 or more AutonomousComponents
 */
public class SimpleNetworkCompositeAutonomousComponent extends NetworkedStateMachine {

    public SimpleNetworkCompositeAutonomousComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        super();
        Log.d(Utils.TAG, "begin full state machine initialization of SimpleNetworkCompositeAutonomousComponent");
        final DecisionDebugPause debugDecision = new DecisionDebugPause(opModeContext, "Wait until X of Y are pressed");
        final GiroDriveEncoder driveForward = new GiroDriveEncoder(0.0, 0.7, 1000, opModeContext, "Drive forward");
        final GiroDriveEncoder driveBackward = new GiroDriveEncoder(0.0, -0.7, 1000, opModeContext, "Drive backward");

        this.setInitialComponent(debugDecision);
        this.addConnection(debugDecision, XYTransition.X, driveForward);
        this.addConnection(debugDecision, XYTransition.Y, driveBackward);
        this.addExitConnection(driveForward, DefaultTransition.DEFAULT);
        this.addExitConnection(driveBackward, DefaultTransition.DEFAULT);

        Log.d(Utils.TAG, "end full state machine initialization of SimpleNetworkCompositeAutonomousComponent");
    }
}
