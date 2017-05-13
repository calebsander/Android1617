package org.gearticks.opmodes.teleop.components;

import org.gearticks.GamepadWrapper;
import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent.NoTransition;
import org.gearticks.components.generic.component.OpModeComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.opmodes.teleop.VelocityDrive;

public class TeleopIntake extends OpModeComponentHardware<VelocityConfiguration, NoTransition> {
    private final GamepadWrapper[] gamepads;

    public TeleopIntake(OpModeContext<VelocityConfiguration> opModeContext) {
        super(opModeContext, NoTransition.class);
        this.gamepads = opModeContext.gamepads;
    }

    public NoTransition run() {
        final double intakePower;
        if (this.gamepads[VelocityDrive.CALVIN].getRightBumper() || this.gamepads[VelocityDrive.JACK].getRightBumper()) {
            intakePower = VelocityConfiguration.MotorConstants.INTAKE_IN;
        }
        else if (this.gamepads[VelocityDrive.CALVIN].getRightTrigger() || this.gamepads[VelocityDrive.JACK].getRightTrigger()) {
            intakePower = VelocityConfiguration.MotorConstants.INTAKE_OUT;
        }
        else {
            intakePower = MotorWrapper.STOPPED; //leave intake off by default to save battery
        }
        this.configuration.intake.setPower(intakePower);
        return null;
    }

}
