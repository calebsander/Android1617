package android.util;

public class Log {
	public static void v(String tag, String msg){
		System.out.println("Verbose: " + tag + " - " + msg);
	}

	public static void d(String tag, String msg){
		System.out.println("Debug: " + tag + " - " + msg);
	}
	
	public static void i(String tag, String msg){
		System.out.println("Info: " + tag + " - " + msg);
	}
	public static void w(String tag, String msg){
		System.out.println("Warning: " + tag + " - " + msg);
	}
	
	public static void e(String tag, String msg){
		System.out.println("Error: " + tag + " - " + msg);
	}
	
}
