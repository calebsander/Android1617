package org.gearticks.components.velocity.component.composite;

import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.velocity.component.drive.GiroDriveEncoderBeacon;
import org.gearticks.components.velocity.component.drive.GiroTurnBeacon;
import org.gearticks.components.velocity.component.vuforia.SelectBeaconSide;
import org.gearticks.components.generic.statemachine.NetworkedStateMachine;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.opmodes.units.SideOfButton;

/**
 * Sample to create a composite autonomous component that will consist of a linear state-machine of 2 or more AutonomousComponents
 */
public class NonLinearPressBeaconButton extends NetworkedStateMachine {

    public NonLinearPressBeaconButton(OpModeContext<VelocityConfiguration> opModeContext, String id) {
        final SelectBeaconSide selectSide = new SelectBeaconSide(true, "Press beacon button", opModeContext);
        final GiroTurnBeacon pushLeftButton = new GiroTurnBeacon(10.0, opModeContext, "Turn left");
        final GiroTurnBeacon pushRightButton = new GiroTurnBeacon(-10.0, opModeContext, "Turn right");
        final GiroDriveEncoderBeacon driveInLeftButton = new GiroDriveEncoderBeacon(10.0, 0.3, 200, opModeContext, "Press left button");
        final GiroDriveEncoderBeacon driveInRightButton = new GiroDriveEncoderBeacon(-10.0, 0.3, 200, opModeContext, "Press right button");
        final GiroTurnBeacon squareUp = new GiroTurnBeacon(0.0, opModeContext, "Square up");

        this.setInitialComponent(selectSide);
        this.addConnection(selectSide, SideOfButton.LEFT, pushLeftButton);
        this.addConnection(selectSide, SideOfButton.RIGHT, pushRightButton);
        this.addExitConnection(selectSide, SideOfButton.UNKNOWN);
        this.addConnection(pushLeftButton, DefaultTransition.DEFAULT, driveInLeftButton);
        this.addConnection(pushRightButton, DefaultTransition.DEFAULT, driveInRightButton);
        this.addConnection(driveInLeftButton, DefaultTransition.DEFAULT, squareUp);
        this.addConnection(driveInRightButton, DefaultTransition.DEFAULT, squareUp);
        this.addExitConnection(squareUp, DefaultTransition.DEFAULT);
    }
}
