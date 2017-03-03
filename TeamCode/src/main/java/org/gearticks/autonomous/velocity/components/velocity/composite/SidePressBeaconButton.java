package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.components.velocity.single.BackPressBeacon;
import org.gearticks.autonomous.velocity.components.velocity.single.CheckPicture;
import org.gearticks.autonomous.velocity.components.velocity.single.CheckPicture.PictureDifference;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.FrontPressBeacon;
import org.gearticks.autonomous.velocity.components.velocity.single.SelectBeaconSide;
import org.gearticks.autonomous.velocity.components.velocity.single.SelectBeaconSide.PictureResult;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.opmodes.units.SideOfButton;

public class SidePressBeaconButton extends NetworkedStateMachine {
    public SidePressBeaconButton(boolean isBlue, OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(id);

        final PictureResult pictureResult = new PictureResult();
        final SelectBeaconSide selectSide = new SelectBeaconSide(isBlue, opModeContext, "Select beacon side", pictureResult);
        final FrontPressBeacon frontButton = new FrontPressBeacon(opModeContext, "Front press beacon");
        final BackPressBeacon backButton = new BackPressBeacon(opModeContext, "Back press beacon");
        final DisengageBeaconServo resetPresser = new DisengageBeaconServo(opModeContext, "Disengage beacon presser");
        final CheckPicture checkBeacon = new CheckPicture(isBlue, opModeContext, pictureResult);
        final Wait wait = new Wait(6.0, "Wait for beacon");
        final FrontPressBeacon frontButton2 = new FrontPressBeacon(opModeContext, "Front press beacon");
        final BackPressBeacon backButton2 = new BackPressBeacon(opModeContext, "Back press beacon");
        final DisengageBeaconServo resetPresser2 = new DisengageBeaconServo(opModeContext, "Disengage beacon presser");

        this.setInitialComponent(selectSide);
        this.addConnection(selectSide, SideOfButton.LEFT, backButton);
        this.addConnection(selectSide, SideOfButton.RIGHT, frontButton);
        this.addConnection(frontButton, DefaultTransition.DEFAULT, resetPresser);
        this.addConnection(backButton, DefaultTransition.DEFAULT, resetPresser);
        this.addConnection(resetPresser, DefaultTransition.DEFAULT, checkBeacon);

        this.addConnection(checkBeacon, PictureDifference.LEFT, backButton2);
        this.addConnection(checkBeacon, PictureDifference.RIGHT, frontButton2);
        this.addConnection(checkBeacon, PictureDifference.CORRECT, resetPresser2);
        this.addConnection(checkBeacon, PictureDifference.WRONG, wait);
        this.addConnection(wait, DefaultTransition.DEFAULT, frontButton2);
        this.addConnection(frontButton2, DefaultTransition.DEFAULT, resetPresser2);
        this.addConnection(backButton2, DefaultTransition.DEFAULT, resetPresser2);
        this.addExitConnection(resetPresser2, DefaultTransition.DEFAULT);
    }
}
