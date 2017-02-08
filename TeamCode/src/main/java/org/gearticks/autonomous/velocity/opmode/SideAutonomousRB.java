package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.experimental.AutonomousSideSelector;
import org.gearticks.autonomous.velocity.components.generic.DebugPause;
import org.gearticks.autonomous.velocity.components.generic.Stopped;
import org.gearticks.autonomous.velocity.components.velocity.composite.BlueSideAutonomous;
import org.gearticks.autonomous.velocity.components.velocity.composite.RedSideAutonomous;
import org.gearticks.autonomous.velocity.components.velocity.composite.Shoot2Balls;
import org.gearticks.autonomous.velocity.components.velocity.single.DeploySideRollers;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageBeaconServo;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.vuforia.VuforiaConfiguration;

import static org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl.NEXT_STATE;

@Autonomous
public class SideAutonomousRB extends VelocityBaseOpMode {
    private static final int DISTANCE_FROM_WALL = 10;

    @Override
    protected void loopBeforeStart() {
        super.loopBeforeStart();
        this.configuration.safeShooterStopper(VelocityConfiguration.MotorConstants.SHOOTER_STOPPER_UP);
        this.configuration.advanceShooterToDown();
    }

    protected AutonomousComponent getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        final NetworkedStateMachine sm = new NetworkedStateMachine();

        //Initialization components
        final AutonomousComponent sideSelector = new AutonomousSideSelector(opModeContext);
        final AutonomousComponent disengageBeaconServo = new DisengageBeaconServo(opModeContext, "Disengage beacon button");
        //final AutonomousComponent deploySideRollers = new DeploySideRollers(this.configuration, "Deploy rollers");
        //shooter components
        final AutonomousComponent shoot2Balls = new Shoot2Balls(true, opModeContext, "Shoot 2 balls");

        // Blue side driving components
        final AutonomousComponent blueSide = new BlueSideAutonomous(DISTANCE_FROM_WALL, opModeContext);

        //Red side driving components
        final AutonomousComponent redSide = new RedSideAutonomous(DISTANCE_FROM_WALL, opModeContext);

        //End component
        final LinearStateMachine teardown = new LinearStateMachine("Teardown");
        teardown.addComponent(new Stopped(opModeContext));

        //Initialize
        sm.setInitialComponent(disengageBeaconServo);
        sm.addConnection(disengageBeaconServo, NEXT_STATE, shoot2Balls);
        //sm.addConnection(deploySideRollers, NEXT_STATE, shoot2Balls);
        sm.addConnection(shoot2Balls, NEXT_STATE, sideSelector);

        //Blue Side
        sm.addConnection(sideSelector, AutonomousSideSelector.BLUE, blueSide);
        sm.addConnection(blueSide, NEXT_STATE, teardown);

        //Red side
        sm.addConnection(sideSelector, AutonomousSideSelector.RED, redSide);
        sm.addConnection(redSide, NEXT_STATE, teardown);

        return sm;
    }
    protected boolean isV2() {
        return true;
    }
}
