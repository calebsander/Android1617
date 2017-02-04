package org.gearticks.autonomous.velocity.components.velocity.composite;

import android.support.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.gearticks.GamepadWrapper;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.DebugPause;
import org.gearticks.autonomous.velocity.components.generic.Wait;
import org.gearticks.autonomous.velocity.components.velocity.single.LoadBall;
import org.gearticks.autonomous.velocity.components.velocity.single.MoveShooterDown;
import org.gearticks.autonomous.velocity.components.velocity.single.ResetSnake;
import org.gearticks.autonomous.velocity.components.velocity.single.ShootBall;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class Shoot2Balls extends LinearStateMachine {
    public Shoot2Balls(@NonNull GamepadWrapper[] gamepads, Telemetry telemetry, @NonNull VelocityConfiguration configuration, String id) {
        super();
        addComponent(new MoveShooterDown(configuration, "MoveShooterDown"));
        addComponent(new DebugPause(gamepads, telemetry, configuration, "Press A to continue"));
        addComponent(new Wait(0.3, "Wait for 0.5 sec"));
        addComponent(new ShootBall(configuration, "Shoot 1st ball"));
        addComponent(new DebugPause(gamepads, telemetry, configuration, "Press A to continue"));
        addComponent(new MoveShooterDown(configuration, "Move Shooter Down"));
        addComponent(new DebugPause(gamepads, telemetry, configuration, "Press A to continue"));
        addComponent(new LoadBall(configuration, "Load 2nd ball"));
        addComponent(new ResetSnake(configuration, "Reset Snake"));
        addComponent(new ShootBall(configuration, "Shoot 2nd ball"));
    }
}
