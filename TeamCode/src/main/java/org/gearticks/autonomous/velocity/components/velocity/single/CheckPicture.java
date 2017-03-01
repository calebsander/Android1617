package org.gearticks.autonomous.velocity.components.velocity.single;

import android.util.Log;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl;
import org.gearticks.autonomous.velocity.components.velocity.single.SelectBeaconSide.PictureResult;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.opmodes.units.SideOfButton;
import org.gearticks.opmodes.utility.Utils;
import org.gearticks.vuforia.VuforiaConfiguration;
import org.gearticks.vuforia.VuforiaConfiguration.BeaconColorCounts;

public class CheckPicture extends AutonomousComponentAbstractImpl {
	public static final Transition CORRECT = new Transition("Correct"), WRONG = new Transition("Wrong"),
			LEFT_TRANSITION = new Transition("Left"), RIGHT_TRANSITION = new Transition("Right"), UNKNOWN_TRANSITION = new Transition("Unknown");

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

		final int dLeftRed = colorCounts.leftRed - oldColorCounts.leftRed;
		final int dRightRed = colorCounts.rightRed - oldColorCounts.rightRed;
		final int dLeftBlue = colorCounts.leftBlue - oldColorCounts.leftBlue;
		final int dRightBlue = colorCounts.rightBlue - oldColorCounts.rightBlue;

		final int change = (dLeftRed - dLeftBlue) + (dRightRed - dRightBlue);

		if (change > 4500){
			Log.i(Utils.TAG, "Significant change: " + change);
			if(isBlue){
				Log.i(Utils.TAG, "Turned red, incorrect");
				return WRONG;
			}
			else {
				Log.i(Utils.TAG, "Turned red, correct");
				return CORRECT;
			}
		}
		else if (change < -4500){
			Log.i(Utils.TAG, "Significant change: " + change);
			Log.i(Utils.TAG, "Turned blue");
			if(isBlue){
				Log.i(Utils.TAG, "Turned blue, correct");
				return CORRECT;
			}
			else {
				Log.i(Utils.TAG, "Turned blue, incorrect");
				return WRONG;
			}
		}
		else {
			Log.i(Utils.TAG, "Not significant change: " + change);
			final SideOfButton sideOfButton = getButtonToPress(this.vuforiaConfiguration.getBeaconBlueSide(colorCounts));
			switch (sideOfButton) {
				case LEFT:
					Log.i(Utils.TAG, "Going left");
					return LEFT_TRANSITION;
				case RIGHT:
					Log.i(Utils.TAG, "Going right");
					return RIGHT_TRANSITION;
				default:
					Log.i(Utils.TAG, "Button color could not be detected");
					return UNKNOWN_TRANSITION;
			}
		}
	}
	private SideOfButton getButtonToPress(SideOfButton sideOfBlue) {
		if (isBlue) {
			return sideOfBlue;
		}
		else {
			return sideOfBlue.getInverse();
		}
	}
}
