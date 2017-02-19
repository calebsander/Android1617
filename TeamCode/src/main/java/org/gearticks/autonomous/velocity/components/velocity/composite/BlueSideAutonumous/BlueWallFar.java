package org.gearticks.autonomous.velocity.components.velocity.composite.BlueSideAutonumous;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.experimental.GiroBananaTurnEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveAlongWallLine;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveToLine;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.velocity.composite.Shoot2Balls;
import org.gearticks.autonomous.velocity.components.velocity.single.DeploySideRollers;
import org.gearticks.autonomous.velocity.components.velocity.single.RaiseSideRollers;
import org.gearticks.hardware.configurations.VelocityConfiguration;

public class BlueWallFar extends LinearStateMachine {
    public BlueWallFar(int distanceFromWall, OpModeContext<VelocityConfiguration> opModeContext, String id) {
        super();

        //Drive to wall

        // Drive froward
        addComponent(new GiroDriveEncoder(0.0, 0.1, 20, opModeContext, "Straighten out"));


        // Shoot 2 balls
        addComponent(new Shoot2Balls(true, opModeContext, "Shoot 2 balls"));

        // Drive around cap ball



    }
}
