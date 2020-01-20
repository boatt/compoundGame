package com.xiao.compound.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * Desc:
 * <p> @Author: ZhouTao
 * <p>Date: 2020/1/21
 * <p>Copyright: Copyright (c) 2016-2020
 * <p>Company: @小牛科技
 * <p>Email:zhoutao@xiaoniuhy.com
 * <p>Update Comments:
 */
public class AnimatorUtils {

    /**
     * 合并动画 左边动画
     * @param view
     */
    public static void mergeAnimatorToLeft(final ImageView view) {
        ObjectAnimator animator0 = ObjectAnimator.ofFloat(view,
                "translationX", 30f, 100f, 0, 0);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view,
                "scaleX", 1, 1, 1, 2.0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view,
                "scaleY", 1, 1, 1, 2.0f);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(view,
                "alpha", 1f, 1f, 1f, 0.1f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
        set.playTogether(animator0, animator1, animator2, animator3);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
                view.setScaleX(1);
                view.setScaleY(1);
                view.setAlpha(1f);
            }
        });
    }

    /**
     * 合并动画 右边动画
     * @param view
     */
    public static void mergeAnimatorToRight(final View view) {
        ObjectAnimator animator0 = ObjectAnimator.ofFloat(view,
                "translationX", -30f, -100f, 0, 0);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view,
                "scaleX", 1, 1, 1, 2.0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view,
                "scaleY", 1, 1, 1, 2.0f);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(view,
                "alpha", 1f, 1f, 1f, 0.1f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
        set.playTogether(animator0, animator1, animator2, animator3);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
                view.setScaleX(1);
                view.setScaleY(1);
                view.setAlpha(1f);
            }
        });
    }


    /**
     * 出生动画
     *
     * @param view
     */
    public static void animatorBirth(View view) {
        Animation scale1 = new ScaleAnimation(0.3f, 1.1f, 0.3f, 1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale1.setDuration(300);
        scale1.setInterpolator(new BounceInterpolator());
        scale1.start();

        view.startAnimation(scale1);
    }


    /**
     * 金币上移动画
     * @param llCoinLayout 金币view
     */
    public static void animatorUp(final View llCoinLayout) {
        llCoinLayout.setVisibility(View.VISIBLE);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0,-100);
        translateAnimation.setDuration(460);
        translateAnimation.setFillAfter(false);
        translateAnimation.setRepeatCount(0);
        translateAnimation.setRepeatMode(ScaleAnimation.RESTART);
        //开始动画
        llCoinLayout.startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llCoinLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }



    /**
     * 放大
     *
     * @param view 视图
     */
    public static  void animatorZoom(View view) {
        Animation scale1 = new ScaleAnimation(1, 1.5f, 1, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale1.setDuration(300);
        scale1.setInterpolator(new DecelerateInterpolator());
        scale1.start();

        view.startAnimation(scale1);
    }

    /**
     * 移动动画
     * @param sTouchView 手指按下的View
     * @param sTargetView 目标View
     */
    public static void animatorMove(final View sTouchView,final View sTargetView) {

        float touchPivotX = sTouchView.getLeft();
        float touchPivotY = sTouchView.getTop();
        float indexPivotX = sTargetView.getLeft();
        float indexPivotY = sTargetView.getTop();


        TranslateAnimation translateAnimation = new TranslateAnimation(-touchPivotX + indexPivotX, 0, indexPivotY - touchPivotY, 0);
        translateAnimation.setDuration(300);
        translateAnimation.setFillAfter(false);
        translateAnimation.setRepeatCount(0);
        translateAnimation.setRepeatMode(ScaleAnimation.RESTART);
        //开始动画
        sTouchView.startAnimation(translateAnimation);

        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                sTouchView.setTranslationX(0);
                sTouchView.setTranslationY(0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    public static void backCat(final int touchIndex,final View mCatViewLayout,View mFloatCatView,Rect touchRect, int mMoveX, int mMoveY) {
        //计算偏移值
        int translateX = mMoveX - touchRect.centerX();
        int translateY = mMoveY - touchRect.centerY();

        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -translateX, Animation.RELATIVE_TO_SELF, -translateY);
        translateAnimation.setDuration(300);
        translateAnimation.setFillAfter(false);
        translateAnimation.setRepeatCount(0);
        translateAnimation.setRepeatMode(ScaleAnimation.RESTART);
        //开始动画
        mFloatCatView.startAnimation(translateAnimation);

        MockData.getInstance().getCatList()[touchIndex].touching = false;
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mCatViewLayout.requestLayout();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
