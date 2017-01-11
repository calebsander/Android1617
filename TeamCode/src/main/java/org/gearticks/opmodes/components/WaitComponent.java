package org.gearticks.opmodes.components;

import com.qualcomm.robotcore.util.ElapsedTime;

public class WaitComponent {
	private final double waitSeconds;
	private final ElapsedTime stageTimer;

	public WaitComponent(double waitSeconds) {
		this.waitSeconds = waitSeconds;
		this.stageTimer = new ElapsedTime();
	}

	public boolean isDone() {
		return this.stageTimer.seconds() > this.waitSeconds;
	}
}