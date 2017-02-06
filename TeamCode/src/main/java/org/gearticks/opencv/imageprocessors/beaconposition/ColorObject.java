package org.gearticks.opencv.imageprocessors.beaconposition;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

/**
 * Represents a color in an image.
 * Based an the contour.
 * Uses Moments to compute area, x, y, etc.
 * @author vterpstra
 *
 */
public class ColorObject implements Comparable<ColorObject> {
	public final BeaconColor beaconColor;
	private final MatOfPoint contour;
	private final Moments moments;
	
	private double area;
	private double centerX;
	private double centerY;
	private Rect rectangle;
	

	public ColorObject(BeaconColor beaconColor, MatOfPoint contour) {
		super();
		this.beaconColor = beaconColor;
		this.contour = contour;
		this.moments = Imgproc.moments(contour, true);
		this.computeImageProperties();
	}
	
	public void computeImageProperties(){
		this.area = moments.get_m00();
		this.centerX = moments.get_m10() / area;
		this.centerY = moments.get_m01() / area;
		this.rectangle = Imgproc.boundingRect(this.contour);
	}
	
	public double getArea(){
		return this.area;
	}
	
	public Point getCenterPoint(){
		return new Point(this.centerX, this.centerY);
	}

	public MatOfPoint getContour() {
		return contour;
	}

	public Moments getMoments() {
		return moments;
	}
	
	public Rect getRectangle(){
		return this.rectangle;
	}

    @Override
    public int compareTo(ColorObject another) {
        return Double.compare(this.getArea(), another.getArea());
    }
}
