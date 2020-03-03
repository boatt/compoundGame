package com.xiao.compound;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xiao.compound.bean.CatInfo;
import com.xiao.compound.utils.AnimatorUtils;
import com.xiao.compound.utils.ResUtils;
import com.xiao.compound.utils.MockData;

import java.util.ArrayList;
import java.util.Random;

public class CatViewLayout extends FrameLayout {

    ArrayList<View> mCatViewList = new ArrayList<>();
    //屏幕
    private int mScreenWidth;
    private int mSingleWidth;
    //行
    byte mRow = 3;
    //列
    byte mColumn = 4;
    public int CAT_HEIGHT = 180;
    public int CAT_WIDTH = 140;
    Rect[] mRectFList = new Rect[mRow * mColumn];

    Random mRandom = new Random();
    private long ANIMATOR_BEAT_TIME = 2000;


    public CatViewLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public CatViewLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public CatViewLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

                View catView = generateCat(1);
                catView.setVisibility(INVISIBLE);
                addView(catView, CAT_WIDTH, CAT_HEIGHT);
                mCatViewList.add(catView);
            }
        }

        startBeat();
    }


    //生成猫
    public View generateCat(int level) {
        View catView = View.inflate(getContext(), R.layout.layout_cat, null);
        ImageView catImage = catView.findViewById(R.id.catIcon);
        TextView tvNumber = catView.findViewById(R.id.tvNumber);
        catImage.setImageResource(ResUtils.getCatImage(level));
        tvNumber.setText(String.valueOf(level));
        return catView;
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

            ViewGroup childAt = (ViewGroup) getChildAt(i);

            CatInfo catInfo = MockData.getInstance().getCatList()[i];
            if (catInfo != null) {
                ImageView image = (ImageView) childAt.getChildAt(0);
                TextView text = (TextView) childAt.getChildAt(1);
                int res = ResUtils.getCatImage(catInfo.level);
                image.setImageResource(res);
                text.setText(String.valueOf(catInfo.level));
                //被按下就不要跳动动画
                if (catInfo.touching) {
                    image.setAlpha(0.5f);
                } else {
                    image.setAlpha(1f);
                    if (catInfo.newCat) {
                        catInfo.newCat = false;
                        AnimatorUtils.animatorBirth(image);
                    }
                }
                childAt.setVisibility(VISIBLE);
            } else {
                childAt.setVisibility(INVISIBLE);
            }

            //求中心
            int centre = ((column - 1) * mSingleWidth) + mSingleWidth / 2;

            mRectFList[i] = new Rect(centre - CAT_WIDTH / 2, row * CAT_HEIGHT + margin * (row + 1), centre + CAT_WIDTH / 2, row * CAT_HEIGHT + CAT_HEIGHT + margin * (row + 1));

            childAt.layout(mRectFList[i].left, mRectFList[i].top, mRectFList[i].right, mRectFList[i].bottom);

            if (column == mColumn) {
                column = 0;
                row++;
            }
        }
        Log.d("111", "33333");
    }

    public void stopBeat() {
        mHandler.removeMessages(1);
    }
    public void startBeat() {
        mHandler.sendEmptyMessageDelayed(1, ANIMATOR_BEAT_TIME);
    }


    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            removeMessages(1);
            sendEmptyMessageDelayed(1, ANIMATOR_BEAT_TIME);
            animatorBeat();
        }
    };

    /**
     * 金币上涨 跳动动画
     */
    private void animatorBeat() {
        int index = mRandom.nextInt(mCatViewList.size());
        View view = mCatViewList.get(index);
        if (view.getVisibility() != VISIBLE) {
            return;
        }
        ImageView catImage = view.findViewById(R.id.catIcon);
        View llCoinLayout = view.findViewById(R.id.llCoinLayout);
        //放大
        AnimatorUtils.animatorZoom(catImage);
        //位移
        AnimatorUtils.animatorUp(llCoinLayout);

    }



    /**
     * 从指定位置获取猫的信息
     * @param index 索引位置
     * @return  猫的信息
     */
    public CatInfo getCat(int index) {
        if (index == -1) {
            return null;
        }
        CatInfo[] catList = MockData.getInstance().getCatList();
        return catList[index];
    }



    public int contains(int mMoveX, int mMoveY) {
        for (int i = 0; i < mRectFList.length; i++) {
            Rect rectF = mRectFList[i];
            if (rectF.top <= mMoveY && rectF.bottom >= mMoveY && rectF.left <= mMoveX && rectF.right >= mMoveX) {
                return i;
            }
        }
        return -1;
    }

    public void moveCat(int mTouchIndex, int targetIndex) {
        CatInfo[] catList = MockData.getInstance().getCatList();
        catList[mTouchIndex].touching = false;
        catList[targetIndex] = catList[mTouchIndex];
        catList[mTouchIndex] = null;
        requestLayout();

        AnimatorUtils.animatorBirth(mCatViewList.get(targetIndex));
    }

    public void swapCat(int mTouchIndex, int targetIndex) {
        CatInfo[] catList = MockData.getInstance().getCatList();

        catList[mTouchIndex].touching = false;
        CatInfo catInfo = catList[targetIndex];
        catList[targetIndex] = catList[mTouchIndex];
        catList[mTouchIndex] = catInfo;
        requestLayout();

        AnimatorUtils.animatorZoom(mCatViewList.get(targetIndex));

        final View sTouchView = mCatViewList.get(mTouchIndex);
        final View sTargetView = mCatViewList.get(targetIndex);
        AnimatorUtils.animatorMove(sTouchView, sTargetView);

    }

    public void setTouching(int mTouchIndex) {
        CatInfo[] catList = MockData.getInstance().getCatList();
        catList[mTouchIndex].touching = true;
        requestLayout();
    }

    public void mergeCat(int mTouchIndex, int targetIndex) {
        CatInfo[] catList = MockData.getInstance().getCatList();
        catList[targetIndex].level = catList[targetIndex].level + 1;
        catList[mTouchIndex] = null;
        requestLayout();
    }


}
