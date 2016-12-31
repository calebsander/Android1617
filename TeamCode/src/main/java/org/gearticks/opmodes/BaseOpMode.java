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
 * - Has a {@link JoystickOptionController} that is automatically updated<br>
 * - Keeps track of the time that has passed since the start of the match
 */
public abstract class BaseOpMode extends OpMode {
	protected static final int GAMEPAD_COUNT = 2;

	protected final ElapsedTime matchTime;
	private final JoystickOptionController optionController;
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

	protected void initialize() {}
	protected void loopBeforeStart() {}
	protected void matchStart() {}
	protected void loopAfterStart() {}
	protected void matchEnd() {}

	protected void addOption(JoystickOption option) {
		this.optionController.addOption(option);
	}
}