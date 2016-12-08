package org.gearticks.opmodes.components;

import org.gearticks.hardware.configurations.HardwareConfiguration;

public class StoppedComponent {
	private final HardwareConfiguration configuration;

	public StoppedComponent(HardwareConfiguration configuration) {
		this.configuration = configuration;
	}

	public void loop() {
		this.configuration.stopMotion();
	}
	public boolean isDone() {
		return false;
	}
}