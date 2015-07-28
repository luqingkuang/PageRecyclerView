package com.kingty.library;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;



public class CustomSwipeToRefresh extends SwipeRefreshLayout {
    private OnDispatchTouchEventListener onDispatchTouchEventListener;
    private int mTouchSlop;
    private float mPrevX;

    public CustomSwipeToRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void setNeedRefresh(boolean isNeedRefresh){
        this.isNeedRefresh = isNeedRefresh;
    }
    private boolean isNeedRefresh = true;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        if(isNeedRefresh) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mPrevX = MotionEvent.obtain(event).getX();
                    break;

                case MotionEvent.ACTION_MOVE:
                    final float eventX = event.getX();
                    float xDiff = Math.abs(eventX - mPrevX);

                    if (xDiff > mTouchSlop) {
                        return false;
                    }
            }

            return super.onInterceptTouchEvent(event);
        }else {
            return false;
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            if (getOnDispatchTouchEventListener() != null) {
                if (getOnDispatchTouchEventListener().dispatchTouchEvent(ev)) {
                    return true;
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return super.dispatchTouchEvent(ev);

    }

    public OnDispatchTouchEventListener getOnDispatchTouchEventListener() {
        return onDispatchTouchEventListener;
    }

    public void setOnDispatchTouchEventListener(OnDispatchTouchEventListener onDispatchTouchEventListener) {
        this.onDispatchTouchEventListener = onDispatchTouchEventListener;
    }

    public interface OnDispatchTouchEventListener {
        public boolean dispatchTouchEvent(MotionEvent ev);
    }
}