package org.gearticks.autonomous.velocity.components.generic;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent.DefaultTransition;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.HardwareConfiguration;
import org.gearticks.joystickoptions.IncrementOption;

public class Delay extends AutonomousComponentHardware<HardwareConfiguration, DefaultTransition> {
	private final ElapsedTime matchTime;
	private final IncrementOption delayOption;

	@SuppressWarnings("unchecked")
	public Delay(OpModeContext opModeContext, IncrementOption delayOption) {
		super(opModeContext, DefaultTransition.class);
		this.matchTime = opModeContext.matchTime;
		this.delayOption = delayOption;
	}

	@Override
	public DefaultTransition run() {
		final DefaultTransition superTransition = super.run();
		if (superTransition != null) return superTransition;

		this.configuration.stopMotion();
		if (this.matchTime.seconds() > this.delayOption.getValue()) return DefaultTransition.DEFAULT;
		else return null;
	}
}