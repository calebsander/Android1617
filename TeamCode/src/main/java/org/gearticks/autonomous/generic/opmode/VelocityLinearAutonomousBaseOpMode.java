package org.gearticks.autonomous.generic.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.opmode.VelocityBaseOpMode;
import org.gearticks.autonomous.generic.statemachine.linear.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.linear.LinearStateMachineSimpleImpl;
import org.gearticks.autonomous.velocity.components.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.Wait;

import java.util.ArrayList;
import java.util.List;

/**
 * Includes a linear state-machine
 */
public abstract class VelocityLinearAutonomousBaseOpMode extends VelocityBaseOpMode {
    private LinearStateMachine sm;

    protected void initialize() {
        super.initialize();
        this.initializeStateMachine();
    }

    protected void loopBeforeStart() {
        super.loopBeforeStart();
    }

    protected void matchStart() {
        super.matchStart();
        this.sm.setup();
    }

    protected void loopAfterStart(){
        super.loopAfterStart();
        this.sm.run();
    }

    protected void matchEnd() {
        super.matchEnd();
        //Some matchEnd call happened with this.sm was null
        if (this.sm != null) {
            this.sm.tearDown();
        }
    }

    protected void initializeStateMachine(){
        List<AutonomousComponent> components = this.createComponents();
        sm = new LinearStateMachineSimpleImpl(components);
        this.sm.initialize();
    }

    /**
     * Create sorted list of AutonomousComponents
     * @return
     */
    protected abstract List<AutonomousComponent> createComponents();
}
