package cn.feichao.app.scanner;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

/**
 * Created by feichao on 2017/3/7.
 *
 */
public class ScanFragment extends Fragment implements SurfaceHolder.Callback{

    private static final String TAG = "ScanFragment";

    private SurfaceView mSurfaceView;
    private ViewFinderView mViewFinderView;
    private SurfaceHolder mHolder;
    private static ScannerActivity mActivity;
    private ScannerPresenter mPresenter;


    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //TODO handle the qrcode
            String str = (String) msg.obj;
            mActivity.onScanned(str);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "create view");
        View view = inflater.inflate(R.layout.frgment_scan, container, false);
        mSurfaceView = (SurfaceView) view.findViewById(R.id.preview_view);
        mViewFinderView = (ViewFinderView)view.findViewById(R.id.view_find_view);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "activity created");
        mActivity = (ScannerActivity) getActivity();
        mPresenter = mActivity.getPresenter();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surface created");
        try {
            mPresenter.openCamera(mActivity, mHolder, mViewFinderView.getRect(), mHandler);
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

        // 调整preview尺寸
        mPresenter.adaptCameraPreview(width, height);

        // start preview with new settings
        try {
            mPresenter.updateCamera(mHolder, mViewFinderView.getRect(), mHandler);
        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mPresenter.closeCamera();
    }

    public void ajustSurfaceView(int width, int height) {
        mSurfaceView.setLayoutParams(new FrameLayout.LayoutParams(width, height));
    }

}
