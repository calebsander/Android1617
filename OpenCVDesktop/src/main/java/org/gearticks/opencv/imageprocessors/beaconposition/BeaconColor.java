package org.gearticks.opencv.imageprocessors.beaconposition;

import org.opencv.core.Scalar;

public enum BeaconColor {
	/*
	 * Note that it seems the H value (normally between 0-360) is divided by 2 to fit the 255 range
	 * The values for S and V are scaled from100 to 255
	 * See http://docs.opencv.org/2.4/modules/imgproc/doc/miscellaneous_transformations.html#cvtcolor
	 */
    //	BLUE (new Scalar(92,0,0), new Scalar(124,256,256), new Scalar(255,0,0)),
    RED (new Scalar(160,0,100), new Scalar(180,256,256), new Scalar(0,0,255), new Scalar(255,0,0)),
    //	RED (new Scalar(160,120,100), new Scalar(180,256,256), new Scalar(0,0,255)),
    BLUE (new Scalar(92,0,0), new Scalar(124,256,256), new Scalar(255,0,0), new Scalar(0,0,255)),
//	RED (new Scalar(170,80,100), new Scalar(180,200,255), new Scalar(0,0,255)),
    UNKNOWN (new Scalar(0,0,0), new Scalar(0,0,0), new Scalar(0,0,0), new Scalar(0,0,0))
    ;
	
	private final Scalar hsvMin;
	private final Scalar hsvMax;
	private final Scalar bgrColor; //blue = Scalar(255,0,0), red = Scalar(0,0,255)
    private final Scalar rgbColor; //blue = Scalar(0,0,255), red = Scalar(255,0,0)

    private BeaconColor(Scalar hsvMin, Scalar hsvMax, Scalar bgrColor, Scalar rgbColor) {
		this.hsvMin = hsvMin;
		this.hsvMax = hsvMax;
		this.bgrColor = bgrColor;
        this.rgbColor = rgbColor;
	}

	public Scalar getHsvMin() {
		return hsvMin;
	}

	public Scalar getHsvMax() {
		return hsvMax;
	}

	public Scalar getBgrColor() {
		return bgrColor;
	}

    public Scalar getRgbColor() {
        return rgbColor;
    }
	
	
	/*
	 * From https://github.com/akaifi/MultiObjectTrackingBasedOnColor
	 * 
	setType(name);
	
	if(name=="blue"){

		//TODO: use "calibration mode" to find HSV min
		//and HSV max values

		setHSVmin(Scalar(92,0,0));
		setHSVmax(Scalar(124,256,256));

		//BGR value for Blue:
		setColor(Scalar(255,0,0));

	}
	if(name=="green"){

		//TODO: use "calibration mode" to find HSV min
		//and HSV max values

		setHSVmin(Scalar(34,50,50));
		setHSVmax(Scalar(80,220,200));

		//BGR value for Green:
		setColor(Scalar(0,255,0));

	}
	if(name=="yellow"){

		//TODO: use "calibration mode" to find HSV min
		//and HSV max values

		setHSVmin(Scalar(20,124,123));
		setHSVmax(Scalar(30,256,256));

		//BGR value for Yellow:
		setColor(Scalar(0,255,255));

	}
	if(name=="red"){

		//TODO: use "calibration mode" to find HSV min
		//and HSV max values

		setHSVmin(Scalar(0,200,0));
		setHSVmax(Scalar(19,255,255));

		//BGR value for Red:
		setColor(Scalar(0,0,255));

	}
	 */
	
	

}
