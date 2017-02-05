package org.gearticks.opencv.imageprocessors.beaconposition;


import android.util.Log;

import org.gearticks.opencv.vision.ImageProcessor;
import org.gearticks.opencv.vision.ImageProcessorResult;
import org.gearticks.opencv.vision.ImageUtil;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.opencv.core.Core.FONT_HERSHEY_SIMPLEX;

/**
 * Detects the positions of the largest red and blue contours in the image
 */
public class CenterOnBeaconProcessor implements ImageProcessor<BeaconPositionResult> {
    private static final String TAG = "gearticks_ImgProcessor";
    final static double MIN_AREA_FRACTION = 0.05; //between 0 and 1

    @Override
    public ImageProcessorResult<BeaconPositionResult> process(long startTime, Mat rgbaFrame, boolean saveImages) {
        Log.v(TAG, "Starting the CenterOnBeaconProcessor. SaveImages = "+saveImages);
        //save the image in the Pictures directory
        if (saveImages) {
            ImageUtil.saveImage(TAG, rgbaFrame, Imgproc.COLOR_RGBA2BGR, "0_camera", startTime);
        }
        //convert image to hsv
        Mat hsv = new Mat();
        Imgproc.cvtColor(rgbaFrame, hsv, Imgproc.COLOR_RGB2HSV);
        // rgbaFrame is untouched; hsv now contains the same image but using HSV colors

        //Apply inRange

        Mat dstBlue = new Mat();
        Mat dstRed = new Mat();
        Core.inRange(hsv, BeaconColor.BLUE.getHsvMin(),BeaconColor.BLUE.getHsvMax(),dstBlue);
        Core.inRange(hsv, BeaconColor.RED.getHsvMin(),BeaconColor.RED.getHsvMax(),dstRed);

        //Debug: write inRange images
        //Note: image type is now GRAY, not HSV
        if (saveImages) {
            ImageUtil.saveImage(TAG, dstBlue, Imgproc.COLOR_GRAY2BGR, "1_blue_inrange", startTime);
            ImageUtil.saveImage(TAG, dstRed, Imgproc.COLOR_GRAY2BGR, "1_red_inrange", startTime);
        }

        //Morph
        this.morphOps(dstBlue);
        this.morphOps(dstRed);

         //Debug: write morphed images
        if (saveImages) {
            ImageUtil.saveImage(TAG, dstBlue, Imgproc.COLOR_GRAY2BGR, "2_blue_morph", startTime);
            ImageUtil.saveImage(TAG, dstRed, Imgproc.COLOR_GRAY2BGR, "3_red_morph", startTime);
        }

        //Contours
        double minArea = this.getMinimumArea(rgbaFrame);
        List<ColorObject> blueObjects = this.findLargeColorObjects(dstBlue, minArea, BeaconColor.BLUE);
        List<ColorObject> redObjects = this.findLargeColorObjects(dstRed, minArea, BeaconColor.RED);

        if (saveImages) {
            //Only draw color objects if we're saving the image. Not necessary for 'production'.
            drawColorObjects(rgbaFrame, blueObjects, redObjects); //Note that this operation does overwrite the original image
            ImageUtil.saveImage(TAG, rgbaFrame, Imgproc.COLOR_RGBA2BGR, "3_final", startTime);
        }

        //Extract positions
        BeaconColor leftColor = BeaconColor.UNKNOWN;
        BeaconColor rightColor = BeaconColor.UNKNOWN;
        double leftColorX = -1;
        double rightColorX = -1;

        if (blueObjects.size() > 0 && redObjects.size() > 0 ){
            ColorObject largestBlue = blueObjects.get(0);
            ColorObject largestRed = redObjects.get(0);
            /*
            TODO: apply some threshold?
            I.e. is difference is too small, return UNKNOWN
             */
            if (largestBlue.getCenterPoint().x < largestRed.getCenterPoint().x){
                leftColor = BeaconColor.BLUE;
                rightColor = BeaconColor.RED;
                leftColorX = largestBlue.getCenterPoint().x;
                rightColorX = largestRed.getCenterPoint().x;
            }
            else {
                leftColor = BeaconColor.RED;
                rightColor = BeaconColor.BLUE;
                leftColorX = largestRed.getCenterPoint().x;
                rightColorX = largestBlue.getCenterPoint().x;
            }
        }

        //construct and return the result
        return new ImageProcessorResult<>(startTime, rgbaFrame,
                new BeaconPositionResult(leftColor, rightColor, leftColorX, rightColorX)
        );
    }

    /**
     * Seems to remove the small ares and produce a smoother consecutive area
     * @param image
     */
    protected void morphOps(Mat image){
		/*
		 * Create structuring element that will be used to "dilate" and "erode" image.
		 * The element chosen here is a 3px by 3px rectangle
		 */
        Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3));
//		Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(8,8));
        //dilate with larger element so make sure object is nicely visible
        Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(8,8));
        Imgproc.erode(image, image, erodeElement);
        Imgproc.erode(image, image, erodeElement);
//		Imgproc.erode(image, image, erodeElement);
//		Imgproc.erode(image, image, erodeElement);


        Imgproc.dilate(image, image, dilateElement);
        Imgproc.dilate(image, image, dilateElement);
    }




    /**
     * Detects contours based on a binary image. Returns ColorObjects whose contour area exceeds the minimum threshold.
     * Assumes m is a binary image.
     * @param image - a binary image
     * @param minArea - the minimum area of a large contour
     * @param beaconColor - color for the detected ColorObjects
     * @return
     */
    protected List<ColorObject> findLargeColorObjects(Mat image, double minArea, BeaconColor beaconColor){
        List<ColorObject> largeColorObjects = new ArrayList<>();

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();

		/*
		 * Note the use of the mode setting.
		 * See http://docs.opencv.org/3.1.0/d9/d8b/tutorial_py_contours_hierarchy.html
		 */
        Imgproc.findContours(image, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

        for (MatOfPoint contour : contours){
            ColorObject obj = new ColorObject(beaconColor, contour); //use 'moments'
            if (obj.getArea() > minArea){
                largeColorObjects.add(obj);
                System.out.println("Contour area = " + obj.getArea() + " x = " + obj.getCenterPoint().x + " y = "+ obj.getCenterPoint().y);
            }
        }

        //Sort the List by largest area size first, i.e. descending on area
        Collections.sort(largeColorObjects, Collections.reverseOrder());

        return largeColorObjects;
    }

    protected double getMinimumArea(Mat image){
        return (image.rows() * image.cols()) * MIN_AREA_FRACTION;
    }

    /**
     * Draws the contour and the contour center on the image.
     * @param rgbaFrame
     * @param blueObjects
     * @param redObjects
     */
    protected static void drawColorObjects(Mat rgbaFrame, List<ColorObject> blueObjects, List<ColorObject> redObjects){
        final int radius = 10;
        final int blueRadius = 30;
        final int redRadius = 10;

        Collection<ColorObject> objects = new ArrayList<>();
        objects.addAll(blueObjects);//normally only one object!
        objects.addAll(redObjects);//normally only one object!
        for (ColorObject co : objects){
            List<MatOfPoint> contours = new ArrayList<>();
            contours.add(co.getContour());

            //Draw a contour
            Imgproc.drawContours(rgbaFrame, contours, -1, co.beaconColor.getRgbColor());
            //Draw a circle in the center
            //Imgproc.circle(image, co.getCenterPoint(), radius, co.beaconColor.getBgrColor(), -1);
            Core.circle(rgbaFrame, co.getCenterPoint(), radius, co.beaconColor.getRgbColor(), -1);
            //Draw text
            Core.putText(rgbaFrame, co.beaconColor.name(),  co.getCenterPoint(), FONT_HERSHEY_SIMPLEX, 1, co.beaconColor.getRgbColor(), 4);

        //Draw rectange
            Point pt1 = co.getRectangle().br();
            Point pt2 = co.getRectangle().tl();
            //Imgproc.rectangle(image, pt1, pt2, co.beaconColor.getBgrColor());
            Core.rectangle(rgbaFrame, pt1, pt2, co.beaconColor.getRgbColor());
        }
    }

}
