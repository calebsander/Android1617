package org.gearticks.vuforia;

public abstract class VuforiaImages {
	public static int getID(String imageName) {
		switch (imageName) {
			case "Wheels": return 0;
			case "Tools": return 1;
			case "Legos": return 2;
			case "Gears": return 3;
			default: throw new RuntimeException("Invalid image name: " + imageName);
		}
	}
	public static String getImageName(boolean allianceColorIsBlue, boolean isNearBeacon) {
		if (allianceColorIsBlue) {
			if (isNearBeacon) return "Wheels";
			else return "Legos";
		}
		else {
			if (isNearBeacon) return "Gears";
			else return "Tools";
		}
	}
}