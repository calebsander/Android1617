package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.components.velocity.single.BackPressBeacon;
import org.gearticks.autonomous.velocity.components.velocity.single.CheckPicture;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.FrontPressBeacon;
import org.gearticks.autonomous.velocity.components.velocity.single.SelectBeaconSide;
import org.gearticks.autonomous.velocity.components.velocity.single.SelectBeaconSide.PictureResult;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class SidePressBeaconButton extends NetworkedStateMachine {
    public SidePressBeaconButton(boolean isBlue, OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(id);

        final PictureResult pictureResult = new PictureResult();
        final AutonomousComponent selectSide = new SelectBeaconSide(isBlue, opModeContext, "Select beacon side", pictureResult);
        final AutonomousComponent frontButton = new FrontPressBeacon(opModeContext, "Front press beacon");
        final AutonomousComponent backButton = new BackPressBeacon(opModeContext, "Back press beacon");
        final AutonomousComponent resetPresser = new DisengageBeaconServo(opModeContext, "Disengage beacon presser");
        final AutonomousComponent checkBeacon = new CheckPicture(isBlue, opModeContext, pictureResult);
	    final AutonomousComponent wait = new Wait(6.0, "Wait for beacon");
	    final AutonomousComponent frontButton2 = new FrontPressBeacon(opModeContext, "Front press beacon");
	    final AutonomousComponent backButton2 = new BackPressBeacon(opModeContext, "Back press beacon");
	    final AutonomousComponent resetPresser2 = new DisengageBeaconServo(opModeContext, "Disengage beacon presser");

        this.setInitialComponent(selectSide);
        this.addConnection(selectSide, SelectBeaconSide.LEFT_TRANSITION, backButton);
        this.addConnection(selectSide, SelectBeaconSide.RIGHT_TRANSITION, frontButton);
        this.addConnection(frontButton, NEXT_STATE, resetPresser);
        this.addConnection(backButton, NEXT_STATE, resetPresser);
        this.addConnection(resetPresser, NEXT_STATE, checkBeacon);

        this.addConnection(checkBeacon, CheckPicture.LEFT_TRANSITION, backButton2);
        this.addConnection(checkBeacon, CheckPicture.RIGHT_TRANSITION, frontButton2);
	    this.addConnection(checkBeacon, CheckPicture.CORRECT, resetPresser2);
	    this.addConnection(checkBeacon, CheckPicture.WRONG, wait);
	    this.addConnection(wait, NEXT_STATE, frontButton2);
	    this.addConnection(frontButton2, NEXT_STATE, resetPresser2);
	    this.addConnection(backButton2, NEXT_STATE, resetPresser2);
        this.addExitConnection(resetPresser2, NEXT_STATE);
    }
}
