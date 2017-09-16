package org.gearticks.opmodes.teleop.components;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.gearticks.GamepadWrapper;
import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent.NoTransition;
import org.gearticks.components.generic.component.OpModeComponentHardware;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.hardware.drive.MotorWrapper;
import org.gearticks.opmodes.teleop.VelocityDrive;

public class TeleopDrive extends OpModeComponentHardware<VelocityConfiguration, NoTransition> {
    private final GamepadWrapper[] gamepads;
    private final DriveDirection direction;
    private final Telemetry telemetry;

    public TeleopDrive(OpModeContext<VelocityConfiguration> opModeContext) {
        super(opModeContext, NoTransition.class);
        this.gamepads = opModeContext.gamepads;
        this.direction = new DriveDirection();
        this.telemetry = opModeContext.telemetry;
    }

    public NoTransition run() {
        final int driveGamepad;
        double yScaleFactor;
        double sScaleFactor;
        if (this.gamepads[VelocityDrive.CALVIN].leftStickAtRest() && this.gamepads[VelocityDrive.CALVIN].rightStickAtRest()) {
            driveGamepad = VelocityDrive.JACK;
            yScaleFactor = 0.8;
            sScaleFactor = Math.max(0.4, Math.abs(this.gamepads[driveGamepad].getLeftY() * yScaleFactor)); //if just turning, turn slower for greater accuracy
        }
        else {
            driveGamepad = VelocityDrive.CALVIN;
            yScaleFactor = 1.0;
            sScaleFactor = Math.max(0.5, Math.abs(this.gamepads[driveGamepad].getLeftY() * yScaleFactor)); //if just turning, turn slower for greater accuracy
        }

        final boolean slowMode = this.gamepads[VelocityDrive.CALVIN].getLeftBumper();
        final double maxPower;
        if (slowMode) {
            maxPower = 0.4;
            sScaleFactor = Math.max(0.15, Math.abs(this.gamepads[driveGamepad].getLeftY() * maxPower));
        }
        else maxPower = 1.0;
        final double accelLimit;
        if (slowMode) accelLimit = 0.075;
        else accelLimit = MotorWrapper.NO_ACCEL_LIMIT;

        final double maxSlope = 50.0; //defines range at which robot will go straight forward/back or will turn in place
        final double leftX = this.gamepads[driveGamepad].getLeftX(), leftY = this.gamepads[driveGamepad].getLeftY();
        if (leftX == 0 || Math.abs(leftY / leftX) > maxSlope) {
            this.direction.drive(0.0, scaleStick(leftY) * yScaleFactor);
            this.direction.turn(scaleStick(this.gamepads[driveGamepad].getRightX()) * sScaleFactor);
        } else if (leftY == 0 || Math.abs(leftY / leftX) < 1.0 / maxSlope) {
            this.direction.turn(scaleStick(leftX) * sScaleFactor);
        } else {
            this.direction.turn(Math.signum(leftY) * leftX / 2.0);
            this.direction.drive(0.0, leftY + Math.signum(leftY) * Math.abs(leftX) / 2.0);
        }

        this.configuration.drive.calculatePowers(this.direction);
        this.configuration.drive.scaleMotorsDown(maxPower);
        this.configuration.drive.accelLimit(accelLimit);
        this.configuration.drive.commitPowers();

        this.telemetry.addData("Controller", driveGamepad);
        this.telemetry.addData("Shooter down", this.configuration.isShooterDown());

        return null;
    }

    private static double scaleStick(double stick) {
        return stick * stick * stick;
    }
}
