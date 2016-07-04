package com.peekaboo.presentation.animation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.presentation.activities.LogInActivity;

/**
 * Created by rohit on 22/7/15.
 */
public class DepthAnimation extends BaseAppIntro {

    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(SampleSlide.newInstance(R.layout.intro));
        addSlide(SampleSlide.newInstance(R.layout.intro2));
        addSlide(SampleSlide.newInstance(R.layout.intro3));
        addSlide(SampleSlide.newInstance(R.layout.intro4));

        setDepthAnimation();
    }

    private void loadLogInActivity(){
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSkipPressed() {
        finish();
        loadLogInActivity();
        Toast.makeText(getApplicationContext(), getString(R.string.skip), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNextPressed() {
        Toast.makeText(getApplicationContext(), "Next", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDonePressed() {
        finish();
        loadLogInActivity();
    }

    @Override
    public void onSlideChanged() {

    }

    public void getStarted(View v){
        finish();
        loadLogInActivity();
    }
}
