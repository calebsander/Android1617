package org.gearticks.autonomous.generic.statemachine;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.gearticks.autonomous.generic.component.AutonomousComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implements
 */
public abstract class StateMachineAbstractImpl implements AutonomousComponent {

	private final String id;
    @Nullable
    protected AutonomousComponent currentState = null;
    @NonNull
    protected List<AutonomousComponent> components = new ArrayList<>();

	public StateMachineAbstractImpl() {
		super();
		this.id = this.getClass().getSimpleName();
	}

	public StateMachineAbstractImpl(String id) {
		super();
		this.id = id;
	}

    public void addComponent(AutonomousComponent ac){
        this.components.add(ac);
    }
    public void addComponents(@NonNull Collection<AutonomousComponent> components){
        this.components.addAll(components);
    }

    @Override
    public void initializeAtMatchStart() {
        for (AutonomousComponent ac : this.components){
            ac.initializeAtMatchStart();
        }
    }

	/**
	 * Default is empty so subclass doesn't have to implement if not necessary
	 */
	@Override
	public void setup(int inputPort) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Convenience method.
	 * Calls this.setup(1);
	 */
	public void setup() {
		this.setup(1);
	}

	/**
	 * Default is empty so subclass doesn't have to implement if not necessary.
	 * Returns 0;
	 */
	@Override
	public int run() {
		return 0;
	}

	/**
	 * Default is empty so subclass doesn't have to implement if not necessary
	 */
    @Override
    public void tearDown() {
        this.currentState = null;
    }
	
	@Override
	public String toString(){
		return this.id;
	}

    public Logger getLogger(){
        return Logger.getLogger(this.getClass().getSimpleName());
    }

    public String getId() {
        return id;
    }
}
