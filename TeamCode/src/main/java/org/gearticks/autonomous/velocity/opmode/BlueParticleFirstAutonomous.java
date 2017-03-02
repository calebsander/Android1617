package org.gearticks.autonomous.velocity.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.ParallelComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.Stopped;
import org.gearticks.autonomous.velocity.components.velocity.composite.BlueSideAutonomous;
import org.gearticks.autonomous.velocity.components.velocity.composite.Shoot2Balls;
import org.gearticks.autonomous.velocity.components.velocity.single.DeploySideRollers;
import org.gearticks.hardware.configurations.VelocityConfiguration;

@Autonomous
public class BlueParticleFirstAutonomous extends InitializedAutonomous {

    protected AutonomousComponent getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
        final LinearStateMachine sm = new LinearStateMachine();

        //Initialization components
        final ParallelComponent shootAndDeployRollers = new ParallelComponent("Shoot and rollers");
        shootAndDeployRollers.addComponent(new DeploySideRollers(opModeContext, "Deploy rollers"));
        shootAndDeployRollers.addComponent(new Shoot2Balls(true, opModeContext, "Shoot 2 balls"));
        sm.addComponent(shootAndDeployRollers);

        // Blue side driving components
        sm.addComponent(new BlueSideAutonomous(opModeContext));

        //End component
        final LinearStateMachine teardown = new LinearStateMachine("Teardown");
        teardown.addComponent(new Stopped(opModeContext));
        sm.addComponent(teardown);

        return sm;
    }
    protected boolean isV2() {
        return true;
    }
}
