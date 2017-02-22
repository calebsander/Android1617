package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.ParallelComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.velocity.single.EngageClutch;
import org.gearticks.autonomous.velocity.components.velocity.single.IntakeUntilBadBoy;
import org.gearticks.autonomous.velocity.components.velocity.single.LoadBall;
import org.gearticks.autonomous.velocity.components.velocity.single.MoveShooterDown;
import org.gearticks.autonomous.velocity.components.velocity.single.ResetSnake;
import org.gearticks.autonomous.velocity.components.velocity.single.RunIntake;
import org.gearticks.autonomous.velocity.components.velocity.single.ShootBall;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class Shoot3Balls extends LinearStateMachine {

    /**
     *
     * @param ballAlreadyIn - ball is already in intake
     * @param opModeContext - the OpModeContext this is running in
     * @param id - descriptive name for logging
     */
    public Shoot3Balls(boolean ballAlreadyIn, OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(id);

        //Shooting
        final LinearStateMachine shoot = new LinearStateMachine("Shoot");
        shoot.addComponent(new ShootBall(opModeContext, "Shoot 1st ball"));
        shoot.addComponent(new MoveShooterDown(opModeContext, "Move Shooter Down"));
        shoot.addComponent(new LoadBall(opModeContext, "Load 2nd ball"));
        shoot.addComponent(new ResetSnake(false, opModeContext, "Reset Snake"));

        //Shooting and intake
        final ParallelComponent shootingAndIntaking = new ParallelComponent();
        shootingAndIntaking.addComponent(shoot);
        shootingAndIntaking.addComponent(new RunIntake(2.0, true, opModeContext, "Pull in third ball"));

        //Shoot and reset
        final LinearStateMachine shootAndReset = new LinearStateMachine("Shoot and reset");
        shootAndReset.addComponent(new ShootBall(opModeContext, "Shoot 2nd ball"));
        shootAndReset.addComponent(new MoveShooterDown(opModeContext, "Reset for third shot"));

        //Load 3rd ball in snake
        final LinearStateMachine thirdBallIntoSnake = new LinearStateMachine("Third ball into snake");
        thirdBallIntoSnake.addComponent(new EngageClutch(opModeContext, "Engage clutch"));
        thirdBallIntoSnake.addComponent(new IntakeUntilBadBoy(2.0, opModeContext));

        //Shoot and Load 3rd ball in snake
        final ParallelComponent shootingAndLoading = new ParallelComponent();
        shootingAndLoading.addComponent(shootAndReset);
        shootingAndLoading.addComponent(thirdBallIntoSnake);

        //State machine
        if(ballAlreadyIn) addComponent(shoot);
        else addComponent(shootingAndIntaking);
        addComponent(shootingAndLoading);
        addComponent(new LoadBall(opModeContext, "Load 3nd ball"));
        addComponent(new ResetSnake(false, opModeContext, "Reset Snake"));
        addComponent(new ShootBall(opModeContext, "Shoot 3nd ball"));

    }
}
