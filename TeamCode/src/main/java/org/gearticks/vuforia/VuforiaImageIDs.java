package org.gearticks.vuforia;

public abstract class VuforiaImageIDs {
	public static int getID(String imageName) {
		switch (imageName) {
			case "Wheels": return 0;
			case "Tools": return 1;
			case "Legos": return 2;
			case "Gears": return 3;
			default: throw new RuntimeException("Invalid image name: " + imageName);
		}
	}
}