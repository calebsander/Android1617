package org.gearticks.autonomous.generic.statemachine;

import android.util.Log;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.opmodes.utility.Utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Assumes all internal components form a linear/sequential chain.
 * Simplifies initialization of a state-machine
 */
public class LinearStateMachine extends StateMachineBase {
    private Iterator<AutonomousComponent> iterator;

    public LinearStateMachine() {
        this(new ArrayList<AutonomousComponent>());
    }
    public LinearStateMachine(List<AutonomousComponent> components) {
        super(components);
    }
    public LinearStateMachine(String id) {
        this(new ArrayList<AutonomousComponent>(), id);
    }
    public LinearStateMachine(List<AutonomousComponent> components, String id) {
        super(components, id);
    }

    /**
     * Adds a component to the end of the chain.
     * Must be called before the state machine gets initialized.
     * @param component the component to add
    */
    public void addComponent(AutonomousComponent component) {
        this.components.add(component);
    }

    @Override
    public void setup() {
        //Set currentState to first component
        this.iterator = this.components.iterator();
        if (this.iterator.hasNext()) {
            Log.i(Utils.TAG, "Starting with " + this.currentState);
            this.transitionToNextStage();
        }
        else {
            this.currentState = null;
            Log.w(Utils.TAG, "LinearStateMachine has no components. Cannot start " + this);
        }
    }

    @Override
    public Transition run() {
        final Transition superTransition = super.run();
        if (superTransition != null) return superTransition;

        if (this.currentState == null) {
            //If there is no (more) current state, then end this state-machine
            Log.w(Utils.TAG, "LinearStateMachine in run() has no currentState " + this);
            return NEXT_STATE;
        }

        //Delegate run() to current state
        final Transition transition = this.currentState.run();
        //If no transition, we're done
        if (transition == null) return null;

        //If the component is done, get the next component
        if (this.iterator.hasNext()) {
            this.transitionToNextStage();
            return null;
        }
        else {
            this.currentState.tearDown();
            //No more components -> end of this state-machine
            Log.i(Utils.TAG, "Exiting " + this + " with transition: " + transition);
            this.currentState = null;
            return transition;
        }
    }

    private void transitionToNextStage() {
        final AutonomousComponent nextState = this.iterator.next();
        Log.i(Utils.TAG, "Transition from " + this.currentState + " => " + nextState);
        if (this.currentState != null) this.currentState.tearDown();
        this.currentState = nextState;
        nextState.setup();
    }
}
