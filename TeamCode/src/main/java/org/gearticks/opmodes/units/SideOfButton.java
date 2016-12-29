package org.gearticks.opmodes.units;

import org.gearticks.Vuforia.VuforiaConfiguration;

/**
 * Represents the side of a button on a beacon
 * Includes unknown to represent an unknown button location
 */

public enum SideOfButton{
    LEFT,
    RIGHT,
    UNKNOWN;

    /**
     * Returns the inverse of left and right
     * Keeps unknown the same
     * @return
     */
    public SideOfButton getInverse(){
        switch (this){
            case LEFT:
                return SideOfButton.RIGHT;
            case RIGHT:
                return SideOfButton.LEFT;
            case UNKNOWN:
                return SideOfButton.UNKNOWN;
        }
        return SideOfButton.UNKNOWN;
    }
}
