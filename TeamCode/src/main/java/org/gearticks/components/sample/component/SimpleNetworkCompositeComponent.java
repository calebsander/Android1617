package org.gearticks.components.sample.component;

import android.util.Log;
import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.statemachine.NetworkedStateMachine;
import org.gearticks.components.velocity.component.debug.DecisionDebugPause;
import org.gearticks.components.velocity.component.debug.DecisionDebugPause.XYTransition;
import org.gearticks.components.hardwareagnostic.component.GiroDriveEncoder;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.Utils;

/**
 * Sample to create a composite autonomous component that will consist of a non-linear state-machine of 2 or more AutonomousComponents
 */
public class SimpleNetworkCompositeComponent extends NetworkedStateMachine {

    public SimpleNetworkCompositeComponent(OpModeContext<VelocityConfiguration> opModeContext) {
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
