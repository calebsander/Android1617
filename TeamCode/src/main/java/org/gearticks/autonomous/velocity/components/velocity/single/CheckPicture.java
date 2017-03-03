package org.gearticks.autonomous.velocity.components.velocity.single;

import android.util.Log;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl;
import org.gearticks.autonomous.velocity.components.velocity.single.CheckPicture.PictureDifference;
import org.gearticks.autonomous.velocity.components.velocity.single.SelectBeaconSide.PictureResult;
import org.gearticks.opmodes.units.SideOfButton;
import org.gearticks.opmodes.utility.Utils;
import org.gearticks.vuforia.VuforiaConfiguration;
import org.gearticks.vuforia.VuforiaConfiguration.BeaconColorCounts;

public class CheckPicture extends AutonomousComponentAbstractImpl<PictureDifference> {
	public enum PictureDifference {
		CORRECT,
		WRONG,
		LEFT,
		RIGHT,
		UNKNOWN
	}

	private final PictureResult pictureResult;
	private final VuforiaConfiguration vuforiaConfiguration;
	private final boolean isBlue;

	public CheckPicture(boolean isBlue, OpModeContext<?> opModeContext, PictureResult pictureResult) {
		super(PictureDifference.class);
		this.isBlue = isBlue;
		this.pictureResult = pictureResult;
		this.vuforiaConfiguration = opModeContext.getVuforiaConfiguration();
	}

	@Override
	public PictureDifference run() {
		final PictureDifference superTransition = super.run();
		if (superTransition != null) return superTransition;

		final BeaconColorCounts oldColorCounts = pictureResult.colorCounts;
		final BeaconColorCounts colorCounts = this.vuforiaConfiguration.getColorCounts();

		final int dLeftRed = colorCounts.leftRed - oldColorCounts.leftRed;
		final int dRightRed = colorCounts.rightRed - oldColorCounts.rightRed;
		final int dLeftBlue = colorCounts.leftBlue - oldColorCounts.leftBlue;
		final int dRightBlue = colorCounts.rightBlue - oldColorCounts.rightBlue;

		final int change = (dLeftRed - dLeftBlue) + (dRightRed - dRightBlue);

		if (change > 4500){
			Log.i(Utils.TAG, "Significant change: " + change);
			if(isBlue){
				Log.i(Utils.TAG, "Turned red, incorrect");
				return PictureDifference.WRONG;
			}
			else {
				Log.i(Utils.TAG, "Turned red, correct");
				return PictureDifference.CORRECT;
			}
		}
		else if (change < -4500){
			Log.i(Utils.TAG, "Significant change: " + change);
			Log.i(Utils.TAG, "Turned blue");
			if(isBlue){
				Log.i(Utils.TAG, "Turned blue, correct");
				return PictureDifference.CORRECT;
			}
			else {
				Log.i(Utils.TAG, "Turned blue, incorrect");
				return PictureDifference.WRONG;
			}
		}
		else {
			Log.i(Utils.TAG, "Not significant change: " + change);
			final SideOfButton sideOfButton = getButtonToPress(this.vuforiaConfiguration.getBeaconBlueSide(colorCounts));
			switch (sideOfButton) {
				case LEFT:
					Log.i(Utils.TAG, "Going left");
					return PictureDifference.LEFT;
				case RIGHT:
					Log.i(Utils.TAG, "Going right");
					return PictureDifference.RIGHT;
				default:
					Log.i(Utils.TAG, "Button color could not be detected");
					return PictureDifference.UNKNOWN;
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
