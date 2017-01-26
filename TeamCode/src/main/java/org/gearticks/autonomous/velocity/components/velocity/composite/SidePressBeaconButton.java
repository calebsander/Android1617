package org.gearticks.autonomous.velocity.components.velocity.composite;

import android.support.annotation.NonNull;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.LeftPressBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.RightPressBeaconServo;
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
        final AutonomousComponent leftButton = new LeftPressBeaconServo(configuration, "Left press beacon");
        final AutonomousComponent rightButton = new RightPressBeaconServo(configuration, "Right press beacon");
        final AutonomousComponent resetButton = new DisengageBeaconServo(configuration, "Disengage beacon presser");


        this.setInitialComponent(selectSide);
        this.addConnection(selectSide, SelectBeaconSide.LEFT_TRANSITION, leftButton);
        this.addConnection(selectSide, SelectBeaconSide.RIGHT_TRANSITION, rightButton);
        this.addConnection(leftButton, NEXT_STATE, resetButton);
        this.addConnection(rightButton, NEXT_STATE, resetButton);
        this.addExitConnection(resetButton, NEXT_STATE);
    }
}
