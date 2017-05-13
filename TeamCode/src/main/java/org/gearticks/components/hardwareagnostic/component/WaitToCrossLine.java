package org.gearticks.components.hardwareagnostic.component;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent.DefaultTransition;
import org.gearticks.components.generic.component.OpModeComponentAbstract;

public class WaitToCrossLine extends OpModeComponentAbstract<DefaultTransition> {
	private final ElapsedTime matchTime;

	public WaitToCrossLine(OpModeContext<?> opModeContext) {
		super(DefaultTransition.class, "Wait until 10 seconds in");
		this.matchTime = opModeContext.matchTime;
	}

	@Override
	public DefaultTransition run() {
		final DefaultTransition superTransition = super.run();
		if (superTransition != null) return superTransition;

		if (this.matchTime.seconds() > 10.0) return DefaultTransition.DEFAULT;
		else return null;
	}
}