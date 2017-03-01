package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.generic.Stopped;
import org.gearticks.autonomous.velocity.components.velocity.composite.BlueSideAutonomous;
import org.gearticks.autonomous.velocity.components.velocity.composite.Shoot2Balls;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageBeaconServo;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;

import static org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl.NEXT_STATE;


@Autonomous
public class BlueParticleFirstAutonomous extends VelocityBaseOpMode {
    private static final int DISTANCE_FROM_WALL = 9;

    @Override
    protected void loopBeforeStart() {
        super.loopBeforeStart();
        this.configuration.safeShooterStopper(VelocityConfiguration.MotorConstants.SHOOTER_STOPPER_UP);
        this.configuration.advanceShooterToDownSlowly();
        this.configuration.beaconPresserFrontIn();
        this.configuration.beaconPresserBackIn();
        this.configuration.engageTopLatch();
    }
    protected void matchStart() {
        super.matchStart();
        this.configuration.disengageTopLatch();
    }

    protected AutonomousComponent getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        final NetworkedStateMachine sm = new NetworkedStateMachine();

        //Initialization components
        final AutonomousComponent disengageBeaconServo = new DisengageBeaconServo(opModeContext, "Disengage beacon button");
        //shooter components
        final AutonomousComponent shoot2Balls = new Shoot2Balls(true, opModeContext, "Shoot 2 balls");

        // Blue side driving components
        final AutonomousComponent blueSide = new BlueSideAutonomous(DISTANCE_FROM_WALL, opModeContext);
        //End component

        final LinearStateMachine teardown = new LinearStateMachine("Teardown");
        teardown.addComponent(new Stopped(opModeContext));

        //Initialize
        sm.setInitialComponent(disengageBeaconServo);
        sm.addConnection(disengageBeaconServo, NEXT_STATE, shoot2Balls);
        sm.addConnection(shoot2Balls, NEXT_STATE, blueSide);
        sm.addConnection(blueSide, NEXT_STATE, teardown);

        return sm;
    }
    protected boolean isV2() {
        return true;
    }
}
