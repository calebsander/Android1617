package org.gearticks.components.hardwareagnostic.component;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent.DefaultTransition;
import org.gearticks.components.generic.component.OpModeComponentHardware;
import org.gearticks.hardware.configurations.HardwareConfiguration;
import org.gearticks.joystickoptions.IncrementOption;

public class Delay extends OpModeComponentHardware<HardwareConfiguration, DefaultTransition> {
	private final ElapsedTime matchTime;
	private final IncrementOption delayOption;

	public Delay(OpModeContext<? extends HardwareConfiguration> opModeContext, IncrementOption delayOption) {
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