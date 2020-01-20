package com.xiao.compound;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SeatViewLayout extends FrameLayout {

    ArrayList<View> mKongLongs = new ArrayList<>();
    private int mScreenWidth;
    private int mSingleWidth;

    //行
    byte mRow = 3;
    //列
    byte mColumn = 4;
    int SEAT_HEIGHT = 180;
    int SEAT_WIDTH = 140;

    Rect[] mRectFList = new Rect[mRow * mColumn];

    public SeatViewLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public SeatViewLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public SeatViewLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        mScreenWidth = dm.widthPixels;

        mSingleWidth = mScreenWidth / mColumn;

        for (int i = 0; i < mColumn; i++) {
            for (int j = 0; j < mRow; j++) {
                View inflate = View.inflate(getContext(), R.layout.layout_seat, null);
                addView(inflate, SEAT_WIDTH, SEAT_HEIGHT);
                mKongLongs.add(inflate);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int row = 0;
        int column = 0;
        //给点边距
        int margin = 60;
        for (int i = 0; i < getChildCount(); i++) {
            column++;

            View childAt = getChildAt(i);
            //求中心
            int centre = ((column - 1) * mSingleWidth) + mSingleWidth / 2;

            mRectFList[i] = new Rect(centre - SEAT_WIDTH /2, row * SEAT_HEIGHT + margin * (row + 1), centre + SEAT_WIDTH /2, row * SEAT_HEIGHT + SEAT_HEIGHT + margin * (row + 1));

            childAt.layout(mRectFList[i].left, mRectFList[i].top, mRectFList[i].right, mRectFList[i].bottom);

            if (column == mColumn) {
                column = 0;
                row++;
            }
        }
    }

    public Rect getRectForIndex(int index) {
        if (index == -1) {
            return null;
        }
        return mRectFList[index];
    }


}
