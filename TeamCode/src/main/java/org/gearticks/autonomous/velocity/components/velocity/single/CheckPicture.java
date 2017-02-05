package org.gearticks.autonomous.velocity.components.velocity.single;

import org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl;
import org.gearticks.autonomous.velocity.components.velocity.single.SelectBeaconSide.PictureResult;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.vuforia.VuforiaConfiguration;
import org.gearticks.vuforia.VuforiaConfiguration.BeaconColorCounts;

public class CheckPicture extends AutonomousComponentAbstractImpl {
	public static final int CORRECT = newTransition(), WRONG = newTransition();

	private final PictureResult pictureResult;
	private final VuforiaConfiguration vuforiaConfiguration;

	public CheckPicture(PictureResult pictureResult, VuforiaConfiguration vuforiaConfiguration) {
		this.pictureResult = pictureResult;
		this.vuforiaConfiguration = vuforiaConfiguration;
	}

	@Override
	public int run() {
		final int superTransition = super.run();
		if (superTransition != NOT_DONE) return superTransition;

		final BeaconColorCounts oldColorCounts = pictureResult.colorCounts;
		final int oldBlue = oldColorCounts.leftBlue + oldColorCounts.rightBlue;
		final BeaconColorCounts colorCounts = vuforiaConfiguration.getColorCounts();
		final int newBlue = colorCounts.leftBlue + colorCounts.rightBlue;
		final boolean nowBlue = newBlue > oldBlue;
		final boolean nowCorrect = nowBlue ^ AllianceOption.allianceOption.getRawSelectedOption() == AllianceOption.RED;
		if (nowCorrect) return CORRECT;
		else return WRONG;
	}
}
