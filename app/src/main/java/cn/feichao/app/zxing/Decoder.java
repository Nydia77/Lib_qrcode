package cn.feichao.app.zxing;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.HybridBinarizer;

import java.util.HashMap;
import java.util.Map;

import cn.feichao.app.utils.ImageUtil;

/**
 * Created by feichao on 2017/3/3.
 * 二维码解码
 */
public class Decoder {

    private Reader decoder;

    public Decoder() {
        decoder = new MultiFormatReader();
    }

    public String decodeCameraCatchImage(byte[] data, int width, int height, Rect rect) {
        return decodeCameraCatchImage(data, width, height, rect, null);
    }

    public String decodeCameraCatchImage(byte[] data, int width, int height, Rect rect, ResultPointCallback callback) {
        if(data == null) {
            return null;
        }
        // 旋转数据
//        Log.i(TAG, "width = " + width + "height = " + height + "data.length = " + data.length);
//        byte[] rotatedData = new byte[data.length];
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++)
//                rotatedData[x * height + height - y - 1] = data[x + y * width];
//        }

        PlanarYUVLuminanceSource luminanceSource = new PlanarYUVLuminanceSource(
                data, width, height, rect.left, rect.top, rect.width(), rect.height(), false);
        Binarizer binarizer = new HybridBinarizer(luminanceSource);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

        return decode(binaryBitmap, callback);
    }

    public String decodeImage(Bitmap bitmap) {
        return decodeImage(bitmap, null);
    }

    public String decodeImage(Bitmap bitmap, ResultPointCallback callback) {
        if(bitmap == null) {
            return null;
        }
        LuminanceSource luminanceSource = new BitmapLuminanceSource(bitmap);
        Binarizer binarizer = new HybridBinarizer(luminanceSource);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

        return decode(binaryBitmap, callback);
    }

    private String decode(BinaryBitmap binaryBitmap, ResultPointCallback callback) {
        try {
            // TODO zxing decoder hints setup
            Map<DecodeHintType, Object> hints = new HashMap<>();
            if(callback != null) {
                hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, callback);
            }
//            else {
//                hints = null;
//            }
//            List<BarcodeFormat> formats = new ArrayList<>();
//            formats.add(BarcodeFormat.QR_CODE);
//            hints.put(DecodeHintType.POSSIBLE_FORMATS, formats);
            hints.put(DecodeHintType.TRY_HARDER, true);

            Result result = decoder.decode(binaryBitmap, hints);
            if("".equals(result.getText())) {
                return null;
            }
            return result.getText();
        } catch (NotFoundException | ChecksumException | FormatException e) {
            e.printStackTrace();
        }
        return null;
    }


    class BitmapLuminanceSource extends LuminanceSource {
        private byte[] pixels;

        protected BitmapLuminanceSource(Bitmap bitmap) {
            super(bitmap.getWidth(), bitmap.getHeight());
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            int[] data = new int[w * h];
            pixels = new byte[w * h];
            bitmap.getPixels(data, 0, w, 0, 0, w, h);
            for(int i = 0; i < data.length; i++) {
                pixels[i] = (byte)data[i];
            }
        }

        @Override
        public byte[] getRow(int y, byte[] row) {
            System.arraycopy(pixels, y * getWidth(), row, 0, getWidth());
            return row;
        }

        @Override
        public byte[] getMatrix() {
            return pixels;
        }
    }
}
