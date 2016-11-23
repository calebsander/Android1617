package org.gearticks.autonomous.component;

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
	public void initialize() {
		// Default doesn't do anything
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
		// TODO Auto-generated method stub

	}
	
	@Override
	public String toString(){
		return this.id;
	}
	
	

}
