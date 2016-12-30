package org.gearticks.autonomous.generic.opmode;

import android.util.Log;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.linear.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.linear.LinearStateMachineSimpleImpl;
import org.gearticks.opmodes.utility.Utils;

import java.util.List;

/**
 * Includes a linear state-machine
 */
public abstract class VelocityLinearAutonomousBaseOpMode extends VelocityBaseOpMode {
    private final LinearStateMachine sm = new LinearStateMachineSimpleImpl();

    protected void initialize() {
        Log.d(Utils.TAG, "Start VelocityLinearAutonomousBaseOpMode initialize");
        super.initialize();
        Log.d(Utils.TAG, "Start VelocityLinearAutonomousBaseOpMode initialize - createComponents");
        sm.addComponents(this.createComponents());
        Log.d(Utils.TAG, "End VelocityLinearAutonomousBaseOpMode initialize");
    }

    protected void loopBeforeStart() {
        super.loopBeforeStart();
    }

    protected void matchStart() {
        super.matchStart();
        this.sm.initializeAtMatchStart();
        this.sm.setup();
    }

    protected void loopAfterStart(){
        super.loopAfterStart();
        this.sm.run();
    }

    protected void matchEnd() {
        super.matchEnd();
        this.sm.tearDown();
    }

    /**
     * Create sorted list of AutonomousComponents
     * @return
     */
    protected abstract List<AutonomousComponent> createComponents();
}
