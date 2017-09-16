package org.gearticks.opmodes.teleop.components;

import org.gearticks.GamepadWrapper;
import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent.NoTransition;
import org.gearticks.components.generic.component.OpModeComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.opmodes.teleop.VelocityDrive;

public class TeleopShooterStopper extends OpModeComponentHardware<VelocityConfiguration, NoTransition> {
    private final GamepadWrapper[] gamepads;

    public TeleopShooterStopper(OpModeContext<VelocityConfiguration> opModeContext) {
        super(opModeContext, NoTransition.class);
        this.gamepads = opModeContext.gamepads;
    }

    public NoTransition run() {
        final double shooterStopperPower;
        if (this.gamepads[VelocityDrive.JACK].dpadUp()) {
            shooterStopperPower = VelocityConfiguration.MotorConstants.SHOOTER_STOPPER_UP;
        }
        else if (this.gamepads[VelocityDrive.JACK].dpadDown()) {
            shooterStopperPower = VelocityConfiguration.MotorConstants.SHOOTER_STOPPER_DOWN;
        }
        else {
            shooterStopperPower = MotorWrapper.STOPPED;
        }
        this.configuration.safeShooterStopper(shooterStopperPower);
        return null;
    }
}
