package org.gearticks.opencv.imageprocessors.evbeacon;


import org.gearticks.opencv.vision.ImageProcessor;
import org.gearticks.opencv.vision.ImageProcessorResult;
import org.opencv.core.Mat;

/**
 *
 * FTC Team EV 7393
 */
public class BeaconProcessorTemplate implements ImageProcessor<BeaconColorResult> {
    
    @Override
    public ImageProcessorResult<BeaconColorResult> process(long startTime, Mat rgbaFrame, boolean saveImages) {
        
        //<The code we write will go here>
        return null;
    }
}
