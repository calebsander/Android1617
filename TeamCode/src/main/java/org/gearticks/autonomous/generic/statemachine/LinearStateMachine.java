package org.gearticks.autonomous.generic.statemachine;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl;
import java.util.List;
import java.util.ListIterator;

/**
 * Assumes all internal components form a linear/sequential chain.
 * Simplifies initialization of a sate-machine
 */
public class LinearStateMachine extends StateMachineAbstractImpl {

    private ListIterator<AutonomousComponent> iterator;

    public LinearStateMachine(List<AutonomousComponent> components) {
        super();
        this.addComponents(components);
    }

    @Override
    public void setup() {
        //Set currentState to first component
        this.iterator = this.components.listIterator();
        if (this.iterator.hasNext()) {
            this.getLogger().info("Starting with " + this.currentState);
            this.transitionToNextStage();
        }
        else {
            this.currentState = null;
            this.getLogger().warning("LinearStateMachine has no components. Cannot start " + this.getId());
        }
    }

    @Override
    public int run() {
        final int superTransition = super.run();
        if (superTransition != NOT_DONE) return superTransition;

        final int outputPortNumber;
        if (this.currentState == null) {
            //If there is no (more) current state, then end this state-machine
            outputPortNumber = NEXT_STATE;
            this.getLogger().warning("LinearStateMachine in run() has no currentState " + this.getId());
        }
        else {
            //regular 'run':
            final int transition = this.currentState.run();

            //Check for transition:
            if (transition == NOT_DONE) outputPortNumber = NOT_DONE;
            else {
                //Get next component
                if (this.iterator.hasNext()) {
                    this.transitionToNextStage();
                    outputPortNumber = NOT_DONE;
                }
                else {
                    //No more next component -> end of this state-machine
                    outputPortNumber = NEXT_STATE;
                }
            }
        }
        return outputPortNumber;
    }

    private void transitionToNextStage() {
        final AutonomousComponent nextStage = this.iterator.next();
        this.getLogger().info("Transition from " + this.currentState + " => " + nextStage);
        if (this.currentState != null) this.currentState.tearDown();
        this.currentState = nextStage;
        nextStage.setup();
    }
}
