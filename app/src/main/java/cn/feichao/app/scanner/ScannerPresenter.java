package cn.feichao.app.scanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import com.google.zxing.ResultPointCallback;

import java.io.IOException;

import cn.feichao.app.scanner.camera.CameraManager;
import cn.feichao.app.zxing.Decoder;

/**
 * Created by feichao on 2017/3/11.
 *
 */
public class ScannerPresenter {

//    private ScannerActivity mActivity;
    private onScannedListener mListener;
    private ScannerManager mScannerManager;
    private CameraManager mCameraManager;

    private Camera.PreviewCallback mCallback;

    public ScannerPresenter(Context context, final onScannedListener listener) {
        mScannerManager = new ScannerManager(context);
        mCameraManager = new CameraManager(context);
        mListener = listener;

        mCallback = new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                // TODO AsyncTask
                Camera.Size s = camera.getParameters().getPreviewSize();

                String qrcode = mScannerManager.decodeByteData(data, s.width, s.height,
                        Config.getViewFinderRect(), null);

                if (qrcode != null) {
//                    camera.stopPreview();
                    mListener.onScanned(qrcode);
                }
            }
        };
    }

    public void scanLocalImage(Bitmap bitmap) {
        String str = mScannerManager.decodeLocalImage(bitmap);
        processScannedResult(str);
    }

    public void openCamera(SurfaceHolder holder) throws IOException {
        mCameraManager.openCamera(holder, mCallback);
    }

    public void updateCamera(SurfaceHolder holder) throws IOException {
        mCameraManager.updateCamera(holder, mCallback);
    }

    public void stopCameraPreview() {
        mCameraManager.stopPreview();
    }

    public void closeCamera() {
        mCameraManager.releaseCamera();
    }


    /**
     * 处理扫描结果
     * @param str 解析得到的结果
     */
    private void processScannedResult(String str) {
        if(str != null) {
            mListener.onScanned(str);
        }else {
            mListener.onScanFailed();
        }
    }

    /**
     * Manager继承这个接口
     */
    public interface IScanner {
        /**
         * 解析本地图片
         * @param bitmap 要解析的图片  intent 传递bitmap 要小于505kb
         * @return 解析得到的结果 null 解析失败
         */
        public String decodeLocalImage(Bitmap bitmap);

        /**
         *
         * @param data  需要解析的相机扫描到的数据 一般是相机preview的数据
         * @param width 数据宽度
         * @param height 数据高度
         * @param viewFinderRect 取景框
         * @param callback 可能的点的回调
         */
        public String decodeByteData(byte[] data, int width, int height,
                                   Rect viewFinderRect, ResultPointCallback callback);
    }

    public interface ICamera {
        /**
         * 打开相机
         * @param holder 相机preview 显示的配置
         * @param callBack 扫描结束后处理结果的回调
         * @throws IOException  相机设置preview不成功 setPreviewDisplay
         */
        public void openCamera(SurfaceHolder holder, Camera.PreviewCallback callBack) throws IOException;

        /**
         * 摄像头移动，更新preview 调整尺寸
         * @param holder SurfaceHolder
         * @param callBack 扫描结束后处理结果的回调
         * @throws IOException
         */
        public void updateCamera(SurfaceHolder holder, Camera.PreviewCallback callBack) throws IOException;

        /**
         * 停止preview更新
         */
        public void stopPreview();

        /**
         * 释放相机资源
         */
        public void releaseCamera();
    }

    /**
     * activity 继承这个接口
     */
    public interface onScannedListener {
        /**
         * 扫描成功
         * @param result 扫描结果
         */
        public void onScanned(String result);

        /**
         * 扫描失败
         */
        public void onScanFailed();
    }
}
