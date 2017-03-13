package cn.feichao.app.scanner;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import cn.feichao.app.R;

public class ScannerActivity extends AppCompatActivity implements ScannerPresenter.OnScannedListener{

    // action
    public static final String SCANIMGACTION = "cn.feichao.app.scanner.SCANLOCALIMAGE";
    public static final String SCANBYCAMERAACTION = "cn.feichao.app.scanner.SCANBYCAMERA";

    // intent
    public static final String IMG = "img";
    public static final String BARCODE = "barcode";
    public static final String BITMAP = "bitmap";

    private ScannerPresenter mPresenter;
    private ScanFragment mScanFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        mPresenter = new ScannerPresenter(this);
        Intent intent = getIntent();
        String action = intent.getAction();
        if(action != null && action.equals(SCANIMGACTION)) {
            Bitmap bitmap = intent.getParcelableExtra(IMG);
            mPresenter.scanLocalImage(bitmap);
        }else {
            initView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_scan);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mScanFragment = new ScanFragment();
        fragmentTransaction.replace(R.id.scan_container, mScanFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onScanned(String barcode) {
        Intent intent = new Intent();
        intent.putExtra(BARCODE, barcode);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onAjusted(Size size) {
        mScanFragment.ajustSurfaceView(size.getWidth(), size.getHeight());
    }

    public ScannerPresenter getPresenter() {
        return mPresenter;
    }

    //    private void regiterScanReceiver() {
//        mReceiver = new ScanBroadcastReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(SCANIMGACTION);
//        intentFilter.addAction(SCANBYCAMERAACTION);
//        registerReceiver(mReceiver, intentFilter);
//    }
//
//    public void unregisterScanReceiver() {
//        unregisterReceiver(mReceiver);
//    }
//
//    private class ScanBroadcastReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            switch (action) {
//                case SCANIMGACTION:
//                    Bitmap bitmap = intent.getParcelableExtra(IMG);
//                    mPresenter.scanLocalImage(bitmap);
//                    break;
//                case SCANBYCAMERAACTION:
//                    initView();
////                    mPresenter.scanByCamera();
//                    break;
//                default:
//                    throw new RuntimeException("无效的action");
//            }
//        }
//    }
}
