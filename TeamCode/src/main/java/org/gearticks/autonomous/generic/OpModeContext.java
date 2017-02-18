package org.gearticks.autonomous.generic;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.gearticks.GamepadWrapper;
import org.gearticks.hardware.configurations.HardwareConfiguration;
import org.gearticks.vuforia.VuforiaConfiguration;

public class OpModeContext<HARDWARE_TYPE extends HardwareConfiguration> {
	public final HARDWARE_TYPE configuration;
	private VuforiaConfiguration vuforiaConfiguration; //lazily computed
	public final GamepadWrapper[] gamepads;
	public final Telemetry telemetry;
	public final ElapsedTime matchTime;

	public OpModeContext(HARDWARE_TYPE configuration, GamepadWrapper[] gamepads, Telemetry telemetry, ElapsedTime matchTime) {
		this.configuration = configuration;
		this.vuforiaConfiguration = null;
		this.gamepads = gamepads;
		this.telemetry = telemetry;
		this.matchTime = matchTime;
	}

	public VuforiaConfiguration getVuforiaConfiguration() {
		if (this.vuforiaConfiguration == null) this.vuforiaConfiguration = new VuforiaConfiguration();
		return this.vuforiaConfiguration;
	}
}