package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.velocity.single.LoadBall;
import org.gearticks.autonomous.velocity.components.velocity.single.MoveShooterDown;
import org.gearticks.autonomous.velocity.components.velocity.single.ResetSnake;
import org.gearticks.autonomous.velocity.components.velocity.single.ShootBall;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class Shoot2Balls extends LinearStateMachine {
    public Shoot2Balls(boolean alreadyDown, OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(id);
        if (!alreadyDown) addComponent(new MoveShooterDown(opModeContext, "MoveShooterDown"));
        addComponent(new ShootBall(opModeContext, "Shoot 1st ball"));
        addComponent(new MoveShooterDown(opModeContext, "Move Shooter Down"));
        addComponent(new LoadBall(opModeContext, "Load 2nd ball"));
        addComponent(new ResetSnake(false, opModeContext, "Reset Snake"));
        addComponent(new ShootBall(opModeContext, "Shoot 2nd ball"));
    }
}
