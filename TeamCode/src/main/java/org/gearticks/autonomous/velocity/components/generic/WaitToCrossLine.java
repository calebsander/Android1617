package org.gearticks.autonomous.velocity.components.generic;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl;

public class WaitToCrossLine extends AutonomousComponentAbstractImpl {
	private final ElapsedTime matchTime;

	public WaitToCrossLine(OpModeContext opModeContext) {
		super("Wait until 10 seconds in");
		this.matchTime = opModeContext.matchTime;
	}

	@Override
	public Transition run() {
		final Transition superTransition = super.run();
		if (superTransition != null) return superTransition;

		if (this.matchTime.seconds() > 10.0) return NEXT_STATE;
		else return null;
	}
}