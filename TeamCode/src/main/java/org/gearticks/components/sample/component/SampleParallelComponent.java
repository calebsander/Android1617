package org.gearticks.components.sample.component;

import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponentTimer;
import org.gearticks.components.generic.component.ParallelComponent;
import org.gearticks.components.hardwareagnostic.component.GiroDriveEncoder;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;

public class SampleParallelComponent extends ParallelComponent {
	public SampleParallelComponent(OpModeContext<VelocityConfiguration> opModeContext) {
		super("Drive and intake");
		this.addComponent(new GiroDriveEncoder(0.0, 0.5, 5000, opModeContext, "Drive forward"));
		this.addComponent(new OpModeComponentTimer<DefaultTransition>(DefaultTransition.class, "Intaking") {
			@Override
			public void setup() {
				super.setup();
				opModeContext.configuration.intake.setPower(MotorConstants.INTAKE_IN);
			}
			@Override
			public DefaultTransition run() {
				final DefaultTransition superTransition = super.run();
				if (superTransition != null) return superTransition;

				if (this.stageTimer.seconds() > 5.0) return DefaultTransition.DEFAULT; //if time is 1.0, intaking ends first
				else return null;
			}
			@Override
			public void tearDown() {
				super.tearDown();
				opModeContext.configuration.intake.stop();
			}
		});
	}
}