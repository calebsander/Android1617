package org.gearticks.autonomous.velocity.components.velocity.composite.BlueSideAutonumous;

import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.generic.statemachine.SelectedComponent;
import org.gearticks.autonomous.velocity.components.velocity.composite.BlueSideAutonumous.BluePressBeacons;
import org.gearticks.autonomous.velocity.components.velocity.composite.BlueSideAutonumous.BlueWallFar;
import org.gearticks.autonomous.velocity.components.velocity.composite.BlueSideAutonumous.BlueWallNear;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.joystickoptions.PositionOption;

import java.util.HashMap;

public class BlueSideAutonomousNF extends LinearStateMachine {
    public BlueSideAutonomousNF(final int distanceFromWall, final OpModeContext<VelocityConfiguration> opModeContext) {
        super();
        addComponent(new SelectedComponent<>(PositionOption.positionOption, new HashMap<PositionOption, AutonomousComponent>() {{
            put(PositionOption.NEAR, new BlueWallNear(distanceFromWall, opModeContext, "Go from near"));
            put(PositionOption.FAR, new BlueWallFar(distanceFromWall, opModeContext, "Go from far"));
        }}));
        addComponent(new BluePressBeacons(distanceFromWall, opModeContext));


    }
}
