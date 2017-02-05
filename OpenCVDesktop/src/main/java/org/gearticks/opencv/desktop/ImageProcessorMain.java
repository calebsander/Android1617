package org.gearticks.opencv.desktop;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gearticks.opencv.imageprocessors.EvBeaconProcessor;
import org.gearticks.opencv.vision.BeaconColorResult;
import org.gearticks.opencv.vision.ImageProcessor;
import org.gearticks.opencv.vision.ImageProcessorResult;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.highgui.Highgui;
//import org.opencv.imgcodecs.Imgcodecs;

public class ImageProcessorMain {
	static String inputPath = new String("C:/temp/opencv/beacon/in_hardred/");
	static String outputPath = "C:/temp/opencv/beacon/out/";
	static String imageName1 = "2017-01-22 12.59.30";
	static String imageName2 = "2017-01-22 13.03.10";
	static String imageName = imageName1;
	static String inputExtension = "jpg";
	static String outputExtension = "bmp";//"png";
	
//	static ImageProcessor<BeaconColorResult> processor = new EvBeaconProcessor();
	
	Mat image = new Mat();

	public void readImage(){
		//this.image = Imgcodecs.imread(inputPath + imageName + "." + inputExtension);
		this.image = Highgui.imread(inputPath + imageName + "." + inputExtension);
	}
	
	public static Mat readImageFile(String dir, String imageName, String extension){
		//return Imgcodecs.imread(dir + imageName + "." + extension);
		return Highgui.imread(dir + imageName + "." + extension);
	}
	
	public static void writeImageFile(Mat image, String postfix){
		String fileName = outputPath + imageName + postfix + "." + outputExtension;
		System.out.println(String.format("Writing %s", fileName));
		//Imgcodecs.imwrite(fileName, image);
		Highgui.imwrite(fileName, image);
	}
	
	public static void main( String[] args ){
		System.out.println("Start detect color");
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		
		ImageProcessor<?> processor = new EvBeaconProcessor();
		
		runImageProcessorOnDirectory(processor, inputPath, outputPath);
		
		System.out.println("End detect color");
	}
	
	/**
	 * Loops over all .jpg files in inputPath and runs DetectBeaconColorPosition
	 * @param inputPath
	 * @param outputPath
	 */
	public static void runImageProcessorOnDirectory(ImageProcessor<?> processor, String inputPath, String outputPath){
		for (File file : getImageFilesInPath(inputPath, "jpg")){
			final String fileName = file.getName();
			final String imageName = fileName.substring(0, fileName.length() - 4);
			DebugImage di = new DebugImage(inputPath, outputPath, imageName);
			ImageProcessorResult<?> result = processor.process(System.currentTimeMillis(), di.originalImage, true);
			di.writeImageFile(result.getFrame(), "_final");
		}
		
	}
	
	public static Collection<File> getImageFilesInPath(String inputPath, String extension){
		Collection<File> files = new ArrayList<>();
		File dir = new File(inputPath);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				if (getFileExtension(child).equals(extension)){
					files.add(child);
				}
			}
		} else {
			// Handle the case where dir is not really a directory.
			// Checking dir.isDirectory() above would not be sufficient
			// to avoid race conditions with another process that deletes
			// directories.
		}
		return files;
	}
	
	public static String getFileExtension(File file) {
	    String name = file.getName();
	    try {
	        return name.substring(name.lastIndexOf(".") + 1);
	    } catch (Exception e) {
	        return "";
	    }
	}
	

}
