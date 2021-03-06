package org.gearticks.components.velocity.component.composite;

import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponentAbstract;
import org.gearticks.components.generic.component.ParallelComponent;
import org.gearticks.components.generic.statemachine.LinearStateMachine;
import org.gearticks.components.velocity.component.servo.EngageClutch;
import org.gearticks.components.velocity.component.motor.IntakeUntilBadBoy;
import org.gearticks.components.velocity.component.servo.LoadBall;
import org.gearticks.components.velocity.component.motor.MoveShooterDown;
import org.gearticks.components.velocity.component.servo.ResetSnake;
import org.gearticks.components.velocity.component.motor.RunIntake;
import org.gearticks.components.velocity.component.motor.ShootBall;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class Shoot3Balls extends LinearStateMachine {

    /**
     * @param ballAlreadyIn - ball is already in intake
     * @param opModeContext - the OpModeContext this is running in
     * @param id - descriptive name for logging
     */
    public Shoot3Balls(boolean ballAlreadyIn, OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(id);

        //Shooting
        final LinearStateMachine shoot = new LinearStateMachine("Shoot");
        shoot.addComponent(new ShootBall(opModeContext, "Shoot 1st ball"));
        shoot.addComponent(new ShooterDownAndLoadSnake(opModeContext, "Shooter and Snake"));
        shoot.addComponent(new ResetSnake(false, opModeContext, "Reset Snake"));

        //Shooting and intake
        final ParallelComponent shootingAndIntaking = new ParallelComponent();
        shootingAndIntaking.addComponent(shoot);
        if (!ballAlreadyIn) shootingAndIntaking.addComponent(new RunIntake(2.0, true, opModeContext, "Pull in third ball"));

        //Shoot and reset
        final LinearStateMachine shootAndReset = new LinearStateMachine("Shoot and reset");
        shootAndReset.addComponent(new ShootBall(opModeContext, "Shoot 2nd ball"));
        shootAndReset.addComponent(new MoveShooterDown(opModeContext, "Reset for third shot"));

        //Load 3rd ball in snake
        final LinearStateMachine thirdBallIntoSnake = new LinearStateMachine("Third ball into snake");
        thirdBallIntoSnake.addComponent(new EngageClutch(opModeContext, "Engage clutch"));
        thirdBallIntoSnake.addComponent(new IntakeUntilBadBoy(2.0, opModeContext));
        thirdBallIntoSnake.addComponent(new RunIntake(0.5, false, opModeContext, "Intake through bad boys"));

        //Shoot and Load 3rd ball in snake
        final ParallelComponent shootingAndLoading = new ParallelComponent();
        shootingAndLoading.addComponent(shootAndReset);
        shootingAndLoading.addComponent(thirdBallIntoSnake);

        //State machine
        addComponent(new OpModeComponentAbstract<DefaultTransition>(DefaultTransition.class, "Stop reading IMU") {
            @Override
            public DefaultTransition run() {
                final DefaultTransition superTransition = super.run();
                if (superTransition != null) return superTransition;

                opModeContext.configuration.imu.eulerRequest.stopReading();
                return DefaultTransition.DEFAULT;
            }
        });
        addComponent(shootingAndIntaking);
        addComponent(shootingAndLoading);
        addComponent(new LoadBall(opModeContext, "Load 3nd ball"));
        addComponent(new ResetSnake(false, opModeContext, "Reset Snake"));
        addComponent(new ShootBall(opModeContext, "Shoot 3nd ball"));
        addComponent(new OpModeComponentAbstract<DefaultTransition>(DefaultTransition.class, "Start reading IMU") {
            @Override
            public DefaultTransition run() {
                final DefaultTransition superTransition = super.run();
                if (superTransition != null) return superTransition;

                opModeContext.configuration.imu.eulerRequest.startReading();
                return DefaultTransition.DEFAULT;
            }
        });

    }
}
