package org.gearticks.autonomous.velocity.components.velocity.composite.BlueSideAutonumous;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.experimental.GiroBananaTurnEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallLine;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.velocity.composite.Shoot2Balls;
import org.gearticks.autonomous.velocity.components.velocity.single.DeploySideRollers;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class BlueWallNear extends LinearStateMachine {
    public BlueWallNear(int distanceFromWall, OpModeContext<VelocityConfiguration> opModeContext,  String id) {
        super();

        //Shoot 2 balls
        addComponent(new Shoot2Balls(true, opModeContext, "Shoot 2 balls"));

        //Drive to wall
        addComponent(new GiroBananaTurnEncoder(0.0, 18.0, 0.7, 500, opModeContext, "BTR 18 - 1000"));
        addComponent(new GiroBananaTurnEncoder(18, 90.0, 0.8, 7700, opModeContext, "BTR 90 - 7700"));
        addComponent(new GiroTurn(180.0, 0.1, 20, opModeContext, "Straighten out"));
        addComponent(new DeploySideRollers(opModeContext, "Deploy rollers"));
        //addComponent(new GiroDriveAlongWallEncoder(distanceFromWall, 180.0, -0.4, 2000, configuration, "Drive backwards"));
        addComponent(new GiroDriveAlongWallLine(distanceFromWall - 1, 180.0, 0.2, 6000, opModeContext, "Drive to beacon"));
    }
}
