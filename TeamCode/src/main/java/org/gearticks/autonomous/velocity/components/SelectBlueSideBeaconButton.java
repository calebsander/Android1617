package org.gearticks.autonomous.velocity.components;

import android.support.annotation.NonNull;
import android.util.Log;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.gearticks.GamepadWrapper;
import org.gearticks.Vuforia.VuforiaConfiguration;
import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.opmodes.units.SideOfButton;
import org.gearticks.opmodes.utility.Utils;

public class SelectBlueSideBeaconButton extends AutonomousComponentVelocityBase {
	private final DriveDirection direction = new DriveDirection();
	private final VuforiaConfiguration vuforiaConfiguration;
	private boolean allianceColorIsBlue;

	/**
	 *
	 * @param configuration
	 * @param id - descriptive name for logging
	 */
	public SelectBlueSideBeaconButton(@NonNull VuforiaConfiguration vuforiaConfiguration, @NonNull VelocityConfiguration configuration, String id) {
		super(configuration, id);
		this.vuforiaConfiguration = Utils.assertNotNull(vuforiaConfiguration);

	}

	@Override
	public void initializeAtMatchStart(){
		this.allianceColorIsBlue = AllianceOption.allianceOption.getRawSelectedOption() == AllianceOption.BLUE;
	}

	@Override
	public void setup(int inputPort) {
		super.setup(inputPort);
	}

	@Override
	public int run() {
		int transition = 0;
		super.run();
		SideOfButton sideOfButton = getButtonToPress(this.vuforiaConfiguration.getBeaconBlueSide());
		switch (sideOfButton){
			case LEFT:
				Log.i(Utils.TAG, "Going left");
				transition = 1;
				break;
			case RIGHT:
				Log.i(Utils.TAG, "Going right");
				transition = 2;
				break;
			case UNKNOWN:
				Log.i(Utils.TAG, "Button color could not be detected");
				transition = 3;
				break;
		}

		return transition;
	}

	@Override
	public void tearDown() {
		super.tearDown();
	}


	private SideOfButton getButtonToPress(SideOfButton sideOfBlue){
		if (this.allianceColorIsBlue){
			return sideOfBlue;
		}
		else {
			return sideOfBlue.getInverse();
		}
	}

}
