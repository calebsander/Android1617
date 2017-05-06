package org.gearticks.opmodes.teleop.components;

import org.gearticks.GamepadWrapper;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.opmodes.teleop.VelocityDrive;

/**
 * Created by kevin on 5/6/2017.
 */

public class TeleopRollers extends AutonomousComponentHardware<VelocityConfiguration, AutonomousComponent.DefaultTransition> {
    GamepadWrapper[] gamepads;
    boolean rollersDeployed;
    public TeleopRollers(OpModeContext<VelocityConfiguration> opModeContext) {
        super(opModeContext, AutonomousComponent.DefaultTransition.class);
        this.gamepads = opModeContext.gamepads;
        this.rollersDeployed = true;
    }

    public AutonomousComponent.DefaultTransition run() {
        final double shooterStopperPower;
        if (this.gamepads[VelocityDrive.CALVIN].newly(GamepadWrapper::getX)) {
            this.rollersDeployed = !this.rollersDeployed;
            if (this.rollersDeployed) this.configuration.rollersDown();
            else this.configuration.rollersUp();
        }
        return null;
    }
}
