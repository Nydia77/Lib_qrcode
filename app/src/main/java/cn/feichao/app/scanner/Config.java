package cn.feichao.app.scanner;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.view.Display;
import android.view.WindowManager;

import java.util.List;

/**
 * Created by feichao on 2017/3/14.
 * 用于存放全局的配置
 */
public class Config {

    private static Size mScreenSize;
    private static Rect mViewFinderRect;
    private static Size mCameraPreviewSize;

    public static void setScreenParameter(Context context) {
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        Display display = windowManager.getDefaultDisplay();
        display.getSize(point);
        mScreenSize = new Size(point.x, point.y);
    }

    public static Size getScreenSize() {
        return mScreenSize;
    }

    public static void setViewFinderRect(int w, int h) {
        Size screenSize = getScreenSize();
        int left = (screenSize.getWidth() - w) / 2;
        int top = (screenSize.getHeight() - h) / 2;
        int right = left + w;
        int bottom = top + h;
        mViewFinderRect = new Rect(left, top, right, bottom);
    }

    public static Rect getViewFinderRect() {
        return mViewFinderRect;
    }


    public static Size getSuitablePreviewSize(List<Camera.Size> supportedPreviewSizes) {
        Size cameraPreviewSize;
        Size screenSize = getScreenSize();
        int min = -1;
        Camera.Size minSize = null;
        for(Camera.Size size : supportedPreviewSizes) {
            int var = 0;
            var += Math.abs(size.width - screenSize.getWidth());
            var += Math.abs(size.height - screenSize.getHeight());
            if(min == -1) {
                // 初值
                min = var;
                minSize = size;
            }
            if(min > var) {
                min = var;
                minSize = size;
            }
        }
        if(minSize == null) {
            cameraPreviewSize = new Size(screenSize.getWidth(), screenSize.getHeight());
        }
        cameraPreviewSize = new Size(minSize.width, minSize.height);
        setCameraPreviewSize(cameraPreviewSize);
        return cameraPreviewSize;
    }

    private static void setCameraPreviewSize(Size cameraPreviewSize) {
        mCameraPreviewSize = cameraPreviewSize;
    }

    public static Size getCameraPreviewSize() {
        return mCameraPreviewSize;
    }
}
