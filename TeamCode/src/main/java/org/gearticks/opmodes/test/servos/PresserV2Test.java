package org.gearticks.opmodes.test.servos;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.configurations.VelocityConfiguration.MotorConstants;
import org.gearticks.opmodes.BaseOpMode;

@TeleOp
public class PresserV2Test extends BaseOpMode {
    private VelocityConfiguration configuration;

    protected void initialize() {
        this.configuration = new VelocityConfiguration(this.hardwareMap, true);
    }
    protected void loopAfterStart() {
        final double presserPosition;
        if (this.gamepads[0].getX()) presserPosition = MotorConstants.PRESSER_V2_LEFT;
        else if (this.gamepads[0].getB()) presserPosition = MotorConstants.PRESSER_V2_RIGHT;
        else presserPosition = MotorConstants.PRESSER_V2_NEUTRAL;
        this.configuration.beaconPresser.setPosition(presserPosition);
    }
}