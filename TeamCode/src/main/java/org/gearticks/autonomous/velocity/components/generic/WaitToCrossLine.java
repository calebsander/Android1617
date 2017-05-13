package org.gearticks.autonomous.velocity.components.generic;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent.DefaultTransition;
import org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl;

public class WaitToCrossLine extends AutonomousComponentAbstractImpl<DefaultTransition> {
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