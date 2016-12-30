package org.gearticks.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.gearticks.GamepadWrapper;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.joystickoptions.JoystickOption;
import org.gearticks.joystickoptions.JoystickOptionController;

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

	public void init() {
		this.addOption(AllianceOption.allianceOption);
		this.initialize();
	}
	public void init_loop() {
		this.optionController.update(this.gamepad1, this.telemetry);
		this.loopBeforeStart();
	}
	public void start() {
		this.resetStartTime();
		this.matchTime.reset();
		this.gamepads[0] = new GamepadWrapper(this.gamepad1);
		this.gamepads[1] = new GamepadWrapper(this.gamepad2);
		this.matchStart();
	}
	public void loop() {
		this.loopAfterStart();
		for (final GamepadWrapper gamepad : this.gamepads) gamepad.updateLast();
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
}