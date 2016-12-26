package org.gearticks.autonomous.generic.component;

import org.gearticks.autonomous.generic.statemachine.linear.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.linear.LinearStateMachineSimpleImpl;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;

import java.util.List;

public abstract class VelocityLinearBaseAutonomousComponent extends AutonomousComponentVelocityBase {
    private LinearStateMachine sm;

    public VelocityLinearBaseAutonomousComponent(VelocityConfiguration configuration, String id) {
        super(configuration, id);
        this.initializeStateMachine();
    }

    @Override
    public void setup(int inputPort) {
        super.setup(inputPort);
        this.sm.setup();
    }

    protected void initializeStateMachine(){
        List<AutonomousComponent> components = this.createComponents();
        sm = new LinearStateMachineSimpleImpl(components);
        this.sm.initialize();
    }

    protected abstract List<AutonomousComponent> createComponents();
}
