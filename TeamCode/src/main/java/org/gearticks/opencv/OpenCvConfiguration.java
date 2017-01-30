package org.gearticks.opencv;

import android.content.Context;
import android.util.Log;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.R;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.gearticks.opmodes.utility.Utils.TAG;

/**
 * Created by vterpstra on 1/29/2017.
 */

public class OpenCvConfiguration implements CameraBridgeViewBase.CvCameraViewListener2 {
    private FtcRobotControllerActivity activity;
    private CameraBridgeViewBase cameraView;
    private Mat image;

    private static final boolean cameraEnabled = false; //Camera is not working yet!

    public OpenCvConfiguration(HardwareMap hardwareMap){
        this.activity = (FtcRobotControllerActivity)hardwareMap.appContext;

//        cameraView = (CameraBridgeViewBase)activity.findViewById(R.id.ImageView01);
        cameraView = (JavaCameraView) activity.findViewById(R.id.show_camera_activity_java_surface_view);
        cameraView.setCameraIndex(1);   //use front camera
        if (cameraEnabled)
        {
            cameraView.setCvCameraViewListener(this);
        }
    }

    /**
     * Implements the OpenCV manager loader callback methods.
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
                            Log.i(TAG, "OpenCV Init succes");
                            cameraView.enableView();
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

    /**
     * This method is called on every captured camera frame. It will do face detection on the
     * captured frame.
     *
     * @param inputFrame specifies the captured frame object.
     */
    @Override //required by CameraBridgeViewBase.CvCameraViewListener2
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame){
        image = inputFrame.rgba();

        //DO stuff ????
        Log.d(TAG, "OpenCV onCameraFrame");
        Mat m = new Mat();

        return image;
    }

    /**
     * This method is called when the camera view is started. It will allocate and initialize
     * some global resources.
     *
     * @param width specifies the width of the camera view.
     * @param height specifies the height of the camera view.
     */
    @Override //required by CameraBridgeViewBase.CvCameraViewListener2
    public void onCameraViewStarted(int width, int height) {
        //Do something?
    }

    /**
     * This method is called when the camera view is stopped. It will clean up the allocated
     * global resources.
     */
    @Override //required by CameraBridgeViewBase.CvCameraViewListener2
    public void onCameraViewStopped() {
        if (image != null) image.release();
    }

    //-------------------------------------------------------------------------------------------

    public void activate() {
        this.startCamera();
    }

    public void deactivate(){ this.stopCamera(); }


    /**
     * Do this at OpMode initialization?
     * Or each time when a AutComponents wants to use OpenCV?
     */
    private void startCamera() {
        if (cameraEnabled)
        {
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
    }   //startCamera

    private void stopCamera() {

        if (cameraEnabled)
        {
            cameraView.disableView();
            Log.i(TAG, "Stopping camera!");
        }
    }

}
