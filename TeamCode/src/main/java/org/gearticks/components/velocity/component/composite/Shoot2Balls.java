package org.gearticks.components.velocity.component.composite;

import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.statemachine.LinearStateMachine;
import org.gearticks.components.velocity.component.motor.MoveShooterDown;
import org.gearticks.components.velocity.component.servo.ResetSnake;
import org.gearticks.components.velocity.component.motor.ShootBall;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class Shoot2Balls extends LinearStateMachine {

    /**
     *
     * @param alreadyDown - if shooter is already down
     * @param opModeContext - the OpModeContext this is running in
     * @param id - descriptive name for logging
     */
    public Shoot2Balls(boolean alreadyDown, OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(id);
        if (!alreadyDown) addComponent(new MoveShooterDown(opModeContext, "MoveShooterDown"));
        addComponent(new ShootBall(opModeContext, "Shoot 1st ball"));
        addComponent(new ShooterDownAndLoadSnake(opModeContext, "Shooter and Snake"));
        addComponent(new ResetSnake(false, opModeContext, "Reset Snake"));
        addComponent(new ShootBall(opModeContext, "Shoot 2nd ball"));
    }
}
