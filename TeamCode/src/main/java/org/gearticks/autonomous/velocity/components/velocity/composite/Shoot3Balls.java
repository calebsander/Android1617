package org.gearticks.autonomous.velocity.components.velocity.composite;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.ParallelComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.velocity.composite.Shoot2Balls;
import org.gearticks.autonomous.velocity.components.velocity.single.ClutchClutch;
import org.gearticks.autonomous.velocity.components.velocity.single.EngageClutch;
import org.gearticks.autonomous.velocity.components.velocity.single.LoadBall;
import org.gearticks.autonomous.velocity.components.velocity.single.MoveShooterDown;
import org.gearticks.autonomous.velocity.components.velocity.single.ResetSnake;
import org.gearticks.autonomous.velocity.components.velocity.single.RunIntake;
import org.gearticks.autonomous.velocity.components.velocity.single.ShootBall;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class Shoot3Balls extends LinearStateMachine {

    /**
     *
     * @param opModeContext - the OpModeContext this is running in
     * @param id - descriptive name for logging
     */
    public Shoot3Balls(OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super(id);
        ParallelComponent shootingAndIntake = new ParallelComponent();
        shootingAndIntake.addComponent(new Shoot2Balls(true, opModeContext, "Shoot 2 balls"));
        shootingAndIntake.addComponent(new IntakeClutched(3.0, opModeContext, "Run intake clutched"));


        //State machine
        addComponent(shootingAndIntake);
        addComponent(new IntakeEngaged(2.0, opModeContext, "Run intake"));
        addComponent(new LoadBall(opModeContext, "Load 3nd ball"));
        addComponent(new ResetSnake(false, opModeContext, "Reset Snake"));
        addComponent(new ShootBall(opModeContext, "Shoot 3nd ball"));

    }
}
