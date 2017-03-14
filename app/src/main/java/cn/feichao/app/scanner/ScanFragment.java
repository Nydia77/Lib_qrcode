package cn.feichao.app.scanner;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import cn.feichao.app.R;
import cn.feichao.app.scanner.view.ViewFinderView;
import cn.feichao.app.zxing.Decoder;

/**
 * Created by feichao on 2017/3/7.
 *
 */
public class ScanFragment extends Fragment implements SurfaceHolder.Callback{

    private static final String TAG = "ScanFragment";

    private FrameLayout mLayoutView;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private ScannerPresenter mPresenter;
    private ViewFinderView mCustomView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "create view");
        mLayoutView = (FrameLayout) inflater.inflate(R.layout.frgment_scan, container, false);
        mSurfaceView = (SurfaceView) mLayoutView.findViewById(R.id.preview_view);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);

        // TODO 自定义取景框
        mCustomView = new ViewFinderView(getContext());
        mLayoutView.addView(mCustomView);

        return mLayoutView;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surface created");
        try {
            mPresenter.openCamera(mHolder);
        } catch (Exception e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surface changed");
        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        mPresenter.stopCameraPreview();

        // start preview with new settings
        try {
            mPresenter.updateCamera(mHolder);
        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mPresenter.closeCamera();
    }

    public void setCustomView(ViewFinderView view) {
        mCustomView = view;
    }

    public void setPresenter(ScannerPresenter presenter) {
        this.mPresenter = presenter;
    }

}
