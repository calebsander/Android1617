package org.gearticks.opencv;

import android.util.Log;
import android.view.SurfaceView;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.R;
import org.gearticks.opencv.vision.FrameGrabber;
import org.gearticks.opencv.imageprocessors.EvBeaconProcessor;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import static org.gearticks.opmodes.utility.Utils.TAG;

/**
 * Created by vterpstra on 1/29/2017.
 */

public class OpenCvConfiguration {
    private FtcRobotControllerActivity activity;
    private CameraBridgeViewBase cameraBridgeViewBase;
    static final int FRAME_WIDTH_REQUEST = 176;
    static final int FRAME_HEIGHT_REQUEST = 144;

    private Mat image;

    private static final boolean cameraEnabled = false; //Camera is not working yet!

    //manages getting one frame at a time
    public static FrameGrabber frameGrabber = null;

    public OpenCvConfiguration(HardwareMap hardwareMap){
        this.activity = (FtcRobotControllerActivity)hardwareMap.appContext;



//        cameraBridgeViewBase = (CameraBridgeViewBase)activity.findViewById(R.id.ImageView01);
        cameraBridgeViewBase = (JavaCameraView) activity.findViewById(R.id.show_camera_activity_java_surface_view);
        cameraBridgeViewBase.setCameraIndex(1);   //use front camera
        this.initializeCameraBridgeViewBase(cameraBridgeViewBase, FRAME_WIDTH_REQUEST, FRAME_HEIGHT_REQUEST);
        Log.d(TAG, "Found cameraBridgeViewBase" + cameraBridgeViewBase.toString());

        frameGrabber = new FrameGrabber(cameraBridgeViewBase, FRAME_WIDTH_REQUEST, FRAME_HEIGHT_REQUEST);
        frameGrabber.setImageProcessor(new EvBeaconProcessor());
        frameGrabber.setSaveImages(true);

        this.initOpenCv(); //does this needs to be done at least after the cameraBridgeViewBase has a non-null value
    }

    private void initializeCameraBridgeViewBase(final CameraBridgeViewBase camera, final int frameWidthRequest, final int frameHeightRequest){
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //stuff that updates ui
                camera.setVisibility(SurfaceView.VISIBLE);
                camera.setMinimumWidth(frameWidthRequest);
                camera.setMinimumHeight(frameHeightRequest);
                camera.setMaxFrameSize(frameWidthRequest, frameHeightRequest);

            }
        });
    }

    /**
     * Initializes OpenCV.
     * Based on @see <a href="http://docs.opencv.org/2.4/doc/tutorials/introduction/android_binary_package/dev_with_OCV_on_Android.html"</a>
     * Once successful, calls the loaderCallback, which will enable the camera, which will enable the camera listener
     */
    private void initOpenCv(){
        if (!OpenCVLoader.initDebug())
        {
            Log.i(TAG, "Internal OpenCV library not found, using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, activity, loaderCallback);
        }
        else
        {
            Log.i(TAG, "OpenCV library found inside package, use it!");
            loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    /**
     * Implements the OpenCV manager loader callback methods.
     * Is called when OpenCV async is ready (?)
     * If so, enables the cameraBridgeViewBase, which in turn will start the listener (?)
     */
    private BaseLoaderCallback loaderCallback =
            new BaseLoaderCallback(activity) {
                /**
                 * This method is called when the OpenCV manager is connected. It loads the
                 * OpenCV library and the appropriate CascadeClassifier.
                 *
                 * @param status specifies the OpenCV connection status.
                 */
                @Override
                public void onManagerConnected(int status) {
                    final String funcName = "onManagerConnected";

                    switch (status) {
                        case LoaderCallbackInterface.SUCCESS:
                            Log.i(TAG, "OpenCV Init success");
                            cameraBridgeViewBase.enableView();
                            break;
                        case LoaderCallbackInterface.INIT_FAILED:
                            Log.i(TAG, "OpenCV Init Failed");
                            break;
                        case LoaderCallbackInterface.INSTALL_CANCELED:
                            Log.i(TAG, "OpenCV Install Cancelled");
                            break;
                        case LoaderCallbackInterface.INCOMPATIBLE_MANAGER_VERSION:
                            Log.i(TAG, "OpenCV Incompatible Version");
                            break;
                        case LoaderCallbackInterface.MARKET_ERROR:
                            Log.i(TAG, "OpenCV Market Error");
                            break;
                        default:
                            Log.i(TAG, "OpenCV Manager Install");
                            super.onManagerConnected(status);
                            break;
                    }
                }   //onManagerConnected
            };



    //-------------------------------------------------------------------------------------------

    public void activate() {
//        this.startCamera();
    }

    public void deactivate(){
//        this.stopCamera();
    }


//    /**
//     * Do this at OpMode initialization?
//     * Or each time when a AutComponents wants to use OpenCV?
//     */
//    private void startCamera() {
//        if (cameraEnabled)
//        {
//            if (!OpenCVLoader.initDebug())
//            {
//                Log.i(TAG, "Internal OpenCV library not found, using OpenCV Manager for initialization");
//                OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, activity, loaderCallback);
//            }
//            else
//            {
//                Log.i(TAG, "OpenCV library found inside package, use it!");
//                loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
//            }
//        }
//    }   //startCamera
//
//
//
//    private void stopCamera() {
//
//        if (cameraEnabled)
//        {
//            cameraBridgeViewBase.disableView();
//            Log.i(TAG, "Stopping camera!");
//        }
//    }

}
