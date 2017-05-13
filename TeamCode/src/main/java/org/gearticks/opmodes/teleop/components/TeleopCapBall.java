package org.gearticks.opmodes.teleop.components;

import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import org.gearticks.GamepadWrapper;
import org.gearticks.autonomous.generic.OpModeContext;
import org.gearticks.autonomous.generic.component.AutonomousComponent.NoTransition;
import org.gearticks.autonomous.generic.component.AutonomousComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.opmodes.teleop.VelocityDrive;

public class TeleopCapBall extends AutonomousComponentHardware<VelocityConfiguration, NoTransition> {
    private final GamepadWrapper[] gamepads;

    public TeleopCapBall(OpModeContext<VelocityConfiguration> opModeContext) {
        super(opModeContext, NoTransition.class);
        this.gamepads = opModeContext.gamepads;
    }

    public NoTransition run() {
        final double capBallPower;
        final RunMode capBallMode;
        if (this.gamepads[VelocityDrive.JACK].getY()) {
            if (this.configuration.isCapBallUp()) {
                capBallPower = VelocityConfiguration.MotorConstants.CAP_BALL_SUPER_SLOW_UP;
                capBallMode = RunMode.RUN_WITHOUT_ENCODER;
            }
            else {
                capBallPower = VelocityConfiguration.MotorConstants.CAP_BALL_UP;
                capBallMode = RunMode.RUN_USING_ENCODER;
            }
        }
        else if (this.gamepads[VelocityDrive.JACK].getA()) {
            capBallPower = VelocityConfiguration.MotorConstants.CAP_BALL_DOWN;
            capBallMode = RunMode.RUN_USING_ENCODER;
        }
        else {
            capBallPower = MotorWrapper.STOPPED;
            capBallMode = RunMode.RUN_USING_ENCODER;
        }
        final double capBallScaling;
        if (this.gamepads[VelocityDrive.JACK].getBack()) capBallScaling = VelocityConfiguration.MotorConstants.CAP_BALL_SLOW_SCALE;
        else capBallScaling = 1.0;
        this.configuration.capBall.setPower(capBallPower * capBallScaling);
        this.configuration.capBall.setRunMode(capBallMode);

        return null;
    }
}
