package org.gearticks.autonomous.velocity.components.velocity.single;

import android.util.Log;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentTimer;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.opmodes.utility.Utils;
import org.gearticks.vuforia.VuforiaConfiguration;

public class VerifyVuforiaPosition extends AutonomousComponentTimer {
	public static final Transition
		CORRECT = new Transition("Close"),
		INCORRECT = new Transition("Far");

	private final VuforiaConfiguration vuforiaConfiguration;
	private final String target;
	private final double lateralTarget;
	private final double distanceTarget;
	private final double allowedRange;

	public VerifyVuforiaPosition(String target, double lateralTarget, double distanceTarget, double allowedRange, OpModeContext<VelocityConfiguration> opModeContext) {
		this.vuforiaConfiguration = opModeContext.getVuforiaConfiguration();
		this.target = target;
		this.lateralTarget = lateralTarget;
		this.distanceTarget = distanceTarget;
		this.allowedRange = allowedRange;
	}

	@Override
	public void setup() {
		super.setup();
		this.vuforiaConfiguration.activate();
	}
	@Override
	public Transition run() {
		final Transition superTransition = super.run();
		if (superTransition != null) return superTransition;

		if (this.stageTimer.seconds() > 1.0) return INCORRECT; //timeout
		final VuforiaTrackableDefaultListener listener = this.vuforiaConfiguration.getTargetListener(this.target);
		final OpenGLMatrix pose = listener.getPose();
		if (pose == null) return null; //wait until we see the images
		final VectorF translation = pose.getTranslation();
		Log.d(Utils.TAG, "Translation: " + translation);
		final boolean correctLateral = Math.abs(translation.get(1) - this.lateralTarget) < this.allowedRange;
		final boolean correctDistance = Math.abs(translation.get(2) - this.distanceTarget) < this.allowedRange;
		if (correctLateral && correctDistance) return CORRECT;
		else return INCORRECT;
	}
}