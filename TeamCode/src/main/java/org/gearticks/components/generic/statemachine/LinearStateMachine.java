package org.gearticks.components.generic.statemachine;

import android.util.Log;
import org.gearticks.components.generic.component.OpModeComponent;
import org.gearticks.Utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Assumes all internal components form a linear/sequential chain.
 * Simplifies initialization of a state-machine
 */
public class LinearStateMachine extends StateMachineBase {
    private Iterator<OpModeComponent<?>> iterator;

    public LinearStateMachine() {
        this(new ArrayList<>());
    }
    public LinearStateMachine(List<OpModeComponent<?>> components) {
        super(components);
    }
    public LinearStateMachine(String id) {
        this(new ArrayList<>(), id);
    }
    public LinearStateMachine(List<OpModeComponent<?>> components, String id) {
        super(components, id);
    }

    /**
     * Adds a component to the end of the chain.
     * Must be called before the state machine gets initialized.
     * @param component the component to add
    */
    public void addComponent(OpModeComponent<?> component) {
        this.components.add(component);
    }

    @Override
    public void setup() {
        //Set currentState to first component
        this.iterator = this.components.iterator();
        if (this.iterator.hasNext()) {
            Log.i(Utils.TAG, "Starting with \"" + this.currentState + "\"");
            this.transitionToNextStage();
        }
        else {
            this.currentState = null;
            Log.w(Utils.TAG, "LinearStateMachine has no components. Cannot start \"" + this + "\"");
        }
    }

    @Override
    public DefaultTransition run() {
        final DefaultTransition superTransition = super.run();
        if (superTransition != null) return superTransition;

        if (this.currentState == null) {
            //If there is no (more) current state, then end this state-machine
            Log.w(Utils.TAG, "LinearStateMachine in run() has no currentState \"" + this + "\"");
            return DefaultTransition.DEFAULT;
        }

        //Delegate run() to current state
        final Enum<?> transition = this.currentState.run();
        //If no transition, no need to advance state
        if (transition == null) return null;

        //If the component is done, get the next component
        if (this.iterator.hasNext()) {
            this.transitionToNextStage();
            return null;
        }
        else {
            this.currentState.tearDown();
            //No more components -> end of this state-machine
            Log.i(Utils.TAG, "Exiting \"" + this + "\"");
            this.currentState = null;
            return DefaultTransition.DEFAULT;
        }
    }

    private void transitionToNextStage() {
        final OpModeComponent<?> nextState = this.iterator.next();
        Log.i(Utils.TAG, "Transition from \"" + this.currentState + "\" => \"" + nextState + "\"");
        if (this.currentState != null) this.currentState.tearDown();
        this.currentState = nextState;
        nextState.setup();
    }
}
