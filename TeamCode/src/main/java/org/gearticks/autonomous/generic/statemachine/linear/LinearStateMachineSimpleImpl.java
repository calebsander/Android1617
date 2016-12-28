package org.gearticks.autonomous.generic.statemachine.linear;

import android.support.annotation.NonNull;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.StateMachineAbstractImpl;

import java.util.List;
import java.util.ListIterator;

/**
 * Assumes all internal components form a linear/sequential chain.
 * Simplifies initialization of a sate-machine
 */
public class LinearStateMachineSimpleImpl extends StateMachineAbstractImpl implements LinearStateMachine {

    private ListIterator<AutonomousComponent> iterator;

    public LinearStateMachineSimpleImpl() {
        super();
    }
	public LinearStateMachineSimpleImpl(@NonNull List<AutonomousComponent> components) {
        super();
		this.addComponents(components);
	}

    @Override
    public void setup(int inputPort) {
        //Set currentState to first component
        this.iterator = this.components.listIterator();
        if (this.iterator.hasNext()) {
            this.currentState = this.iterator.next();
            this.currentState.setup();
            this.getLogger().info("Starting with " + this.currentState);
        }
        else {
            this.currentState = null;
            this.getLogger().warning("LinearStateMachine has no components. Cannot start " + this.getId());
        }
    }

    @Override
    public int run() {
        int outputPortNumber = 0;

        if (this.currentState == null){
            //If there is no (more) current state, then end this state-machine
            outputPortNumber = 1;
            this.getLogger().warning("LinearStateMachine in run() has no currentState. Ending StateMachine. "+ this.getId());
        }
        else {
            //regular 'run':
            int transition = this.currentState.run();

            //Check for transition:
            if (transition > 0) {
                //Get next component
                if (this.iterator.hasNext()) {
                    this.transitionToNextStage(this.iterator.next());
                }
                else {
                    //No more next component -> end of this state-machine
                    outputPortNumber = 1;
                    this.currentState.tearDown();
                    this.getLogger().info("Ended " + this.currentState);
                }
            }
        }

        return outputPortNumber;
    }

    private void transitionToNextStage(@NonNull AutonomousComponent nextStage){
        this.getLogger().info("Transition from " + this.currentState + " => " + nextStage);
        if (this.currentState != null) {
            this.currentState.tearDown();
        }
        this.currentState = nextStage;
        this.currentState.setup();
    }
}
