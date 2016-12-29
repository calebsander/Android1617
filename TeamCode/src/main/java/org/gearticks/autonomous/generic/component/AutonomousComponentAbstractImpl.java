package org.gearticks.autonomous.generic.component;

import android.util.Log;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.opmodes.utility.Utils;

/**
 * Implements the AutonomousComponent interface.
 * Adds
 * - setup() without a input port number as a convenience method 
 * - toString()
 *
 */
public abstract class AutonomousComponentAbstractImpl implements
        AutonomousComponent {

	private final String id;
	
	public AutonomousComponentAbstractImpl() {
		super();
		this.id = this.getClass().getSimpleName();
	}

	public AutonomousComponentAbstractImpl(String id) {
		super();
		this.id = id;
	}

	/**
	 * Default is empty so subclass doesn't have to implement if not necessary
	 */
	@Override
	public void initializeAtMatchStart() {
		// Default doesn't do anything
	}

	/**
	 * Default is empty so subclass doesn't have to implement if not necessary
	 */
	@Override
	public void setup(int inputPort) {
		Log.d(Utils.TAG, "Setup of " + this.getId());
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
		Log.d(Utils.TAG, "TearDown of " + this.getId());
	}
	
	@Override
	public String toString(){
		return this.id;
	}


	public String getId() {
		return id;
	}
}
