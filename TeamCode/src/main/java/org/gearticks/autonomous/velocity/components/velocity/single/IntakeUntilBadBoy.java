package org.gearticks.autonomous.velocity.components.velocity.single;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;

public class IntakeUntilBadBoy extends AutonomousComponentHardware<VelocityConfiguration> {
	private final double timeOut;

	public IntakeUntilBadBoy(double timeOut, OpModeContext<VelocityConfiguration> opModeContext) {
		super(opModeContext);
		this.timeOut = timeOut;
	}

	@Override
	public void setup() {
		super.setup();
		this.configuration.intake.setPower(MotorConstants.INTAKE_IN);
	}

	@Override
	public Transition run() {
		final Transition superTransition = super.run();
		if (superTransition != null) return superTransition;

		if (this.configuration.ballInSnake() || this.stageTimer.seconds() > timeOut) return NEXT_STATE;
		else return null;
	}

	@Override
	public void tearDown() {
		super.tearDown();
		this.configuration.intake.stop();
	}
}