package org.gearticks.autonomous.velocity.components.velocity.composite;

import android.support.annotation.NonNull;

import org.gearticks.autonomous.velocity.components.velocity.single.GiroDriveEncoderBeacon;
import org.gearticks.autonomous.velocity.components.velocity.single.GiroTurnBeacon;
import org.gearticks.autonomous.velocity.components.velocity.single.SelectBeaconSide;
import org.gearticks.vuforia.VuforiaConfiguration;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.hardware.configurations.VelocityConfiguration;

/**
 * Sample to create a composite autonomous component that will consist of a linear state-machine of 2 or more AutonomousComponents
 */
public class NonLinearPressBeaconButton extends NetworkedStateMachine {

    public NonLinearPressBeaconButton(@NonNull VuforiaConfiguration vuforiaConfiguration, @NonNull VelocityConfiguration configuration, String id) {
        final AutonomousComponent selectSide = new SelectBeaconSide(vuforiaConfiguration, configuration, "Press beacon button");
        final AutonomousComponent pushLeftButton = new GiroTurnBeacon(10.0, configuration, "Turn left");
        final AutonomousComponent pushRightButton = new GiroTurnBeacon(-10.0, configuration, "Turn right");
        final AutonomousComponent driveInLeftButton = new GiroDriveEncoderBeacon(10.0, 0.3, 200, configuration, "Press left button");
        final AutonomousComponent driveInRightButton = new GiroDriveEncoderBeacon(-10.0, 0.3, 200, configuration, "Press right button");
        final AutonomousComponent squareUp = new GiroTurnBeacon(0.0, configuration, "Square up");

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
