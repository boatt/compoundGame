package com.xiao.compound;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.xiao.compound.bean.CatInfo;
import com.xiao.compound.utils.AnimatorUtils;
import com.xiao.compound.utils.ResUtils;
import com.xiao.compound.utils.SoundPlayUtils;

public class GameLayout extends FrameLayout implements LifecycleObserver {

    private View mFloatCatView;
    ImageView mToLeftImageView;
    ImageView mToRightImageView;
    Rect mMergePoint;
    /**
     * 座驾
     */
    SeatViewLayout mSetViewLayout;
    CatViewLayout mCatViewLayout;

    int mMoveX;
    int mMoveY;
    int mTouchIndex;

    public GameLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public GameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume() {
        mCatViewLayout.startBeat();
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
        mCatViewLayout.stopBeat();
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        mCatViewLayout.stopBeat();
    }


    private void init() {
        mSetViewLayout = new SeatViewLayout(getContext());
        mCatViewLayout = new CatViewLayout(getContext());

        addView(mSetViewLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mCatViewLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);




    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mFloatCatView != null) {
            mFloatCatView.layout(mMoveX - mCatViewLayout.CAT_WIDTH /2, mMoveY - mCatViewLayout.CAT_HEIGHT /2, mMoveX + mCatViewLayout.CAT_WIDTH /2, mMoveY + mCatViewLayout.CAT_HEIGHT /2);
        }
        if (mToLeftImageView != null && mToLeftImageView.getVisibility()==VISIBLE && mMergePoint != null) {
            mToLeftImageView.layout(mMergePoint.left, mMergePoint.top, mMergePoint.right, mMergePoint.bottom);
        }
        if (mToRightImageView != null && mToRightImageView.getVisibility()==VISIBLE && mMergePoint != null) {
            mToRightImageView.layout(mMergePoint.left, mMergePoint.top, mMergePoint.right, mMergePoint.bottom);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mMoveX = (int) event.getX();
        mMoveY = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchIndex = mCatViewLayout.contains(mMoveX, mMoveY);
                if (mTouchIndex >= 0) {
                    if (mCatViewLayout.getCat(mTouchIndex) != null) {
                        Log.d("xxxxx ", "Touch    ====   有效" + mTouchIndex);

                        mCatViewLayout.setTouching(mTouchIndex);
                        //有效的范围
                        mFloatCatView = mCatViewLayout.generateCat(mCatViewLayout.getCat(mTouchIndex).level);
                        if (mFloatCatView.getParent()!=null&&mFloatCatView.getParent() instanceof ViewGroup) {
                            ((ViewGroup) mFloatCatView.getParent()).removeAllViews();
                        }
                        addView(mFloatCatView, mCatViewLayout.CAT_WIDTH, mCatViewLayout.CAT_HEIGHT);
                    }
                } else {
                    Log.d("xxxxx ", "Touch 无效" + mTouchIndex);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mFloatCatView == null) {
                    return true;
                }
                int targetIndex = mCatViewLayout.contains(mMoveX, mMoveY);
                if (targetIndex >= 0) {
                    if (targetIndex != mTouchIndex) {
                        CatInfo touchCat = mCatViewLayout.getCat(mTouchIndex);
                        CatInfo cat = mCatViewLayout.getCat(targetIndex);
                        if (cat != null) {
                            if (cat.level != touchCat.level) {
                                Log.d("xxxxx ", "交换 " + targetIndex);
                                mCatViewLayout.swapCat(mTouchIndex, targetIndex);
                            } else {
                                if (mToLeftImageView == null) {
                                    mToLeftImageView = new ImageView(getContext());

                                    addView(mToLeftImageView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                                }

                                if (mToRightImageView == null) {
                                    mToRightImageView = new ImageView(getContext());

                                    addView(mToRightImageView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                                }
                                int level = cat.level + 1;
                                int res = ResUtils.getCatImage(level);
                                mToLeftImageView.setImageResource(res);
                                mToRightImageView.setImageResource(res);
                                mToLeftImageView.setVisibility(VISIBLE);
                                mToRightImageView.setVisibility(VISIBLE);

                                Rect swapLocation = mSetViewLayout.getRectForIndex(targetIndex);
                                mMergePoint = new Rect(swapLocation.left, swapLocation.top, swapLocation.right, swapLocation.bottom);
                                //合并
                                mCatViewLayout.mergeCat(mTouchIndex, targetIndex);
                                Log.d("xxxxx ", "合并 " + targetIndex);
                                SoundPlayUtils.play(3);
                                AnimatorUtils.mergeAnimatorToLeft(mToLeftImageView);
                                AnimatorUtils.mergeAnimatorToRight(mToRightImageView);
                            }
                        } else {
                            Log.d("xxxxx ", "移动 " + targetIndex);
                            mCatViewLayout.moveCat(mTouchIndex, targetIndex);
                        }

                    } else {
                        Log.d("xxxxx ", "同一个位置 返回 " + targetIndex);
                        Rect targetLocation = mSetViewLayout.getRectForIndex(mTouchIndex);
                        AnimatorUtils.backCat(mTouchIndex,mCatViewLayout,mFloatCatView,targetLocation ,mMoveX, mMoveY);
                    }
                } else {
                    Log.d("xxxxx ", "什么位置都不是 返回  " + targetIndex);
                    Rect targetLocation = mSetViewLayout.getRectForIndex(mTouchIndex);
                    AnimatorUtils.backCat(mTouchIndex,mCatViewLayout ,mFloatCatView ,targetLocation ,mMoveX, mMoveY);
                }
                removeView(mFloatCatView);
                mFloatCatView = null;
                break;
            default:
                break;
        }
        requestLayout();
        return true;
    }

    /**
     * 重新布局猫的摆放层
     */
    public void requestLayoutCatView() {
        mCatViewLayout.requestLayout();
    }
}
