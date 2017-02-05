package android.os;

import java.io.File;

public class Environment {
	
	public static java.io.File getExternalStoragePublicDirectory(String type){
		File path = new File(type);
		return path;
	}
	
	public static final String DIRECTORY_PICTURES = "C:/temp/opencv/out/";

}
