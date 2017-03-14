package cn.feichao.app.scanner.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;

import cn.feichao.app.scanner.Config;
import cn.feichao.app.scanner.Size;

/**
 * Created by feichao on 2017/3/9.
 *
 */
public class ViewFinderView extends View implements ResultPointCallback {

    private Paint paint = new Paint();

    public ViewFinderView(Context context) {
        super(context);
    }

    public ViewFinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewFinderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.GREEN);
        paint.setAntiAlias(true);  // 抗锯齿
        paint.setStyle(Paint.Style.STROKE);  // 空心
        canvas.drawRect(Config.getViewFinderRect(), paint);
    }


    @Override
    public void foundPossibleResultPoint(ResultPoint point) {
    }

}
