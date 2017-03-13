package cn.feichao.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import cn.feichao.app.scanner.ScannerActivity;
import cn.feichao.app.utils.CommonUtils;

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

        // 获取图片
        if(requestCode == GETIMAGEREQUESTCODE && data != null) {
            Bitmap bitmap = CommonUtils.getImage(this, data.getData());
//            Decoder decoder = new Decoder();
//            String result = decoder.decodeImage(bitmap);
//            if(result == null) {
//                Toast.makeText(DecodeActivity.this, "解析失败", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(DecodeActivity.this, result, Toast.LENGTH_SHORT).show();
//            }
            Intent scanImage = new Intent(ScannerActivity.SCANIMGACTION);
            scanImage.putExtra(ScannerActivity.IMG, bitmap);
            startActivityForResult(scanImage, IMAGEREQUESTCODE);
        }

        // 处理图片解析的结果
        if(requestCode == IMAGEREQUESTCODE && RESULT_OK == resultCode && data != null ) {
            String barcode = data.getStringExtra(ScannerActivity.BARCODE);
            if(barcode == null) {
                Toast.makeText(DecodeActivity.this, "解析失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DecodeActivity.this, barcode, Toast.LENGTH_SHORT).show();
            }
        }

        // 处理相机扫描的结果
        if(CAMERAQUESTCODE == requestCode && RESULT_OK == resultCode && data != null) {
            String barcode = data.getStringExtra(ScannerActivity.BARCODE);
//            Bitmap bitmap = data.getParcelableExtra("bitmap");
//            imageView.setImageBitmap(bitmap);
            Toast.makeText(DecodeActivity.this, barcode, Toast.LENGTH_SHORT).show();
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
        decodeImgBtn.setOnClickListener(this);
        scanBarcodeBtn = (Button)findViewById(R.id.scan_barcode_btn);
        scanBarcodeBtn.setOnClickListener(this);
        imageView = (ImageView)findViewById(R.id.imageView2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.decode_img_btn:
                CommonUtils.openSystemFile(DecodeActivity.this, GETIMAGEREQUESTCODE, "*/*");
                break;
            case R.id.scan_barcode_btn:
                Intent scan = new Intent(DecodeActivity.this, ScannerActivity.class);
//                Intent scan = new Intent(ScannerActivity.SCANBYCAMERAACTION);
                startActivityForResult(scan, CAMERAQUESTCODE);
                break;
        }
    }

}
