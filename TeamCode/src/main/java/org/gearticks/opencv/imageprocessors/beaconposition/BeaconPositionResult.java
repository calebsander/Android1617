package org.gearticks.opencv.imageprocessors.beaconposition;


/**
 * Storage class for the position and color of the beacon
 * This file was made by the electronVolts, FTC team 7393
 * Date Created: 8/17/16.
 */
public class BeaconPositionResult {

    private final BeaconColor leftColor, rightColor;
    private final double leftColorX, rightColorX, imageXSize;

    public BeaconPositionResult() {
        this.leftColor = BeaconColor.UNKNOWN;
        this.rightColor = BeaconColor.UNKNOWN;
        this.leftColorX = -1;
        this.rightColorX = -1;
        this.imageXSize = 0;
    }

    public BeaconPositionResult(BeaconColor leftColor, BeaconColor rightColor, double leftColorX, double rightColorX, double imageXSize) {
        this.leftColor = leftColor;
        this.rightColor = rightColor;
        this.leftColorX = leftColorX;
        this.rightColorX = rightColorX;
        this.imageXSize = imageXSize;
    }

    public String toString(){
        return "Left: " + leftColor + " at "+ leftColorX +", Right: " + rightColor + " at "+ rightColorX + " => offset = "+ this.getCenterOffset();
    }

    public BeaconColor getLeftColor() {
        return leftColor;
    }

    public BeaconColor getRightColor() {
        return rightColor;
    }

    /**
     * Fraction of the image size where the center of mass is for the combined red and blue contours.
     * Negative is to the left
     * Between 0 - 1
     * @return
     */
    public double getCenterOffset(){
        double centerOfMass = (this.leftColorX + this.rightColorX) / 2;
        double offCenter = (this.imageXSize / 2) - centerOfMass;
        double centerOffsetFraction = offCenter / this.imageXSize;
        return centerOffsetFraction;
    }



}
