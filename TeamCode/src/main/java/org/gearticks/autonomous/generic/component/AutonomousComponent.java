package org.gearticks.autonomous.generic.component;

/**
 * Autonomous component has input and output 'ports', with numbers starting at 1.
 * Interface is as simple as possible to allow for easy conversion.
 * 
 * AutonomousComponent can be used in regular switch-like state-machine, but also by an automated state-machine. 
 * See samples.
 *
 */
public interface AutonomousComponent {
	/**
	 * To be called once at start of autonomous program, e.g. to initialize some sensors
	 */
	public void initialize();
	
	
	/**
	 * Called each time the component state starts, can be multiple times in same autonomous program.
	 * The inputPort argument allows the setup to be dependent on the port through which iterator is triggered.
	 * For regular single-input-single-output components, this is not relevant.
	 * @param inputPort - The port through which this component/state is entered
	 */
	public void setup(int inputPort);

    /**
     * Convenience method.
     * Calls this.setup(1);
     */
    public void setup();
	
	/**
	 * To be called in each loop cycle.
	 * Returns the 'transition' or  id of the 'outputPort' through which this AutonomousComponent ends.
	 * This allows for choice between different transitions/routes to next AutonomousComponents.
	 * 
	 * @return 'outputPort', 0 means not ready
	 */
	public int run();
	
	/**
	 * Called each time the component state ends
	 */
	public void tearDown();
	
//	public Logger getLogger();

}
