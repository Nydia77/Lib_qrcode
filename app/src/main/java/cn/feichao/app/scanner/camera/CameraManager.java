package cn.feichao.app.scanner.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

import cn.feichao.app.scanner.ScannerPresenter;
import cn.feichao.app.scanner.Size;
import cn.feichao.app.utils.ImageUtil;
import cn.feichao.app.zxing.Decoder;

/**
 * Created by feichao on 2017/3/8.
 *
 */
public class CameraManager implements ScannerPresenter.ICamera{
    private static final String TAG = "CameraUtil";

    private static Camera mCamera;

    /** Check if this device has a camera */
    public boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
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
     * @param context 上下文
     * @param width  相机的横向像素  -1 使用默认大小
     * @param height 相机的纵向像素  -1 使用默认大小
     * @return 相机实例
     */
    public Camera getCameraInstance(Context context, int width, int height, Rect rect, Handler handler) {
        if(!checkCameraHardware(context)) {
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
        initCamera(width, height, mCamera, rect, handler);
        return mCamera;
    }

    public Camera getCameraInstance(Context context, Rect rect, Handler handler) {
        return getCameraInstance(context, -1, -1, rect, handler);
    }

    @Override
    public void openCamera(Context context, SurfaceHolder holder, Rect rect, Handler handler) throws IOException {
        mCamera = getCameraInstance(context, rect, handler);
        updateCamera(holder, rect, handler);
    }

    public void updateCamera(SurfaceHolder holder, final Rect rect, final Handler handler) throws IOException {
        if(mCamera != null) {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        }
        mCamera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                Log.i(TAG, "preview frame");
                // TODO AsyncTask
                Decoder decoder = new Decoder();
                Camera.Size s = camera.getParameters().getPreviewSize();
                // TODO 数据 图片数据的宽高
                String qrcode = decoder.decodeCameraCatchImage(data, s.width, s.height, rect, null);
                if (qrcode != null) {
                    camera.stopPreview();
                    Log.i(TAG, "scanned");
                    // TODO 图片转换
                    Bitmap bitmap = ImageUtil.fromYUV(data, s.width, s.height, rect);
                    if(bitmap == null) {
                        Log.i(TAG, "bitman == null");
                    }
                    Log.i(TAG, "qrcode" + qrcode);
                    Message msg = new Message();
                    msg.obj = qrcode;
                    handler.sendMessage(msg);
                }
            }
        });
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

    public void initCamera(int width, int height, Camera camera, final Rect rect, final Handler handler) {
        Camera.Parameters parameters = camera.getParameters();// 获得相机参数
        if((-1 != width) && (-1 != height)) {
            parameters.setPreviewSize(width, height); // 设置预览图像大小
        }
        parameters.setPictureFormat(PixelFormat.YCbCr_422_SP); // TODO 设置照片格式  默认是jpeg
//        parameters.setPictureFormat(ImageFormat.JPEG);
        camera.setParameters(parameters);// 设置相机参数

    }

    public Size ajustPreviewSize(int width, int height) {
        int newWidth = width;
        int newHeight = height;

        Camera.Parameters parameters = mCamera.getParameters();// 获得相机参数
        Camera.Size s = parameters.getPictureSize();
        double w = s.width;
        double h = s.height;
        if(width < height) {
            newWidth = (int)(height*(w/h));
        } else {
            newHeight = (int)(width*(h/w));
        }
        return new Size(newWidth, newHeight);
    }
}
