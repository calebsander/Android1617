package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.NetworkedStateMachine;
import org.gearticks.autonomous.velocity.components.experimental.AutonomousBeaconSelector;
import org.gearticks.autonomous.velocity.components.velocity.composite.BlueSideAutonumous.BlueSideAutonomousNF;
import org.gearticks.autonomous.velocity.components.velocity.single.DisengageBeaconServo;
import org.gearticks.autonomous.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.hardware.configurations.VelocityConfiguration;

import static org.gearticks.autonomous.generic.component.AutonomousComponentAbstractImpl.NEXT_STATE;

@Autonomous
public class SideAutonOptions extends VelocityBaseOpMode {
    private static final int DISTANCE_FROM_WALL = 9;

    @Override
    protected void loopBeforeStart() {
        super.loopBeforeStart();
        this.configuration.safeShooterStopper(VelocityConfiguration.MotorConstants.SHOOTER_STOPPER_UP);
        this.configuration.advanceShooterToDown();
    }

    protected AutonomousComponent getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        final NetworkedStateMachine sm = new NetworkedStateMachine();

        //Components

        //Blue side
        final AutonomousComponent blueSideNF = new BlueSideAutonomousNF(DISTANCE_FROM_WALL, opModeContext);

        //Red side

        final AutonomousComponent disengageBeaconServo = new DisengageBeaconServo(opModeContext, "Disengage beacon button");
        final AutonomousComponent beaconSelector = new AutonomousBeaconSelector(opModeContext);
//        final AutonomousComponent shoot = new PositionShoot(opModeContext, "Shoot from position 1");
//        final AutonomousComponent goToWall = new GoToWall(opModeContext, "Go to Wall");
//        final AutonomousComponent beacon = new FindPressBeacon(opModeContext, "Hit Beacons");
//        final AutonomousComponent park = new Park(opModeContext, "Park");

        //State Machine
        sm.setInitialComponent(disengageBeaconServo);
        sm.addConnection(disengageBeaconServo, NEXT_STATE, blueSideNF);
        sm.addExitConnection(blueSideNF, NEXT_STATE);

        return sm;
    }
    protected boolean isV2() {
        return true;
    }
}
