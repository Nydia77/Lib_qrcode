package cn.feichao.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import cn.feichao.app.scanner.ScannerActivity;
import cn.feichao.app.utils.CommonUtils;
import cn.feichao.app.utils.ImageUtil;

public class DecodeActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "DecodeActivity";

    private static final int GETIMAGEREQUESTCODE = 272;
    private static final int IMAGEREQUESTCODE = 172;
    private static final int CAMERAQUESTCODE = 123;

    private Button decodeImgBtn, scanBarcodeBtn;
    private ImageView imageView;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GETIMAGEREQUESTCODE: // 获取图片
                if(data != null) {
                    // TODO 问题 图片实际大小才129k png格式 但是转换成bitmap却要8M
                    Bitmap bitmap = CommonUtils.getImage(this, data.getData());
                    int size = ImageUtil.getBitmapSize(bitmap);
                    if(size > 500 * 1024) {  // 超过500k需要缩小
                        Toast.makeText(DecodeActivity.this, "图片过大", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent scanImage = new Intent(ScannerActivity.SCANIMGACTION);
                    scanImage.putExtra(ScannerActivity.IMG, bitmap);
                    startActivityForResult(scanImage, IMAGEREQUESTCODE);
                }else {
                    Toast.makeText(DecodeActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case IMAGEREQUESTCODE:  // 图片解析结果处理
                processDecodedResult(resultCode, data);
                break;
            case CAMERAQUESTCODE: // 处理相机扫描的结果
                processDecodedResult(resultCode, data);
                break;
        }
    }

    private void processDecodedResult(int resultCode, Intent data) {
        if(RESULT_OK == resultCode && data != null ) {
            String barcode = data.getStringExtra(ScannerActivity.BARCODE);
            if (barcode == null) {
                Toast.makeText(DecodeActivity.this, "解析失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DecodeActivity.this, barcode, Toast.LENGTH_SHORT).show();
            }
        }else if(RESULT_CANCELED == resultCode) {
            Log.i("DecodeActivity", "");
        }else {
            Toast.makeText(DecodeActivity.this, "解析失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode);

        initView();
    }

    private void initView() {
        decodeImgBtn = (Button)findViewById(R.id.decode_img_btn);
        if (decodeImgBtn != null) {
            decodeImgBtn.setOnClickListener(this);
        }
        scanBarcodeBtn = (Button)findViewById(R.id.scan_barcode_btn);
        if (scanBarcodeBtn != null) {
            scanBarcodeBtn.setOnClickListener(this);
        }
        imageView = (ImageView)findViewById(R.id.imageView2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.decode_img_btn:
                CommonUtils.openSystemFile(DecodeActivity.this, GETIMAGEREQUESTCODE, "image/*");
                break;
            case R.id.scan_barcode_btn:
                Intent scan = new Intent(DecodeActivity.this, ScannerActivity.class);
//                Intent scan = new Intent(ScannerActivity.SCANBYCAMERAACTION);
                startActivityForResult(scan, CAMERAQUESTCODE);
                break;
        }
    }

}
