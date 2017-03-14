package cn.feichao.app.scanner.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

import cn.feichao.app.scanner.Config;
import cn.feichao.app.scanner.ScannerPresenter;
import cn.feichao.app.scanner.Size;

/**
 * Created by feichao on 2017/3/8.
 *
 */
public class CameraManager implements ScannerPresenter.ICamera{
    private static final String TAG = "CameraManager";

    private static Camera mCamera;
    private Context mContext;

    public CameraManager(Context context) {
        mContext = context;
    }

    /** Check if this device has a camera */
    public boolean checkCameraHardware() {
        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            Log.w(TAG, "没有相机设备");
            return false;
        }
    }

    /**
     *
     * @return 相机实例
     */
    public Camera getCameraInstance() {
        if(!checkCameraHardware()) {
            mCamera = null;
            throw new RuntimeException("没有相机设备");
        }
        if(mCamera != null) {
            Log.i(TAG, "相机已经打开");
            return mCamera;
        }
        mCamera = getBackCameraInstance();
        if(mCamera == null) {
            throw new RuntimeException("获取相机设备失败");
        }
        initCamera(mCamera);
        return mCamera;
    }


    @Override
    public void openCamera(SurfaceHolder holder, Camera.PreviewCallback callBack) throws IOException {
        mCamera = getCameraInstance();
        updateCamera(holder, callBack);
    }

    public void updateCamera(SurfaceHolder holder, Camera.PreviewCallback callBack) throws IOException {
        if(mCamera != null) {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            mCamera.setPreviewCallback(callBack);
        }
    }

    @Override
    public void stopPreview() {
        if(mCamera != null) {
            mCamera.stopPreview();
        }
    }

    @Override
    public void releaseCamera() {
        if(mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    /** A safe way to get an instance of the Camera object. */
    private Camera getBackCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e){
            // Camera is not available (in use or does not exist)
            Log.e(TAG, "相机打开失败");
        }
        return c; // returns null if camera is unavailable
    }

    public void initCamera(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();// 获得相机参数
        parameters.setPictureFormat(PixelFormat.YCbCr_422_SP);

        Size size = Config.getSuitablePreviewSize(parameters.getSupportedPreviewSizes());
        parameters.setPreviewSize(size.getWidth(), size.getHeight());

        // TODO 设置聚焦

        camera.setParameters(parameters);// 设置相机参数

    }

    /**
     * preview 调整到和 屏幕尺寸接近的话就不需要再调整比例了
     */
//    public Size ajustPreviewSize(int width, int height) {
//        int newWidth = width;
//        int newHeight = height;
//
//        Camera.Parameters parameters = mCamera.getParameters();// 获得相机参数
//        Camera.Size s = parameters.getPictureSize();
//        double w = s.width;
//        double h = s.height;
//        if(width < height) {
//            newWidth = (int)(height*(w/h));
//        } else {
//            newHeight = (int)(width*(h/w));
//        }
//        return new Size(newWidth, newHeight);
//    }
}
