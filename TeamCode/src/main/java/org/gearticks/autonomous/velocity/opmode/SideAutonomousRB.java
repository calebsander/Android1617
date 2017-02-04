package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.experimental.AutonomousSideSelecter;
import org.gearticks.autonomous.velocity.components.experimental.GiroBananaTurnEncoder;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.experimental.GiroDriveAlongWallLine;
import org.gearticks.autonomous.velocity.components.generic.DebugPause;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.generic.Stopped;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.components.velocity.composite.BlueSideAutonomous;
import org.gearticks.autonomous.velocity.components.velocity.composite.RedSideAutonomous;
import org.gearticks.autonomous.velocity.components.velocity.composite.Shoot2Balls;
import org.gearticks.autonomous.velocity.components.velocity.composite.SidePressBeaconButton;
import org.gearticks.autonomous.velocity.components.velocity.single.DeploySideRollers;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageBeaconServo;
import org.gearticks.autonomous.velocity.components.velocity.single.LoadBall;
import org.gearticks.autonomous.velocity.components.velocity.single.MoveShooterDown;
import org.gearticks.autonomous.velocity.components.velocity.single.ResetSnake;
import org.gearticks.autonomous.velocity.components.velocity.single.SelectBeaconSide;
import org.gearticks.autonomous.velocity.components.velocity.single.ShootBall;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.vuforia.VuforiaConfiguration;

import static org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl.NEXT_STATE;

@Autonomous
public class SideAutonomousRB extends VelocityBaseOpMode {
    protected AutonomousComponent getComponent() {
        final VuforiaConfiguration vuforiaConfiguration = new VuforiaConfiguration();
        final NetworkedStateMachine sm = new NetworkedStateMachine();

        int distanceFromWall = 10;

        // basic components
        final AutonomousComponent stop = new Stopped(this.configuration);
        final AutonomousComponent sideSelecter = new AutonomousSideSelecter(this.configuration);
        final AutonomousComponent disengageBeaconServo = new DisengageBeaconServo(this.configuration, "Disengage beacon button");
        final AutonomousComponent wait = new Wait(0.3, "Wait for 0.5 sec");
        final AutonomousComponent debugPause = new DebugPause(gamepads, telemetry, this.configuration, "Press A to continue");
        final AutonomousComponent deploySideRollers = new DeploySideRollers(this.configuration, "Press A to continue");


        //shooter components
        final AutonomousComponent shoot2Balls = new Shoot2Balls(gamepads, telemetry, this.configuration, "Shoot 2 balls");

        // Blue side driving components
        final AutonomousComponent blueSide = new BlueSideAutonomous(distanceFromWall, gamepads, telemetry, vuforiaConfiguration, this.configuration);

        //Red side driving components
        final AutonomousComponent redSide = new RedSideAutonomous(distanceFromWall, gamepads, telemetry, vuforiaConfiguration, this.configuration);

        //Shoot balls
        sm.setInitialComponent(disengageBeaconServo);
        sm.addConnection(disengageBeaconServo, NEXT_STATE, deploySideRollers);
        sm.addConnection(deploySideRollers, NEXT_STATE, sideSelecter);
        //sm.addConnection(shoot2Balls, NEXT_STATE, sideSelecter);

        //Blue Side
        sm.addConnection(sideSelecter, AutonomousSideSelecter.BLUE, blueSide);
        sm.addConnection(blueSide, NEXT_STATE, stop);

        //Red side
        sm.addConnection(sideSelecter, AutonomousSideSelecter.RED, redSide);
        sm.addConnection(redSide, AutonomousSideSelecter.RED, stop);

        //End
        sm.addExitConnection(stop, NEXT_STATE);

        return sm;
    }
}
