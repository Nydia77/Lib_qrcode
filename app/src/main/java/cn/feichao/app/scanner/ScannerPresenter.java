package cn.feichao.app.scanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.view.SurfaceHolder;

import java.io.IOException;

import cn.feichao.app.scanner.camera.CameraManager;

/**
 * Created by feichao on 2017/3/11.
 *
 */
public class ScannerPresenter {

    private ScannerActivity mActivity;
    private ScannerManager mScannerManager;
    private CameraManager mCameraManager;


    public ScannerPresenter(ScannerActivity activity) {
        mScannerManager = new ScannerManager();
        mCameraManager = new CameraManager();
        mActivity = activity;
    }

    public void scanLocalImage(Bitmap bitmap) {
        String str = mScannerManager.scanLocalImage(bitmap);
        processScannedResult(str);
    }

    public void openCamera(Context context, SurfaceHolder holder, Rect rect, Handler handler) throws IOException {
        mCameraManager.openCamera(context, holder, rect, handler);
    }

    public void updateCamera(SurfaceHolder holder, Rect rect, Handler handler) throws IOException {
        mCameraManager.updateCamera(holder, rect, handler);
    }

    public void stopCameraPreview() {
        mCameraManager.stopPreview();
    }

    public void closeCamera() {
        mCameraManager.releaseCamera();
    }

    public void adaptCameraPreview(int width, int height) {
        Size size = mCameraManager.ajustPreviewSize(width, height);
        mActivity.onAjusted(size);
    }

    /**
     * 处理扫描结果
     * @param str 解析得到的结果
     */
    private void processScannedResult(String str) {
        if(str != null) {
            mActivity.onScanned(str);
        }else {
            throw new RuntimeException("扫描失败");
        }
    }

    /**
     * Manager继承这个接口
     */
    public interface IScanner {
        public String scanLocalImage(Bitmap bitmap);
    }

    public interface ICamera {
        public void openCamera(Context context, SurfaceHolder holder, Rect rect, Handler handler) throws Exception;
        public void updateCamera(SurfaceHolder holder, Rect rect, Handler handler) throws IOException;
        public void stopPreview();
        public void releaseCamera();
    }

    /**
     * activity 继承这个接口
     */
    public interface OnScannedListener {
        public void onScanned(String result);
        public void onAjusted(Size size);
    }
}
