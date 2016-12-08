package org.gearticks.opmodes.components;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.gearticks.joystickoptions.IncrementOption;

public class DelayComponent {
	private final IncrementOption delayOption;
	private final ElapsedTime stageTimer;

	public DelayComponent(IncrementOption delayOption) {
		this.delayOption = delayOption;
		this.stageTimer = new ElapsedTime();
	}

	public boolean isDone() {
		return this.stageTimer.seconds() > this.delayOption.getValue();
	}
}