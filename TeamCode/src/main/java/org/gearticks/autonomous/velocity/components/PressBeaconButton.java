package org.gearticks.autonomous.velocity.components;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;

import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.gearticks.Vuforia.VuforiaConfiguration;
import org.gearticks.autonomous.generic.component.AutonomousComponentVelocityBase;
import org.gearticks.hardware.configurations.VelocityConfiguration;
import org.gearticks.hardware.drive.DriveDirection;
import org.gearticks.joystickoptions.AllianceOption;
import org.gearticks.opmodes.autonomous.CantonBeaconAutonomous;
import org.gearticks.opmodes.units.SideOfButton;
import org.gearticks.opmodes.utility.Utils;

/**
 * recognise the color of the beacon
 * turn to press button
 * pres button
 * back out
 */
public class PressBeaconButton extends AutonomousComponentVelocityBase {
    private final DriveDirection direction = new DriveDirection();
    private final VuforiaConfiguration vuforiaConfiguration;
    private boolean allianceColorIsBlue;
    private final double angleMultiplier;
    private double buttonAngle;



    public PressBeaconButton(@NonNull VuforiaConfiguration vuforiaConfiguration, @NonNull VelocityConfiguration configuration, String id) {
        super(configuration, id);
        this.vuforiaConfiguration = Utils.assertNotNull(vuforiaConfiguration);

        this.allianceColorIsBlue = AllianceOption.allianceOption.getRawSelectedOption() == AllianceOption.BLUE;
        if (this.allianceColorIsBlue) angleMultiplier = 1.0; //angles were calculated for blue side
        else angleMultiplier = -1.0; //invert all angles for red side
    }

    @Override
    public void setup(int inputPort) {
        super.setup(inputPort);
        this.stage = Stage.values()[0];
    }

    @Override
    public int run() {
        int transition = super.run();

        transition = internalStateMachine();


        return transition;
    }

    @Override
    public void tearDown() {
        super.tearDown();
    }

    private Stage stage;
    private enum Stage {
        SELECT_SIDE,
        TURN_TO_PRESS_BUTTON,
        PUSH_BUTTON,
        FACE_SQUARE,
        BACK_UP,
        STOPPED
    }
    private int internalStateMachine(){
        int transition = 0;
        switch (this.stage){
            case SELECT_SIDE:
                SideOfButton sideOfButton = getButtonToPress(this.vuforiaConfiguration.getBeaconBlueSide());
                this.buttonAngle = 90.0 * angleMultiplier;
                switch (sideOfButton){
                    case LEFT:
                        Log.i(Utils.TAG, "Going left");
                        this.buttonAngle += 10.0;
                        this.nextStage();
                        break;
                    case RIGHT:
                        Log.i(Utils.TAG, "Going right");
                        this.buttonAngle -= 10.0;
                        this.nextStage();
                        break;
                    case UNKNOWN:
                        Log.i(Utils.TAG, "Button color could not be detected");
                        transition = 1;
                        break;
                }
                break;
            case TURN_TO_PRESS_BUTTON:
                Log.v(Utils.TAG, "Turing to button");
                if (this.direction.gyroCorrect(this.buttonAngle, 1.0, this.getConfiguration().imu.getRelativeYaw(), 0.08, 0.1) > 10) {
                    this.direction.stopDrive();
                    this.getConfiguration().resetEncoder();
                    this.nextStage();
                }
                break;
            case PUSH_BUTTON:
                Log.v(Utils.TAG, "Pressing to button");
                this.direction.drive(0.0, 0.3);
                this.direction.gyroCorrect(this.buttonAngle, 1.0, this.getConfiguration().imu.getRelativeYaw(), 0.05, 0.1);
                if (this.getConfiguration().encoderPositive() > 200) {
                    this.direction.stopDrive();
                    this.getConfiguration().resetEncoder();
                    this.nextStage();
                }
                break;
            case FACE_SQUARE:
                Log.v(Utils.TAG, "Squaring up");
                if (this.direction.gyroCorrect(90.0 * angleMultiplier, 1.0, this.getConfiguration().imu.getRelativeYaw(), 0.08, 0.1) > 10) {
                    this.direction.stopDrive();
                    this.nextStage();
                }
                break;
            case BACK_UP:
                Log.v(Utils.TAG, "Backing up");
                this.direction.drive(0.0, -0.5);
                this.direction.gyroCorrect(90.0 * angleMultiplier, 1.0, this.getConfiguration().imu.getRelativeYaw(), 0.05, 0.1);
                if (this.getConfiguration().encoderPositive() > 300) {
                    this.direction.stopDrive();
                    this.nextStage();
                }
                break;
            case STOPPED:
                this.getConfiguration().stopMotion();
                transition = 1;
        }
        if (this.stage != Stage.STOPPED) this.getConfiguration().move(this.direction, 0.06);

        return transition;
    }
    private SideOfButton getButtonToPress(SideOfButton sideOfBlue){
        if (this.allianceColorIsBlue){
            return sideOfBlue;
        }
        else {
            return sideOfBlue.getInverse();
        }
    }

    private void setStage(Stage newStage) {
        this.stage = newStage;
    }
    private void nextStage() {
        this.setStage(Stage.values()[this.stage.ordinal() + 1]);
        Log.i(Utils.TAG, "Next stage = " + this.stage);

    }

}
