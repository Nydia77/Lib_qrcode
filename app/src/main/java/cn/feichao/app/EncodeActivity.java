package cn.feichao.app;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import cn.feichao.app.zxing.Encoder;

public class EncodeActivity extends AppCompatActivity implements View.OnClickListener{

    private String[] barcodeFormat = {"QRCode", "DataMatrix", "Aztec", "PDF417"};

    private Button encodeBtn;
    private EditText encodeContentEditText;
    private Spinner encodeTypeSpinner;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode);

        initView();
    }

    private void initView() {
        encodeContentEditText = (EditText)findViewById(R.id.encode_content);
        encodeTypeSpinner = (Spinner)findViewById(R.id.encode_type);
        encodeTypeSpinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, barcodeFormat));
        encodeBtn = (Button)findViewById(R.id.encode_btn);
        encodeBtn.setOnClickListener(this);
        imageView = (ImageView)findViewById(R.id.imageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.encode_btn:
                Encoder encoder = new Encoder();
                String text = encodeContentEditText.getText().toString();
                String type = (String) encodeTypeSpinner.getSelectedItem();
                Bitmap bitmap = encoder.createImage(text, 300, 300, type);
                imageView.setImageBitmap(bitmap);
                break;
        }
    }
}
