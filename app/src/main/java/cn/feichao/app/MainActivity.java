package cn.feichao.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button encodeBtn = (Button)findViewById(R.id.encode_btn);
        Button decodeBtn = (Button)findViewById(R.id.decode_btn);
        if (encodeBtn != null) {
            encodeBtn.setOnClickListener(new ButtonListener(R.id.encode_btn));
        }
        if (decodeBtn != null) {
            decodeBtn.setOnClickListener(new ButtonListener(R.id.decode_btn));
        }
    }



    class ButtonListener implements View.OnClickListener {
        private int btnId;

        public ButtonListener(int btnId) {
            this.btnId = btnId;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.encode_btn:
                    Intent encode = new Intent(MainActivity.this, EncodeActivity.class);
                    startActivity(encode);
                    break;
                case R.id.decode_btn:
                    Intent decode = new Intent(MainActivity.this, DecodeActivity.class);
                    startActivity(decode);
                    break;
            }
        }
    }
}
