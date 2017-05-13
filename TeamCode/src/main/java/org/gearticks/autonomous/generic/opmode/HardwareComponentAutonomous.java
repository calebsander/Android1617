package org.gearticks.autonomous.generic.opmode;

import android.util.Log;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.hardware.configurations.HardwareConfiguration;
import org.gearticks.opmodes.BaseOpMode;
import org.gearticks.Utils;

/**
 * An OpMode that instantiates a configuration
 * and executes a single component (possibly a state machine)
 */
public abstract class HardwareComponentAutonomous<HARDWARE_TYPE extends HardwareConfiguration> extends BaseOpMode {
	protected HARDWARE_TYPE configuration;
	private AutonomousComponent<?> component;
	private boolean done;

	protected void initialize() {
		Log.i(Utils.TAG, "Start OpMode initialize");
		this.configuration = this.newConfiguration();
		this.component = this.getComponent(new OpModeContext<>(this.configuration, this.gamepads, this.telemetry, this.matchTime));
	}
	protected void matchStart() {
		Log.i(Utils.TAG, "Starting OpMode matchStart");
		this.telemetry.clear();
		this.component.onMatchStart();
		this.component.setup();
		this.done = false;
	}
	protected void loopAfterStart() {
		if (!this.done) {
			final Enum<?> transition = this.component.run();
			if (transition != null) {
				Log.i(Utils.TAG, "Component finished");
				this.done = true;
				this.component.tearDown();
			}
		}
		this.telemetry.addData("Component", this.component);
	}
	protected void matchEnd() {
		Log.i(Utils.TAG, "Starting OpMode matchEnd");
		if (!this.done) this.component.tearDown();
		this.configuration.tearDown();
	}

	/**
	 * Specifies the component to execute.
	 * Will be called in initialize() after configuration is created.
	 * @return the component to run
	 */
	protected abstract AutonomousComponent<?> getComponent(OpModeContext<HARDWARE_TYPE> opModeContext);
	/**
	 * Creates a configuration object of the specified type.
	 * The idea is to have a subclass of this class for each configuration type in use
	 * which will implement this method.
	 * Then every autonomous using that type of configuration can inherit from the subclass.
	 * @return an instance of HARDWARE_TYPE created from the hardware map
	 */
	protected abstract HARDWARE_TYPE newConfiguration();
}