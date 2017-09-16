//Utility math functions
package org.gearticks;

public class MathSupplement {
	//Returns the cosine of a degree angle
	public static double cosDegrees(double degrees) {
		return Math.cos(Math.toRadians(degrees));
	}
	//Returns the sine of a degree angle
	public static double sinDegrees(double degrees) {
		return Math.sin(Math.toRadians(degrees));
	}
	//Adjusts a value in one range to the other
	public static double rangeChange(double range1Low, double range1High, double value, double range2Low, double range2High) {
		return range2Low + (value - range1Low) / (range1High - range1Low) * (range2High - range2Low);
	}
	//Returns whether no two of the supplied values have differing signs (+ or - or 0)
	public static boolean sameSign(double... values) {
		if (values.length < 2) return true;
		else {
			final double sign = Math.signum(values[0]);
			for (int i = 1; i < values.length; i++) {
				if (Math.signum(values[i]) != sign) return false;
			}
			return true;
		}
	}
}