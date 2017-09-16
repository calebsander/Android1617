package org.gearticks.components.hardwareagnostic.component;

import com.qualcomm.robotcore.hardware.Servo;
import org.gearticks.components.generic.component.OpModeComponent.DefaultTransition;
import org.gearticks.components.generic.component.OpModeComponentAbstract;

public class SetServoPosition extends OpModeComponentAbstract<DefaultTransition> {
	private final Servo servo;
	private final double position;

	public SetServoPosition(Servo servo, double position) {
		super(DefaultTransition.class);
		this.servo = servo;
		this.position = position;
	}
	public SetServoPosition(Servo servo, double position, String id) {
		super(DefaultTransition.class, id);
		this.servo = servo;
		this.position = position;
	}

	@Override
	public void setup() {
		super.setup();
		this.servo.setPosition(this.position);
	}
	@Override
	public DefaultTransition run() {
		final DefaultTransition superTransition = super.run();
		if (superTransition != null) return superTransition;

		return DefaultTransition.DEFAULT;
	}
}