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

/**
 * Created by feichao on 2017/3/9.
 *
 */
public class ViewFinderView extends View implements ResultPointCallback {

    private Rect rect;
    private Paint paint = new Paint();

    // TODO 设置取景框尺寸
    private int viewFinderWidth = 350;
    private int viewFinderHeight = 350;

    public ViewFinderView(Context context) {
        super(context);
        init();
    }

    public ViewFinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewFinderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Rect getRect() {
        return rect;
    }

    private void init() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int left = (point.x - viewFinderWidth) / 2;
        int top = (point.y - viewFinderHeight) / 2;
        int right = left + viewFinderWidth;
        int bottom = top + viewFinderHeight;
        rect = new Rect(left, top, right, bottom);
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
        // TODO 取景框大小
        canvas.drawRect(rect, paint);

    }


    @Override
    public void foundPossibleResultPoint(ResultPoint point) {
    }
}
