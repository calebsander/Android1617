package org.gearticks.opmodes.teleop.components;

import com.qualcomm.ftccommon.FtcEventLoop;

import org.gearticks.GamepadWrapper;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.autonomous.generic.opmode.HardwareComponentAutonomous;
import org.gearticks.autonomous.generic.statemachine.LinearStateMachine;
import org.gearticks.autonomous.velocity.components.generic.Delay;
import org.gearticks.autonomous.velocity.components.generic.GiroDriveEncoder;
import org.gearticks.autonomous.velocity.components.generic.GiroTurn;
import org.gearticks.autonomous.velocity.components.velocity.composite.Shoot2Balls;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.joystickoptions.IncrementOption;
import org.gearticks.joystickoptions.JoystickOptionController;
import org.gearticks.opmodes.teleop.VelocityDrive;

/**
 * Created by kevin on 5/6/2017.
 */

public class TeleopShooterStopper extends AutonomousComponentHardware<VelocityConfiguration, AutonomousComponent.DefaultTransition>{
    GamepadWrapper[] gamepads;
        public TeleopShooterStopper(OpModeContext<VelocityConfiguration> opModeContext) {
            super(opModeContext, DefaultTransition.class);
            this.gamepads = opModeContext.gamepads;
        }

        public DefaultTransition run() {
            final double shooterStopperPower;
            if (gamepads[VelocityDrive.JACK].dpadUp()) {
                shooterStopperPower = VelocityConfiguration.MotorConstants.SHOOTER_STOPPER_UP;
            }
            else if (gamepads[VelocityDrive.JACK].dpadDown()) {
                shooterStopperPower = VelocityConfiguration.MotorConstants.SHOOTER_STOPPER_DOWN;
            }
            else {
                shooterStopperPower = MotorWrapper.STOPPED;
            }
            this.configuration.safeShooterStopper(shooterStopperPower);
            return null;
        }
}


