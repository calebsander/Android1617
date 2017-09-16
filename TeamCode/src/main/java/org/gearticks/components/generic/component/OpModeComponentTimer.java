package org.gearticks.components.generic.component;

import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Adds a timer on top of AutonomousComponentAbstractImpl
 */
public class OpModeComponentTimer<TRANSITION_TYPE extends Enum<?>> extends OpModeComponentAbstract<TRANSITION_TYPE> {
	protected final ElapsedTime stageTimer;

	public OpModeComponentTimer(Class<TRANSITION_TYPE> transitionClass) {
		super(transitionClass);
		this.stageTimer = new ElapsedTime();
	}

	public OpModeComponentTimer(Class<TRANSITION_TYPE> transitionClass, String id) {
		super(transitionClass, id);
		this.stageTimer = new ElapsedTime();
	}

	@Override
	public void onMatchStart() {
		super.onMatchStart();
		this.stageTimer.reset(); //makes sense if somehow stageTimer is used without a call to setup()
	}

	@Override
	public void setup() {
		super.setup();
		this.stageTimer.reset();
	}
}
