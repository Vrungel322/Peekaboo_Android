package com.peekaboo.presentation.app;

import android.content.Intent;

/**
 * Created by arkadii on 11/22/16.
 */

public class ActivityResult {
    public final int resultCode;
    public final int requestCode;
    public final Intent data;

    public ActivityResult(int resultCode, int requestCode, Intent data) {
        this.resultCode = resultCode;
        this.requestCode = requestCode;
        this.data = data;
    }
}
