package com.harryio.flubo.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

public class ColoredCircleView extends View {
    private Paint circlePaint;
    private int color;

    public ColoredCircleView(Context context) {
        this(context, null);
    }

    public ColoredCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColoredCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centerX = half(getRight() - getLeft());
        int centerY = half(getBottom() - getTop());

        int radius = half(Math.min(getWidth(), getHeight()));
        canvas.drawCircle(centerX, centerY, radius, circlePaint);
    }

    private int half(int val) {
        return val / 2;
    }

    public void setcolor(@ColorRes int colorId) {
        color = ContextCompat.getColor(getContext(), colorId);
        circlePaint.setColor(color);

        invalidate();
    }

    public int getColor() {
        return color;
    }
}