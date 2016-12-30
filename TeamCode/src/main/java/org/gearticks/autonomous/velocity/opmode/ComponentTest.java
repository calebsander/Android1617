package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.vuforia.VuforiaConfiguration;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.velocity.components.DebugPause;
import org.gearticks.autonomous.velocity.components.FacePicture;
import org.gearticks.autonomous.velocity.components.VuforiaIn;

@Autonomous
public class ComponentTest extends VelocityBaseOpMode {
    private VuforiaConfiguration vuforiaConfiguration;

    protected AutonomousComponent getComponent() {
        this.vuforiaConfiguration = new VuforiaConfiguration();
        final LinearStateMachine sm = new LinearStateMachine();
        // Shoot 2 balls
//        components.add(new MoveShooterDown(this.configuration, "MoveShooterDown"));
//        components.add(new LoadBall(this.configuration, "Load 1st ball"));
//        components.add(new ResetSnake(this.configuration, "Reset Snake"));
////        components.add(new CompleteBallLoad(this.configuration, "Load Ball + Reset"));
////        components.add(new DebugPause(this.gamepads, this.telemetry, this.configuration, "Wait until X is pressed"));
//        components.add(new ShootBall(this.configuration, "Shoot 1st ball"));
//        components.add(new DebugPause(this.gamepads, this.telemetry, this.configuration, "Wait until X is pressed"));
//        components.add(new MoveShooterDown(this.configuration, "Move Shooter Down"));
//        components.add(new LoadBall(this.configuration, "Load 2nd ball"));
//        components.add(new ResetSnake(this.configuration, "Reset Snake"));
//        components.add(new ShootBall(this.configuration, "Shoot 2nd ball"));
//        components.add(new DebugPause(this.gamepads, this.telemetry, this.configuration, "Wait until X is pressed"));

        // Drive to beacon
//        components.add(new GiroDriveEncoder(0.0, 0.7, 1700, this.configuration, "Drive off wall for 1700 ticks"));
//        components.add(new Wait(500, this.configuration, "Wait before first turn"));
//        components.add(new GiroTurn(40.0, this.configuration, "Turn to far target"));
//        components.add(new GiroDriveEncoder(40.0, 0.7, 2900, this.configuration, "Drive in front of near target"));
//        components.add(new Wait(500, this.configuration, "Wait before second turn"));
//        components.add(new GiroTurn(90.0, this.configuration, "Face near target"));
//        components.add(new DebugPause(this.gamepads, this.telemetry, this.configuration, "Wait until X is pressed"));

        // Go to 1st beacon
        sm.addComponent(new DebugPause(this.gamepads, this.telemetry, this.configuration, "Wait until X is pressed"));

        sm.addComponent(new VuforiaIn(500F, this.vuforiaConfiguration, this.configuration, "Drive to near target"));
        sm.addComponent(new FacePicture(this.vuforiaConfiguration, this.configuration, "Face near target"));
        sm.addComponent(new VuforiaIn(175F, this.vuforiaConfiguration, this.configuration, "Drive closer to near target"));

        return sm;
    }

    @Override
    protected void matchStart(){
        super.matchStart();
        this.vuforiaConfiguration.activate(); // TODO may want to activate/deactivate in aut component
    }
}
