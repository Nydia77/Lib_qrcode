package cn.feichao.app.scanner;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import cn.feichao.app.R;
import cn.feichao.app.scanner.view.ViewFinderView;

public class ScannerActivity extends AppCompatActivity implements ScannerPresenter.onScannedListener {

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
        mPresenter = new ScannerPresenter(getApplicationContext(), this);
        Intent intent = getIntent();
        String action = intent.getAction();
        if(action != null && action.equals(SCANIMGACTION)) {
            // 解析图片
            // TODO bitmap
            Bitmap bitmap = intent.getParcelableExtra(IMG);
            mPresenter.scanLocalImage(bitmap);
        }else {
            // 相机扫描
            // 设置参数
            Config.setScreenParameter(this);
            initView();
        }
    }

    private void initView() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_scan);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mScanFragment = new ScanFragment();
        // TODO 设置fragment的参数
        mScanFragment.setPresenter(mPresenter);
        customView(200, 200);

        fragmentTransaction.replace(R.id.scan_container, mScanFragment);
        fragmentTransaction.commit();
    }

    private void customView(int width, int height) {
        Config.setViewFinderRect(width, height);
        ViewFinderView viewFinderView = new ViewFinderView(this);
        mScanFragment.setCustomView(viewFinderView);
    }

    @Override
    public void onScanned(String barcode) {
        Intent intent = new Intent();
        intent.putExtra(BARCODE, barcode);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onScanFailed() {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK == keyCode) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
