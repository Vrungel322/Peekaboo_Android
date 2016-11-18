package com.peekaboo.presentation.app.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.StrictMode;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.peekaboo.BuildConfig;
import com.peekaboo.R;
import com.peekaboo.presentation.utils.ResourcesUtils;

/**
 * Created by arkadii on 11/17/16.
 */

public class OnlineIndicatorView extends FrameLayout {


    private View indicatorView;
    private TextView messagesCountView;
    private int onlineBackgroundColor;
    private int offlineBackgroundColor;
    private int onlineColorText;
    private int offlineColorText;
    private int strokeWidth;
    private float[] indicatorTextSizes = new float[3];
    private int indicatorTextHeight;

    public OnlineIndicatorView(Context context) {
        super(context);
        initializeViews(context);
    }

    public OnlineIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public OnlineIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        onlineBackgroundColor = ResourcesUtils.getColor(context, R.color.onlineBackground);
        offlineBackgroundColor = ResourcesUtils.getColor(context, R.color.offlineBackground);
        onlineColorText = ResourcesUtils.getColor(context, R.color.online_text);
        offlineColorText = ResourcesUtils.getColor(context, R.color.offline_text);
        strokeWidth = ResourcesUtils.getDimenInPx(context, R.dimen.online_indicator_stroke_width);
        indicatorTextSizes[0] = ResourcesUtils.getDimenInDp(context, R.dimen.online_indicator_1_symbol_text_size);
        indicatorTextSizes[1] = ResourcesUtils.getDimenInDp(context, R.dimen.online_indicator_2_symbol_text_size);
        indicatorTextSizes[2] = ResourcesUtils.getDimenInDp(context, R.dimen.online_indicator_3_symbol_text_size);
        indicatorTextHeight = ResourcesUtils.getDimenInPx(context, R.dimen.online_indicator_3_symbol_text_size);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.online_indicator_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        GradientDrawable shapeDrawable = new GradientDrawable();
        shapeDrawable.setShape(GradientDrawable.OVAL);
        shapeDrawable.setColor(offlineBackgroundColor);
        setBackground(this, shapeDrawable);
        indicatorView = getChildAt(0);
        messagesCountView = (TextView) getChildAt(1);
    }

    public void setState(boolean online, int count) {
        GradientDrawable shapeDrawable = new GradientDrawable();
        shapeDrawable.setShape(GradientDrawable.OVAL);
        shapeDrawable.setColor(online ? onlineBackgroundColor : offlineBackgroundColor);
        shapeDrawable.setStroke(strokeWidth, onlineBackgroundColor);
        setBackground(indicatorView, shapeDrawable);

        String text;
        if (count == 0) {
            text = "";
        } else if (count < 10) {
            messagesCountView.setTextSize(indicatorTextSizes[0]);
            text = String.valueOf(count);
        } else if (count < 100) {
            messagesCountView.setTextSize(indicatorTextSizes[1]);
            text = String.valueOf(count);
        } else {
            messagesCountView.setTextSize(indicatorTextSizes[2]);
            text = "99+";
        }
        messagesCountView.setText(text);
        messagesCountView.setTextColor(online ? onlineColorText : offlineColorText);
    }

    private static void setBackground(View view, Drawable drawable) {
        if (BuildConfig.VERSION_CODE < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }
}
