package org.gearticks.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.gearticks.GamepadWrapper;
import org.gearticks.joystickoptions.JoystickOption;
import org.gearticks.joystickoptions.JoystickOptionController;

public abstract class BaseOpMode extends OpMode {
	protected static final int GAMEPAD_COUNT = 2;

	protected final ElapsedTime matchTime;
	private JoystickOptionController optionController;
	protected final GamepadWrapper[] gamepads;

	public BaseOpMode() {
		this.matchTime = new ElapsedTime();
		this.gamepads = new GamepadWrapper[GAMEPAD_COUNT];
	}

	public void init() {
		this.optionController = new JoystickOptionController();
		this.gamepads[0] = new GamepadWrapper(this.gamepad1);
		this.gamepads[1] = new GamepadWrapper(this.gamepad2);
		this.initialize();
	}
	public void init_loop() {
		this.optionController.update(this.gamepads[0], this.telemetry);
		this.loopBeforeStart();
		this.updateGamepadLasts();
	}
	public void start() {
		this.resetStartTime();
		this.matchTime.reset();
		this.matchStart();
	}
	public void loop() {
		this.loopAfterStart();
		this.updateGamepadLasts();
	}
	public void stop() {
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
	private void updateGamepadLasts() {
		for (final GamepadWrapper gamepad : this.gamepads) gamepad.updateLast();
	}
}