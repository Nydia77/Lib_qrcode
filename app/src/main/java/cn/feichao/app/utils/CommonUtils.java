package cn.feichao.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by feichao on 2016/12/21.
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
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToNext()) {
            filename = cursor.getString(cursor.getColumnIndex("_display_name"));
        }
        return filename;
    }

    public static Bitmap getImage(Context context, Uri uri) {
        if(uri == null) {
            return null;
        }
        Bitmap bitmap = null;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex("_data"));

            bitmap = BitmapFactory.decodeFile(path);
            return bitmap;
        }
        return bitmap;
    }

    public static void openSystemFile(Activity activity, int requestCode, String fileType){
        //系统调用Action属性
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        //设置文件类型
        intent.setType(fileType);
        // 添加Category属性
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activity.startActivityForResult(intent, requestCode);
    }

}
