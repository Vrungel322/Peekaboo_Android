package com.peekaboo.presentation.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import com.peekaboo.presentation.utils.ResourcesUtils;

/**
 * Created by arkadius on 9/27/16.
 */

public class PHorizontalScrollView extends HorizontalScrollView {
    private boolean scrollAvailable = true;

    public PHorizontalScrollView(Context context) {
        super(context);
        fixWidth(context);
    }

    public PHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        fixWidth(context);
    }

    public PHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        fixWidth(context);
    }


    public void setScrollAvailable(boolean scrollAvailable) {
        this.scrollAvailable = scrollAvailable;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return scrollAvailable && super.onInterceptTouchEvent(ev);
    }
    public void fixWidth(Context context){
        if(getWidth() < ResourcesUtils.getDisplayWidth(context)){
            setMinimumWidth(ResourcesUtils.getDisplayWidth(context));
        }
    }
}
