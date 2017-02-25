package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.velocity.single.BackPressBeacon;
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
        final AutonomousComponent resetButton = new DisengageBeaconServo(opModeContext, "Disengage beacon presser");
        final AutonomousComponent fixBeacon = new BeaconCheck(isBlue, pictureResult, opModeContext);
        this.setInitialComponent(selectSide);
        this.addConnection(selectSide, SelectBeaconSide.LEFT_TRANSITION, backButton);
        this.addConnection(selectSide, SelectBeaconSide.RIGHT_TRANSITION, frontButton);
        this.addConnection(frontButton, NEXT_STATE, resetButton);
        this.addConnection(backButton, NEXT_STATE, resetButton);
        this.addConnection(resetButton, NEXT_STATE, fixBeacon);
        this.addExitConnection(fixBeacon, NEXT_STATE);
    }
}
