package com.wootric.androidsdk.objects;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.wootric.androidsdk.Constants;
import com.wootric.androidsdk.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by maciejwitowski on 5/5/15.
 */
public class Settings implements Parcelable {

    private Long firstSurvey = 31L;
    private int adminPanelTimeDelay = Constants.NOT_SET;
    private LocalizedTexts localizedTexts;

    private WootricCustomMessage adminPanelCustomMessage;
    private WootricCustomMessage localCustomMessage;

    private int timeDelay = Constants.NOT_SET;

    private boolean surveyImmediately;
    private boolean skipFollowupScreenForPromoters;

    private Integer dailyResponseCap;
    private Integer registeredPercent;
    private Integer visitorPercent;
    private Integer resurveyThrottle;

    private String languageCode;
    private String productName;
    private String recommendTarget;
    private String facebookPageId;
    private String twitterPage;

    private WootricCustomThankYou customThankYou;

    private int surveyColor = Constants.NOT_SET;
    private int scoreColor = Constants.NOT_SET;
    private int thankYouButtonBackgroundColor = Constants.NOT_SET;
    private int socialSharingColor = Constants.NOT_SET;

    // Sets values from the argument settings only if they are not provided yet
    public void mergeWithSurveyServerSettings(Settings settings) {
        if(settings == null) {
            return;
        }

        this.adminPanelCustomMessage = settings.adminPanelCustomMessage;
        this.adminPanelTimeDelay = settings.adminPanelTimeDelay;
        this.localizedTexts = settings.localizedTexts;
    }

    public boolean firstSurveyDelayPassed(long timeFrom) {

        Boolean firstCriteria = timeFrom == Constants.NOT_SET;
        Boolean secondCriteria = new Date().getTime() - firstSurvey * Constants.DAY_IN_MILLIS >= timeFrom;

        if (firstCriteria || secondCriteria) {
            return true;
        }
        return  false;
    }

    public void setSurveyImmediately(boolean surveyImmediately) {
        this.surveyImmediately = surveyImmediately;
    }

    public boolean isSurveyImmediately() {
        return surveyImmediately;
    }

    public void setSkipFollowupScreenForPromoters(boolean skipFollowupScreenForPromoters) {
        this.skipFollowupScreenForPromoters = skipFollowupScreenForPromoters;
    }

    public boolean shouldSkipFollowupScreenForPromoters() {
        return skipFollowupScreenForPromoters;
    }

    public int getTimeDelayInMillis() {
        int time;

        if(timeDelay != Constants.NOT_SET) {
            time = timeDelay;
        } else if(adminPanelTimeDelay != Constants.NOT_SET) {
            time = adminPanelTimeDelay;
        } else {
            time = 0;
        }

        return time*1000;
    }

    public LocalizedTexts getLocalizedTexts() {
        return localizedTexts;
    }

    public String getNpsQuestion() {
        return localizedTexts.getNpsQuestion();
    }

    public String getAnchorLikely() {
        return localizedTexts.getAnchorLikely();
    }

    public String getAnchorNotLikely() {
        return localizedTexts.getAnchorNotLikely();
    }

    public String getBtnSubmit() {
        return localizedTexts.getSubmit().toUpperCase();
    }

    public String getBtnDismiss() {
        return localizedTexts.getDismiss().toUpperCase();
    }

    public String getBtnEditScore() {
        return localizedTexts.getEditScore().toUpperCase();
    }

    public String getFollowupQuestion(int score) {
        String followupQuestion = null;

        if(localCustomMessage != null) {
            followupQuestion = localCustomMessage.getFollowupQuestionForScore(score);
        }

        if(followupQuestion == null && adminPanelCustomMessage != null) {
            followupQuestion =  adminPanelCustomMessage.getFollowupQuestionForScore(score);
        }

        if(followupQuestion == null) {
            followupQuestion = localizedTexts.getFollowupQuestion();
        }

        return followupQuestion;
    }

    public String getFollowupPlaceholder(int score) {
        String followupPlaceholder = null;

        if(localCustomMessage != null) {
            followupPlaceholder = localCustomMessage.getPlaceholderForScore(score);
        }

        if(followupPlaceholder == null && adminPanelCustomMessage != null) {
            followupPlaceholder =  adminPanelCustomMessage.getPlaceholderForScore(score);
        }

        if(followupPlaceholder == null) {
            followupPlaceholder = localizedTexts.getFollowupPlaceholder();
        }

        return followupPlaceholder;
    }

    public String getCustomThankYouMessage(int score) {
        String thankYouMessage = null;

        if(customThankYou != null) {
            thankYouMessage = customThankYou.getTextForScore(score);
        }

        return thankYouMessage;
    }

    public String getThankYouMessage() {
        return localizedTexts.getFinalThankYou();
    }

    public String getThankYouLinkText(int score) {
        return (customThankYou == null) ?
                null : customThankYou.getLinkTextForScore(score);
    }

    public Uri getThankYouLinkUri(int score, String comment) {
        return (customThankYou == null) ?
                null :  customThankYou.getLinkUri(score, comment);
    }

    public boolean isThankYouActionConfigured(int score, String comment) {
        return customThankYou != null &&
                customThankYou.getLinkTextForScore(score) != null &&
                customThankYou.getLinkUri(score, comment) != null;
    }

    public String getSocialShareQuestion() {
        return localizedTexts.getSocialShareQuestion();
    }

    public String getSocialShareDecline() {
        return localizedTexts.getSocialShareDecline();
    }


    public String getFinalThankYou() {
        return localizedTexts.getFinalThankYou();
    }

    public void setTimeDelay(int timeDelay) {
        this.timeDelay = timeDelay;
    }

    public void setFirstSurveyDelay(long firstSurvey) {
        this.firstSurvey = firstSurvey;
    }

    public Long getFirstSurveyDelay() {
        return firstSurvey;
    }

    public void setLocalCustomMessage(WootricCustomMessage customMessage) {
        this.localCustomMessage = customMessage;
    }

    public WootricCustomMessage getLocalCustomMessage() {
        return localCustomMessage;
    }

    public void setLocalizedTexts(LocalizedTexts localizedTexts) {
        this.localizedTexts = localizedTexts;
    }

    public void setAdminPanelCustomMessage(WootricCustomMessage adminPanelCustomMessage) {
        this.adminPanelCustomMessage = adminPanelCustomMessage;
    }

    public WootricCustomMessage getAdminPanelCustomMessage() {
        return adminPanelCustomMessage;
    }

    public void setAdminPanelTimeDelay(int adminPanelTimeDelay) {
        this.adminPanelTimeDelay = adminPanelTimeDelay;
    }

    public int getAdminPanelTimeDelay() {
        return adminPanelTimeDelay;
    }

    public void setDailyResponseCap(int dailyResponseCap) {
        this.dailyResponseCap = dailyResponseCap;
    }

    public void setRegisteredPercent(Integer registeredPercent) {
        this.registeredPercent = registeredPercent;
    }

    public void setVisitorPercent(Integer visitorPercent) {
        this.visitorPercent = visitorPercent;
    }

    public void setResurveyThrottle(Integer resurveyThrottle) {
        this.resurveyThrottle = resurveyThrottle;
    }

    public Integer getDailyResponseCap() {
        return dailyResponseCap;
    }

    public Integer getRegisteredPercent() {
        return registeredPercent;
    }

    public Integer getVisitorPercent() {
        return visitorPercent;
    }

    public Integer getResurveyThrottle() {
        return resurveyThrottle;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRecommendTarget() {
        return recommendTarget;
    }

    public void setRecommendTarget(String recommendTarget) {
        this.recommendTarget = recommendTarget;
    }

    public String getFacebookPageId() {
        return facebookPageId;
    }

    public void setFacebookPageId(String facebookPageId) {
        this.facebookPageId = facebookPageId;
    }

    public String getTwitterPage() {
        return twitterPage;
    }

    public void setTwitterPage(String twitterPage) {
        this.twitterPage = twitterPage;
    }

    public void setCustomThankYou(WootricCustomThankYou customThankYou) {
        this.customThankYou = customThankYou;
    }

    public int getSurveyColor() {
        if (surveyColor != Constants.NOT_SET){
            return surveyColor;
        }
        return R.color.wootric_survey_layout_header_background;
    }

    public void setSurveyColor(int surveyColor) {
        this.surveyColor = surveyColor;
    }

    public int getScoreColor() {
        if (scoreColor != Constants.NOT_SET){
            return scoreColor;
        }
        return R.color.wootric_score_color;
    }

    public void setScoreColor(int scoreColor) {
        this.scoreColor = scoreColor;
    }

    public int getThankYouButtonBackgroundColor () {
        if (thankYouButtonBackgroundColor != Constants.NOT_SET) {
            return thankYouButtonBackgroundColor;
        }
        return R.color.wootric_score_color;
    }

    public void setThankYouButtonBackgroundColor (int thankYouButtonBackgroundColor) {
        this.thankYouButtonBackgroundColor = thankYouButtonBackgroundColor;
    }

    public int getSocialSharingColor () {
        if (socialSharingColor != Constants.NOT_SET) {
            return socialSharingColor;
        }
        return R.color.wootric_social;
    }

    public void setSocialSharingColor (int socialSharingColor) {
        this.socialSharingColor = socialSharingColor;
    }

    public Settings() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.firstSurvey);
        dest.writeInt(this.adminPanelTimeDelay);
        dest.writeParcelable(this.localizedTexts, 0);
        dest.writeParcelable(this.adminPanelCustomMessage, 0);
        dest.writeParcelable(this.localCustomMessage, 0);
        dest.writeInt(this.timeDelay);
        dest.writeByte(surveyImmediately ? (byte) 1 : (byte) 0);
        dest.writeValue(this.dailyResponseCap);
        dest.writeValue(this.registeredPercent);
        dest.writeValue(this.visitorPercent);
        dest.writeValue(this.resurveyThrottle);
        dest.writeString(this.languageCode);
        dest.writeString(this.productName);
        dest.writeString(this.recommendTarget);
        dest.writeString(this.facebookPageId);
        dest.writeString(this.twitterPage);
        dest.writeParcelable(this.customThankYou, 0);
    }

    private Settings(Parcel in) {
        this.firstSurvey = (Long) in.readValue(Long.class.getClassLoader());
        this.adminPanelTimeDelay = in.readInt();
        this.localizedTexts = in.readParcelable(LocalizedTexts.class.getClassLoader());
        this.adminPanelCustomMessage = in.readParcelable(WootricCustomMessage.class.getClassLoader());
        this.localCustomMessage = in.readParcelable(WootricCustomMessage.class.getClassLoader());
        this.timeDelay = in.readInt();
        this.surveyImmediately = in.readByte() != 0;
        this.dailyResponseCap = (Integer) in.readValue(Integer.class.getClassLoader());
        this.registeredPercent = (Integer) in.readValue(Integer.class.getClassLoader());
        this.visitorPercent = (Integer) in.readValue(Integer.class.getClassLoader());
        this.resurveyThrottle = (Integer) in.readValue(Integer.class.getClassLoader());
        this.languageCode = in.readString();
        this.productName = in.readString();
        this.recommendTarget = in.readString();
        this.facebookPageId = in.readString();
        this.twitterPage = in.readString();
        this.customThankYou = in.readParcelable(WootricCustomThankYou.class.getClassLoader());
    }

    public static final Creator<Settings> CREATOR = new Creator<Settings>() {
        public Settings createFromParcel(Parcel source) {
            return new Settings(source);
        }

        public Settings[] newArray(int size) {
            return new Settings[size];
        }
    };

    public static Settings fromJson(JSONObject settingsObject) throws JSONException {
        Settings settings = new Settings();
        settings.firstSurvey = settingsObject.optLong("first_survey");
        settings.timeDelay = settingsObject.optInt("time_delay");

        JSONObject localizedTextsJson = settingsObject.optJSONObject("localized_texts");
        settings.localizedTexts = LocalizedTexts.fromJson(localizedTextsJson);

        JSONObject customMessagesJson = settingsObject.optJSONObject("messages");
        settings.adminPanelCustomMessage = WootricCustomMessage.fromJson(customMessagesJson);

        return settings;
    }
}