package org.gearticks.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.gearticks.GamepadWrapper;
import org.gearticks.hardware.configurations.RenameThisHardwareConfiguration;
import org.gearticks.joystickoptions.JoystickOption;
import org.gearticks.joystickoptions.JoystickOptionController;

public abstract class BaseOpMode extends OpMode {
	protected final ElapsedTime matchTime;
	protected final JoystickOptionController optionController;
	protected RenameThisHardwareConfiguration configuration;
	protected GamepadWrapper[] gamepads;

	public BaseOpMode() {
		this.matchTime = new ElapsedTime();
		this.optionController = new JoystickOptionController();
	}

	public void init() {
		this.configuration = new RenameThisHardwareConfiguration(this.hardwareMap);
		this.initialize();
	}
	public void init_loop() {
		this.optionController.update(this.gamepad1, this.telemetry);
		this.loopBeforeStart();
	}
	public void start() {
		this.resetStartTime();
		this.matchTime.reset();
		this.gamepads = new GamepadWrapper[]{new GamepadWrapper(this.gamepad1), new GamepadWrapper(this.gamepad2)};
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