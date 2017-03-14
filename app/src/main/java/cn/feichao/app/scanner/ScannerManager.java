package cn.feichao.app.scanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

import com.google.zxing.ResultPointCallback;

import cn.feichao.app.zxing.Decoder;

/**
 * Created by feichao on 2017/3/11.
 *
 */
public class ScannerManager implements ScannerPresenter.IScanner {

    private Decoder mDecoder;
    private Context mContext;

    public ScannerManager(Context context) {
        mContext = context;
        mDecoder = new Decoder();
    }

    @Override
    public String decodeLocalImage(Bitmap bitmap) {
        return mDecoder.decodeImage(bitmap);
    }

    @Override
    public String decodeByteData(byte[] data, int width, int height,
                               Rect viewFinderRect, ResultPointCallback callback) {

        // TODO 取景框比例调节
        Rect rect = Config.getViewFinderRect();
        Size screenSize = Config.getScreenSize();
        Size previewSize = Config.getCameraPreviewSize();

        Rect newRect = new Rect();
        newRect.left = rect.left * previewSize.getWidth() / screenSize.getWidth();
        newRect.right = rect.right * previewSize.getWidth() / screenSize.getWidth();
        newRect.top = rect.top * previewSize.getHeight() / screenSize.getHeight();
        newRect.bottom = rect.bottom * previewSize.getHeight() / screenSize.getHeight();

        return mDecoder.decodeCameraCatchImage(data, width, height, rect, null);
    }
}
