package org.gearticks.autonomous.generic.component;

import android.support.annotation.NonNull;

import org.gearticks.autonomous.generic.statemachine.StateMachine;
import org.gearticks.autonomous.generic.statemachine.linear.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.linear.LinearStateMachineSimpleImpl;
import org.gearticks.autonomous.generic.statemachine.network.StateMachineFullImpl;
import org.gearticks.hardware.configurations.VelocityConfiguration;

import java.util.List;

/**
 * AutonomousComponent that contains a Full State-Machine.
 * Can be used to create composite components that are full/non-linear state-machines of other components
 */
public abstract class VelocityNonLinearBaseAutonomousComponent extends AutonomousComponentVelocityBase {
    private final StateMachine sm;

    public VelocityNonLinearBaseAutonomousComponent(@NonNull VelocityConfiguration configuration, String id) {
        super(configuration, id);
        this.sm = this.initializeStateMachine();
    }

    protected abstract StateMachine initializeStateMachine();


    @Override
    public void initializeAtMatchStart(){
        super.initializeAtMatchStart();
        this.sm.initializeAtMatchStart();
    }

    @Override
    public void setup(int inputPort) {
        super.setup(inputPort);
        this.sm.setup();
    }
    @Override
    public int run() {
        int transition = super.run();

        if (this.sm.run() > 0) {
            transition = 1;
        }

        return transition;
    }

    @Override
    public void tearDown() {
        super.tearDown();
        this.sm.tearDown();
    }
}
