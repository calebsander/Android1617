package org.gearticks.opencv.imageprocessors.beaconposition;


import org.gearticks.opencv.vision.ImageUtil;
import org.opencv.core.Scalar;

/**
 * Storage class for the position and color of the beacon
 * This file was made by the electronVolts, FTC team 7393
 * Date Created: 8/17/16.
 */
public class BeaconPositionResult {

    private final BeaconColor leftColor, rightColor;
    private final double leftColorX, rightColorX;

    public BeaconPositionResult() {
        this.leftColor = BeaconColor.UNKNOWN;
        this.rightColor = BeaconColor.UNKNOWN;
        this.leftColorX = -1;
        this.rightColorX = -1;
    }

    public BeaconPositionResult(BeaconColor leftColor, BeaconColor rightColor, double leftColorX, double rightColorX) {
        this.leftColor = leftColor;
        this.rightColor = rightColor;
        this.leftColorX = leftColorX;
        this.rightColorX = rightColorX;
    }

    public String toString(){
        return leftColor + " at "+ leftColorX +", " + rightColor + " at "+ rightColorX;
    }

    public BeaconColor getLeftColor() {
        return leftColor;
    }

    public BeaconColor getRightColor() {
        return rightColor;
    }


}
