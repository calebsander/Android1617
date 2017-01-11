package org.gearticks.autonomous.generic.opmode;

import android.util.Log;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.hardware.configurations.HardwareConfiguration;
import org.gearticks.opmodes.BaseOpMode;
import org.gearticks.opmodes.utility.Utils;

/**
 * An OpMode that instantiates a configuration
 * and executes a single component (possibly a state machine)
 */
public abstract class HardwareComponentAutonomous<HARDWARE_TYPE extends HardwareConfiguration> extends BaseOpMode {
	protected HARDWARE_TYPE configuration;
	private AutonomousComponent component;

	protected void initialize() {
		Log.d(Utils.TAG, "Start OpMode initialize");
		this.configuration = this.newConfiguration();
		this.component = this.getComponent();
	}
	protected void matchStart() {
		Log.d(Utils.TAG, "Starting OpMode matchStart");
		this.telemetry.clear();
		this.component.onMatchStart();
		this.component.setup();
	}
	protected void loopAfterStart() {
		this.component.run();
	}
	protected void matchEnd() {
		Log.d(Utils.TAG, "Starting OpMode matchEnd");
		this.component.tearDown();
		this.configuration.teardown();
	}

	/**
	 * Specifies the component to execute.
	 * Will be called in initialize() after configuration is created.
	 * @return the component to run
	 */
	protected abstract AutonomousComponent getComponent();
	/**
	 * Creates a configuration object of the specified type.
	 * The idea is to have a subclass of this class for each configuration type in use
	 * which will implement this method.
	 * Then every autonomous using that type of configuration can inherit from the subclass.
	 * @return an instance of HARDWARE_TYPE created from the hardware map
	 */
	protected abstract HARDWARE_TYPE newConfiguration();
}