package org.gearticks.autonomous.velocity.components.velocity.single;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent.DefaultTransition;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;

public class IntakeUntilBadBoy extends AutonomousComponentHardware<VelocityConfiguration, DefaultTransition> {
	private final double timeOut;

	public IntakeUntilBadBoy(double timeOut, OpModeContext<VelocityConfiguration> opModeContext) {
		super(opModeContext, DefaultTransition.class);
		this.timeOut = timeOut;
	}

	@Override
	public void setup() {
		super.setup();
		this.configuration.intake.setPower(MotorConstants.INTAKE_IN);
	}

	@Override
	public DefaultTransition run() {
		final DefaultTransition superTransition = super.run();
		if (superTransition != null) return superTransition;

		if (this.configuration.ballInSnake() || this.stageTimer.seconds() > timeOut) return DefaultTransition.DEFAULT;
		else return null;
	}

	@Override
	public void tearDown() {
		super.tearDown();
		this.configuration.intake.stop();
	}
}