package org.gearticks.opencv.desktop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.highgui.Highgui;
//import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * Allows for outputting files during and after the image processing to see intermediate images as files
 * @author vterpstra
 *
 */
public class DebugImage {
	final String inputPath;
	final String outputPath;
	final String imageName;
	final String inputExtension;// = "jpg";
	final String outputExtension; // = "bmp";//"png";
	
	public Mat originalImage;

	/**
	 * Note that the path strings should end with the OS delimiter (e.g. "\")<br/>
	 * Uses "jpg" for input extension and "bmp" for output extension
	 * @param inputPath
	 * @param outputPath
	 * @param imageName
	 */
	public DebugImage(String inputPath, String outputPath, String imageName) {
		this(inputPath, outputPath, imageName, "jpg", "bmp");
	}
	

	public DebugImage(String inputPath, String outputPath, String imageName, String inputExtension,
			String outputExtension) {
		this.inputPath = inputPath;
		this.outputPath = outputPath;
		this.imageName = imageName;
		this.inputExtension = inputExtension;
		this.outputExtension = outputExtension;
		this.originalImage = this.readImageFile(imageName);
	}



	/**
	 * Reads an image from the inputPath and with the extension of this DebugImage
	 * @param imageName
	 * @return
	 */
	public Mat readImageFile(String imageName){
		//return Imgcodecs.imread(inputPath + imageName + "." + inputExtension);
		return Highgui.imread(inputPath + imageName + "." + inputExtension);
	}
	
	/**
	 * Writes the image to a file using the outputPath and outputExtension of this DebugImage
	 * @param image
	 * @param postfix - postfix appended to the filename
	 */
	public void writeImageFile(Mat image, String postfix){
		String fileName = outputPath + imageName + postfix + "." + outputExtension;
		System.out.println(String.format("Writing %s", fileName));
		//Imgcodecs.imwrite(fileName, image);
		Highgui.imwrite(fileName, image);
	}
	
//	/**
//	 * Draws the contour and the contour center on the image.
//	 * @param image
//	 * @param blueObjects
//	 * @param redObjects
//	 */
//	protected static void drawColorObjects(Mat image, List<ColorObject> blueObjects, List<ColorObject> redObjects){
//		final int radius = 10;
//		
//		Collection<ColorObject> objects = new ArrayList<>();
//		objects.addAll(blueObjects);//normally only one object!
//		objects.addAll(redObjects);//normally only one object!
//		for (ColorObject co : objects){
//			List<MatOfPoint> contours = new ArrayList<>();
//			contours.add(co.getContour());
//			
//			//Draw a contour
//			Imgproc.drawContours(image, contours, -1, co.beaconColor.getBgrColor());
//			//Draw a circle in the center
//			//Imgproc.circle(image, co.getCenterPoint(), radius, co.beaconColor.getBgrColor(), -1);
//			Core.circle(image, co.getCenterPoint(), radius, co.beaconColor.getBgrColor(), -1);
//			//Draw rectange
//			Point pt1 = co.getRectangle().br();
//			Point pt2 = co.getRectangle().tl();
//			//Imgproc.rectangle(image, pt1, pt2, co.beaconColor.getBgrColor());
//			Core.rectangle(image, pt1, pt2, co.beaconColor.getBgrColor());
//		}
//	}
//
//	public void writeFinalImageDetectionResult(List<ColorObject> blueObjects, List<ColorObject> redObjects) {
//		drawColorObjects(this.originalImage, blueObjects, redObjects); //Note that this operation does overwrite the original image
//		this.writeImageFile(this.originalImage, "_____final");
//	}

}
