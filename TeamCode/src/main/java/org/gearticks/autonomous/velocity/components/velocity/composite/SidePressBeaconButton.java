package org.gearticks.autonomous.velocity.components.velocity.composite;

import android.support.annotation.NonNull;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.EngageBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.GiroDriveEncoderBeacon;
import org.gearticks.autonomous.velocity.components.velocity.single.GiroTurnBeacon;
import org.gearticks.autonomous.velocity.components.velocity.single.SelectBeaconSide;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.vuforia.VuforiaConfiguration;

/**
 * Sample to create a composite autonomous component that will consist of a linear state-machine of 2 or more AutonomousComponents
 */
public class SidePressBeaconButton extends NetworkedStateMachine {

    public SidePressBeaconButton(double targetHeading, @NonNull VuforiaConfiguration vuforiaConfiguration, @NonNull VelocityConfiguration configuration, String id) {
        super();
        final AutonomousComponent selectSide = new SelectBeaconSide(vuforiaConfiguration, configuration, "Select beacon side");
        final AutonomousComponent driveForward = new GiroDriveEncoder(targetHeading, -0.15, 150, configuration, "To right button");
        final AutonomousComponent driveBackward = new GiroDriveEncoder(targetHeading, -0.15, 700, configuration, "To left button");
        final AutonomousComponent bumpBeacon = new GiroTurn(targetHeading - 18, configuration, "Press button");
        final AutonomousComponent squareUp = new GiroTurn(targetHeading, configuration, "Square up");

        this.setInitialComponent(selectSide);
        this.addConnection(selectSide, SelectBeaconSide.LEFT_TRANSITION, driveBackward);
        this.addConnection(selectSide, SelectBeaconSide.RIGHT_TRANSITION, driveForward);
        this.addConnection(driveBackward, NEXT_STATE, bumpBeacon);
        this.addConnection(driveForward, NEXT_STATE, bumpBeacon);
        this.addConnection(bumpBeacon, NEXT_STATE, squareUp);
        this.addExitConnection(squareUp, NEXT_STATE);
    }
}
