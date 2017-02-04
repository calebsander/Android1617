package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.experimental.AutonomousSideSelector;
import org.gearticks.autonomous.velocity.components.generic.Stopped;
import org.gearticks.autonomous.velocity.components.velocity.composite.BlueSideAutonomous;
import org.gearticks.autonomous.velocity.components.velocity.composite.RedSideAutonomous;
import org.gearticks.autonomous.velocity.components.velocity.composite.Shoot2Balls;
import org.gearticks.autonomous.velocity.components.velocity.single.DeploySideRollers;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.RaiseSideRollers;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.vuforia.VuforiaConfiguration;

import static org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl.NEXT_STATE;

@Autonomous
public class SideAutonomousRB extends VelocityBaseOpMode {
    private static final int DISTANCE_FROM_WALL = 10;

    protected AutonomousComponent getComponent() {
        final VuforiaConfiguration vuforiaConfiguration = new VuforiaConfiguration();
        final NetworkedStateMachine sm = new NetworkedStateMachine();

        //Initialization components
        final AutonomousComponent sideSelector = new AutonomousSideSelector(this.configuration);
        final AutonomousComponent disengageBeaconServo = new DisengageBeaconServo(this.configuration, "Disengage beacon button");
        final AutonomousComponent deploySideRollers = new DeploySideRollers(this.configuration, "Deploy rollers");

        //shooter components
        final AutonomousComponent shoot2Balls = new Shoot2Balls(gamepads, telemetry, this.configuration, "Shoot 2 balls");

        // Blue side driving components
        final AutonomousComponent blueSide = new BlueSideAutonomous(DISTANCE_FROM_WALL, gamepads, telemetry, vuforiaConfiguration, this.configuration);

        //Red side driving components
        final AutonomousComponent redSide = new RedSideAutonomous(DISTANCE_FROM_WALL, gamepads, telemetry, vuforiaConfiguration, this.configuration);

        //End component
        final LinearStateMachine teardown = new LinearStateMachine("Teardown");
        teardown.addComponent(new RaiseSideRollers(this.configuration, "Raise rollers"));
        teardown.addComponent(new Stopped(this.configuration));

        //Initialize
        sm.setInitialComponent(disengageBeaconServo);
        sm.addConnection(disengageBeaconServo, NEXT_STATE, deploySideRollers);
        sm.addConnection(deploySideRollers, NEXT_STATE, sideSelector);
        //sm.addConnection(shoot2Balls, NEXT_STATE, sideSelector);

        //Blue Side
        sm.addConnection(sideSelector, AutonomousSideSelector.BLUE, blueSide);
        sm.addConnection(blueSide, NEXT_STATE, teardown);

        //Red side
        sm.addConnection(sideSelector, AutonomousSideSelector.RED, redSide);
        sm.addConnection(redSide, NEXT_STATE, teardown);

        return sm;
    }
}
