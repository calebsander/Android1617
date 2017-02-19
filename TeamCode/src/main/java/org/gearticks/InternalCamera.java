//Manages the camera to simplify using it
package org.gearticks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("deprecation")
public class InternalCamera implements Camera.PreviewCallback {
	//Stores an image at a given time
	public class Image {
		private final Bitmap imageSnapshot; //guaranteed not to update when camera provides new data, unlike InternalCamera.image

		public Image(Bitmap imageSnapshot) {
			this.imageSnapshot = imageSnapshot;
		}

		//Gets the value of the pixel at the desired coordinates
		public Pixel getPixel(int x, int y) {
			return new Pixel(this.imageSnapshot.getPixel(x, y));
		}
		//Gets the raw Bitmap for use in other things
		public Bitmap getBitmap() {
			return this.imageSnapshot;
		}
		//Gets the width of the image
		public int getWidth() {
			return this.imageSnapshot.getWidth();
		}
		//Gets the height of the image
		public int getHeight() {
			return this.imageSnapshot.getHeight();
		}
	}
	//Stores the RGB values (0-255 each) of a single pixel
	public class Pixel {
		//The pixel's value (bytes are: ?RGB)
		private final int pixelValue;

		public Pixel(int pixelValue) {
			this.pixelValue = pixelValue;
		}
		public Pixel(int red, int green, int blue) {
			this(Color.rgb(red, green, blue));
		}

		//Get the value of a single color of the pixel
		public int getRed() {
			return Color.red(this.pixelValue);
		}
		public int getGreen() {
			return Color.green(this.pixelValue);
		}
		public int getBlue() {
			return Color.blue(this.pixelValue);
		}
	}

	//Default quality of the JPEG picture
	private static final int DEFAULT_JPEG_QUALITY = 100;
	//Returned image's width
	private static final int RESULTANT_WIDTH = 160;
	//Returned image's height
	private static final int RESULTANT_HEIGHT = 120;

	//The camera being used
	private Camera camera;
	//Used to capture the camera's preview
	@SuppressWarnings("FieldCanBeLocal")
	private SurfaceTexture surfaceTexture;
	//The last image taken
	private Bitmap image;

	public InternalCamera() {
		final int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			Camera.CameraInfo info = new Camera.CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
				this.camera = Camera.open(i);
				break;
			}
		}
		//Modify the dimensions of the captured image
		final Camera.Parameters parameters = this.camera.getParameters();
		final List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
		final Camera.Size smallestSize = sizes.get(sizes.size() - 1);
		parameters.setPreviewSize(smallestSize.width, smallestSize.height);
		parameters.setAutoExposureLock(true);
		parameters.setAutoWhiteBalanceLock(true);
		this.camera.setParameters(parameters);

		this.camera.setPreviewCallback(this); //ensures that each new picture can be processed
		this.surfaceTexture = new SurfaceTexture(10);
		try {
			this.camera.setPreviewTexture(this.surfaceTexture);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		this.camera.startPreview();
	}

	public void onPreviewFrame(byte[] data, Camera camera) {
		//Make image into JPEG
		Camera.Size size = camera.getParameters().getPreviewSize();
		YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		image.compressToJpeg(new Rect(0, 0, size.width, size.height), DEFAULT_JPEG_QUALITY, out);
		byte[] imageBytes = out.toByteArray();
		final Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
		final Bitmap scaledDown = Bitmap.createScaledBitmap(bitmap, RESULTANT_WIDTH, RESULTANT_HEIGHT, false);
		//Rotate image by 90 degrees (not sure why this is necessary)
		Matrix matrix = new Matrix();
		matrix.postRotate(180);
		this.image = Bitmap.createBitmap(scaledDown, 0, 0, RESULTANT_WIDTH, RESULTANT_HEIGHT, matrix, true);
	}
	//Returns whether or not an image has yet been received (to avoid getting NullPointerExceptions)
	public boolean hasImage() {
		return this.image != null;
	}
	//Get the full image for more complicated processing
	public Image getImage() {
		return new Image(this.image);
	}
	//Get the width of the image in pixels
	public int getImageWidth() {
		return this.image.getWidth();
	}
	//Get the height of the image in pixels
	public int getImageHeight() {
		return this.image.getHeight();
	}
	//Get a specified pixel in the image
	public Pixel getPixel(int x, int y) {
		return new Pixel(this.image.getPixel(x, y));
	}
	//Releases the camera for other use
	public void release() {
		this.camera.stopPreview();
		this.camera.setPreviewCallback(null);
		this.camera.release();
	}
}