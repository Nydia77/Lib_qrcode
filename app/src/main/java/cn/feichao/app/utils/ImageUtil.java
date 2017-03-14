package cn.feichao.app.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Build;

import java.io.ByteArrayOutputStream;

/**
 * Created by feichao on 2017/3/11.
 *
 */
public final class ImageUtil {

    public static Bitmap fromYUV(byte[] data, int width, int height, Rect rect) {
        YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, width, height, null);
        ByteArrayOutputStream raw = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, width, height), 100, raw);
        byte[] temp = raw.toByteArray();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        // TODO OOM
        Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length, options);
        return Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height());
    }

    public static int getBitmapSize(Bitmap bitmap){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1){//API 12
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }
}
