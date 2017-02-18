package org.gearticks.autonomous.velocity.components.experimental;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.HardwareConfiguration;
import org.gearticks.joystickoptions.IncrementOption;

public class Delay extends AutonomousComponentHardware<HardwareConfiguration> {
	private final IncrementOption delayOption;

	public Delay(OpModeContext<HardwareConfiguration> opModeContext, IncrementOption delayOption) {
		super(opModeContext);
		this.delayOption = delayOption;
	}

	@Override
	public Transition run() {
		final Transition superTransition = super.run();
		if (superTransition != null) return superTransition;

		this.configuration.stopMotion();
		if (this.stageTimer.seconds() > this.delayOption.getValue()) return NEXT_STATE;
		else return null;
	}
}