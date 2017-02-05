package org.gearticks.opencv.desktop;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.gearticks.opencv.imageprocessors.beaconposition.CenterOnBeaconProcessor;
import org.gearticks.opencv.imageprocessors.evbeacon.EvBeaconProcessor;
import org.gearticks.opencv.vision.ImageProcessor;
import org.gearticks.opencv.vision.ImageProcessorResult;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
//import org.opencv.imgcodecs.Imgcodecs;

public class ImageProcessorMain {
    static String inputPath = new String("C:/temp/opencv/beacon/in_hardred/");
    static String outputPath = "C:/temp/opencv/beacon/out/";
    static String inputExtension = "jpg";
    static String outputExtension = "bmp";//"png";


    public static void main( String[] args ){
        System.out.println("Start detect color");
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

        //ImageProcessor<?> processor = new EvBeaconProcessor();
        ImageProcessor<?> processor = new CenterOnBeaconProcessor();

        runImageProcessorOnDirectory(processor, inputPath, outputPath);

        System.out.println("End detect color");
    }

    /**
     * Loops over all .jpg files in inputPath and runs them though the ImageProcessor
     * @param inputPath
     * @param outputPath
     */
    public static void runImageProcessorOnDirectory(ImageProcessor<?> processor, String inputPath, String outputPath){
        for (File file : getImageFilesInPath(inputPath, "jpg")){
            final String fileName = file.getName();
            final String imageName = fileName.substring(0, fileName.length() - 4);
            //DebugImage di = new DebugImage(inputPath, outputPath, imageName);

            /*
            The jpg image is a BGR image, as where the image from the Android camera is RGB.
            Thus convert to RGB to match Android camera.
             */
            Mat bgrFrame = readImageFile(inputPath, imageName, inputExtension);
            Mat rgbFrame = new Mat();
            Imgproc.cvtColor(bgrFrame, rgbFrame, Imgproc.COLOR_BGR2RGB);
            //rgbFrame = di.originalImage;
            ImageProcessorResult<?> result = processor.process(System.currentTimeMillis(), rgbFrame, true);
            //di.writeImageFile(result.getFrame(), "_final");
            System.out.println(result.toString());

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

    public static Mat readImageFile(String inputPath, String imageName, String inputExtension){
        //return Imgcodecs.imread(inputPath + imageName + "." + inputExtension);
        return Highgui.imread(inputPath + imageName + "." + inputExtension);
    }
}
