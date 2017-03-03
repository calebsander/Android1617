package org.gearticks.autonomous.velocity.components.velocity.single;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.vuforia.VuforiaConfiguration;
import org.gearticks.vuforia.VuforiaConfiguration.BeaconColorCounts;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.opmodes.units.SideOfButton;

public class SelectBeaconSide extends AutonomousComponentHardware<VelocityConfiguration, SideOfButton> {
	private final VuforiaConfiguration vuforiaConfiguration;
	private boolean allianceColorIsBlue;
	private final PictureResult pictureResult;

	/**
	 * @param isBlue - whether component is running on the blue alliance
	 * @param opModeContext - the OpModeContext this is running in
	 * @param id - descriptive name for logging
	 * @param pictureResult - the object to store the beacon color counts in
	 */
	public SelectBeaconSide(boolean isBlue, OpModeContext<VelocityConfiguration> opModeContext, String id, PictureResult pictureResult) {
		super(opModeContext, SideOfButton.class, id);
		this.vuforiaConfiguration = opModeContext.getVuforiaConfiguration();
		this.pictureResult = pictureResult;
		this.allianceColorIsBlue = isBlue;
	}
	public SelectBeaconSide(boolean isBlue, String id, OpModeContext<VelocityConfiguration> opModeContext) {
		this(isBlue, opModeContext, id, new PictureResult());
	}

	@Override
	public SideOfButton run() {
		final SideOfButton superTransition = super.run();
		if (superTransition != null) return superTransition;

		final BeaconColorCounts colorCounts = this.vuforiaConfiguration.getColorCounts();
		this.pictureResult.colorCounts = colorCounts;
		return getButtonToPress(this.vuforiaConfiguration.getBeaconBlueSide(colorCounts));
	}


	private SideOfButton getButtonToPress(SideOfButton sideOfBlue) {
		if (this.allianceColorIsBlue) {
			return sideOfBlue;
		}
		else {
			return sideOfBlue.getInverse();
		}
	}

	public static class PictureResult {
		BeaconColorCounts colorCounts;
	}

}
