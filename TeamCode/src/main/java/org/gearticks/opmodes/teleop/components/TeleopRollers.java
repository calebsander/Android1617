package org.gearticks.opmodes.teleop.components;

import org.gearticks.GamepadWrapper;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent.NoTransition;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.opmodes.teleop.VelocityDrive;

public class TeleopRollers extends AutonomousComponentHardware<VelocityConfiguration, NoTransition> {
    private final GamepadWrapper[] gamepads;
    private boolean rollersDeployed;

    public TeleopRollers(OpModeContext<VelocityConfiguration> opModeContext) {
        super(opModeContext, NoTransition.class);
        this.gamepads = opModeContext.gamepads;
        this.rollersDeployed = true;
    }

    public void setup() {
        super.setup();
        this.configuration.rollersDown();
    }
    public NoTransition run() {
        if (this.gamepads[VelocityDrive.CALVIN].newly(GamepadWrapper::getX)) {
            this.rollersDeployed = !this.rollersDeployed;
            if (this.rollersDeployed) this.configuration.rollersDown();
            else this.configuration.rollersUp();
        }
        return null;
    }
}
