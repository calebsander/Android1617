package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.opmode.VelocityLinearAutonomousBaseOpMode;
import org.gearticks.autonomous.velocity.components.CompleteBallLoad;
import org.gearticks.autonomous.velocity.components.DebugPause;
import org.gearticks.autonomous.velocity.components.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.GiroTurn;
import org.gearticks.autonomous.velocity.components.LoadBall;
import org.gearticks.autonomous.velocity.components.MoveShooterDown;
import org.gearticks.autonomous.velocity.components.ResetSnake;
import org.gearticks.autonomous.velocity.components.ShootBall;
import org.gearticks.autonomous.velocity.components.Wait;
import org.gearticks.GamepadWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Autonomous(name = "Component Test")
public class ComponentTest extends VelocityLinearAutonomousBaseOpMode {
    private static final int CALVIN = 0, JACK = 1;

    protected List<AutonomousComponent> createComponents(){
        List<AutonomousComponent> components = new ArrayList<>();
        components.add(new MoveShooterDown(this.configuration, "MoveShooterDown"));
//        components.add(new LoadBall(this.configuration, "Load Ball"));
//        components.add(new ResetSnake(this.configuration, "Reset Snake"));
        components.add(new CompleteBallLoad(this.configuration, "Load Ball + Reset"));
        components.add(new DebugPause(this.gamepads, this.telemetry, this.configuration, "Wait until A is pressed"));
        components.add(new ShootBall(this.configuration, "Shoot Ball"));
        return components;
    }
}
