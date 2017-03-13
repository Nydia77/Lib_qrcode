package cn.feichao.app.scanner;

import android.graphics.Bitmap;

import cn.feichao.app.zxing.Decoder;

/**
 * Created by feichao on 2017/3/11.
 *
 */
public class ScannerManager implements ScannerPresenter.IScanner {

    private Decoder mDecoder;

    public ScannerManager() {
        mDecoder = new Decoder();
    }

    @Override
    public String scanLocalImage(Bitmap bitmap) {
        return mDecoder.decodeImage(bitmap);
    }

}
