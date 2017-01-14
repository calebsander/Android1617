package org.gearticks.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.gearticks.GamepadWrapper;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.joystickoptions.JoystickOption;
import org.gearticks.joystickoptions.JoystickOptionController;

/**
 * Adds some handy functionality on top of {@link OpMode}:<br>
 * - Adds default empty implementations of init() and loop() so they can be omitted<br>
 * - Wraps the gamepads and updates the last values each loop cycle<br>
 * - Has a {@link JoystickOptionController} that is automatically updated
 * and contains the alliance selector at the beginning<br>
 * - Keeps track of the time that has passed since the start of the match
 */
public abstract class BaseOpMode extends OpMode {
	protected static final int GAMEPAD_COUNT = 2;

	/**
	 * The amount of time that has passed in the match.
	 * Values should not be used before the match starts.
	 */
	protected final ElapsedTime matchTime;
	private final JoystickOptionController optionController;
	/**
	 * The two gamepads, wrapped.
	 * Index 0 is Calvin's, index 1 is Jack's.
	 */
	protected final GamepadWrapper[] gamepads;

	public BaseOpMode() {
		this.matchTime = new ElapsedTime();
		this.optionController = new JoystickOptionController();
		this.gamepads = new GamepadWrapper[GAMEPAD_COUNT];
	}

	public final void init() {
		this.addOption(AllianceOption.allianceOption);
		this.initialize();
	}
	public final void init_loop() {
		this.optionController.update(this.gamepad1, this.telemetry);
		this.loopBeforeStart();
	}
	public final void start() {
		this.resetStartTime();
		this.matchTime.reset();
		this.gamepads[0] = new GamepadWrapper(this.gamepad1);
		this.gamepads[1] = new GamepadWrapper(this.gamepad2);
		this.matchStart();
	}
	public final void loop() {
		this.loopAfterStart();
		for (final GamepadWrapper gamepad : this.gamepads) gamepad.updateLast();
	}
	public final void stop() {
		this.matchEnd();
	}

	/**
	 * What to do when OpMode is initialized.
	 * Can be left empty.
	 */
	protected void initialize() {}
	/**
	 * What to do repeatedly before match starts.
	 * Can be left empty.
	 */
	protected void loopBeforeStart() {}
	/**
	 * What to do when match starts.
	 * Can be left empty.
	 */
	protected void matchStart() {}
	/**
	 * What to do repeatedly after match starts,
	 * but before match ends.
	 * Can be left empty.
	 */
	protected void loopAfterStart() {}
	/**
	 * What to do when match ends.
	 * Can be left empty.
	 */
	protected void matchEnd() {}

	/**
	 * Add an option to end of the option controller
	 * @param option the option to add
	 */
	protected void addOption(JoystickOption option) {
		this.optionController.addOption(option);
	}
}