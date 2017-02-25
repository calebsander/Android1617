package org.gearticks.autonomous.velocity.components.velocity.single;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl;
import org.gearticks.autonomous.velocity.components.velocity.single.SelectBeaconSide.PictureResult;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.vuforia.VuforiaConfiguration;
import org.gearticks.vuforia.VuforiaConfiguration.BeaconColorCounts;

public class CheckPicture extends AutonomousComponentAbstractImpl {
	public static final Transition CORRECT = new Transition("Correct"), WRONG = new Transition("Wrong");

	private final PictureResult pictureResult;
	private final VuforiaConfiguration vuforiaConfiguration;
	private final boolean isBlue;

	public CheckPicture(boolean isBlue, OpModeContext opModeContext, PictureResult pictureResult) {
		this.isBlue = isBlue;
		this.pictureResult = pictureResult;
		this.vuforiaConfiguration = opModeContext.getVuforiaConfiguration();
	}

	@Override
	public Transition run() {
		final Transition superTransition = super.run();
		if (superTransition != null) return superTransition;

		final BeaconColorCounts oldColorCounts = pictureResult.colorCounts;
		final int oldBlue = oldColorCounts.leftBlue + oldColorCounts.rightBlue;
		final BeaconColorCounts colorCounts = this.vuforiaConfiguration.getColorCounts();
		final int newBlue = colorCounts.leftBlue + colorCounts.rightBlue;
		final boolean nowBlue = newBlue > oldBlue;
		final boolean nowCorrect = nowBlue ^ (!isBlue);
		if (nowCorrect) return CORRECT;
		else return WRONG;
	}
}
