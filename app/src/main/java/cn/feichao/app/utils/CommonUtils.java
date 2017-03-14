package cn.feichao.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by feichao on 2016/12/21.
 *
 */
public final class CommonUtils {

    private CommonUtils() {
    }

    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "无版本号";
        }
    }


    public static String getFileName(Context context, Uri uri) {
        String filename = "";
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToNext()) {
                filename = cursor.getString(cursor.getColumnIndex("_display_name"));
            }
        }finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return filename;
    }

    public static Bitmap getImage(Context context, Uri uri) {
        if(uri == null) {
            return null;
        }
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = context.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if(in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static void openSystemFile(Activity activity, int requestCode, String fileType){
        //系统调用Action属性
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        //设置文件类型
        intent.setType(fileType);
        // 添加Category属性
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activity.startActivityForResult(intent, requestCode);
    }


    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer).append(", 0x");
        }
        return stringBuilder.toString();
    }
}
