package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.velocity.components.deprecated.GiroDriveEncoderBeacon;
import org.gearticks.autonomous.velocity.components.deprecated.GiroTurnBeacon;
import org.gearticks.autonomous.velocity.components.velocity.single.SelectBeaconSide;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * Sample to create a composite autonomous component that will consist of a linear state-machine of 2 or more AutonomousComponents
 */
public class NonLinearPressBeaconButton extends NetworkedStateMachine {

    public NonLinearPressBeaconButton(OpModeContext<VelocityConfiguration> opModeContext, String id) {
        final AutonomousComponent selectSide = new SelectBeaconSide(opModeContext, "Press beacon button");
        final AutonomousComponent pushLeftButton = new GiroTurnBeacon(10.0, opModeContext, "Turn left");
        final AutonomousComponent pushRightButton = new GiroTurnBeacon(-10.0, opModeContext, "Turn right");
        final AutonomousComponent driveInLeftButton = new GiroDriveEncoderBeacon(10.0, 0.3, 200, opModeContext, "Press left button");
        final AutonomousComponent driveInRightButton = new GiroDriveEncoderBeacon(-10.0, 0.3, 200, opModeContext, "Press right button");
        final AutonomousComponent squareUp = new GiroTurnBeacon(0.0, opModeContext, "Square up");

        this.setInitialComponent(selectSide);
        this.addConnection(selectSide, SelectBeaconSide.LEFT_TRANSITION, pushLeftButton);
        this.addConnection(selectSide, SelectBeaconSide.RIGHT_TRANSITION, pushRightButton);
        this.addExitConnection(selectSide, SelectBeaconSide.UNKNOWN_TRANSITION);
        this.addConnection(pushLeftButton, NEXT_STATE, driveInLeftButton);
        this.addConnection(pushRightButton, NEXT_STATE, driveInRightButton);
        this.addConnection(driveInLeftButton, NEXT_STATE, squareUp);
        this.addConnection(driveInRightButton, NEXT_STATE, squareUp);
        this.addExitConnection(squareUp, NEXT_STATE);
    }
}
