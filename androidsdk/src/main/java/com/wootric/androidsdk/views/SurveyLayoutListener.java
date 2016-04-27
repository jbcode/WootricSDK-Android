package com.wootric.androidsdk.views;

import com.wootric.androidsdk.objects.Settings;

/**
 * Created by maciejwitowski on 9/30/15.
 */
public interface SurveyLayoutListener extends ThankYouLayoutListener {

    void onSurveySubmit(int score, String text);

    void onDismissClick();
}