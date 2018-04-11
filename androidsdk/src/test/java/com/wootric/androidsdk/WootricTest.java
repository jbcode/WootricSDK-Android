package com.wootric.androidsdk;

import android.app.Activity;

import com.wootric.androidsdk.network.WootricRemoteClient;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.objects.WootricCustomMessage;
import com.wootric.androidsdk.utils.PermissionsValidator;
import com.wootric.androidsdk.utils.PreferencesUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import static com.wootric.androidsdk.TestHelper.TEST_ACTIVITY;
import static junit.framework.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WootricTest {

    @Mock SurveyManager mockSurveyManager;
    @Mock SurveyValidator mockSurveyValidator;
    @Mock PreferencesUtils mockPreferencesUtils;
    @Mock PermissionsValidator mockPermissionsValidator;

    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String ACCOUNT_TOKEN = "account_token";

    @Before
    public void setUp() {
        Wootric.singleton = null;
        Wootric.init(TEST_ACTIVITY, CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN);
    }

    @Test public void fails_whenContextIsNull() throws Exception {
        try {
            Wootric.singleton = null;
            Wootric.init(null, CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN);
            fail("Null activity should throw exception");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test public void fails_whenClientIdIsNull() throws Exception {
        try {
            Wootric.singleton = null;
            Wootric.init(new FragmentActivity(), null, CLIENT_SECRET, ACCOUNT_TOKEN);
            fail("Null client id should throw exception");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test public void fails_whenClientSecretIsNull() throws Exception {
        try {
            Wootric.singleton = null;
            Wootric.init(new FragmentActivity(), CLIENT_ID, null, ACCOUNT_TOKEN);
            fail("Null client secret should throw exception");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test public void fails_whenAccountTokenIsNull() throws Exception {
        try {
            Wootric.singleton = null;
            Wootric.init(new FragmentActivity(), CLIENT_ID, CLIENT_SECRET, null);
            fail("Null account token should throw exception");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test public void inits_singleton() throws Exception {
        Wootric.singleton = null;
        Wootric wootric = Wootric.init(TEST_ACTIVITY, CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN);
        Wootric wootric_2 = Wootric.init(TEST_ACTIVITY, CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN);

        assertThat(wootric).isEqualTo(wootric_2);
    }

    @Test public void init_sets_endUser() throws Exception {
        assertThat(Wootric.singleton.endUser).isNotNull();
    }

    @Test public void init_sets_user() throws Exception {
        final User user = Wootric.singleton.user;
        assertThat(user.getClientId()).isEqualTo(CLIENT_ID);
        assertThat(user.getClientSecret()).isEqualTo(CLIENT_SECRET);
        assertThat(user.getAccountToken()).isEqualTo(ACCOUNT_TOKEN);
    }

    @Test public void init_sets_Settings() throws Exception {
        assertThat(Wootric.singleton.settings).isNotNull();
    }

    @Test public void setEndUserEmail() throws Exception {
        Wootric wootric = Wootric.singleton;
        wootric.setEndUserEmail("nps@example.com");

        assertThat(wootric.endUser.getEmailOrUnknown()).isEqualTo("nps@example.com");
    }

    @Test public void setEndUserExternalId() throws Exception {
        Wootric wootric = Wootric.singleton;
        wootric.setEndUserExternalId("a1b2c3d4");

        assertThat(wootric.endUser.getExternalId()).isEqualTo("a1b2c3d4");
    }

    @Test public void setEndUserPhoneNumber() throws Exception {
        Wootric wootric = Wootric.singleton;
        wootric.setEndUserPhoneNumber("+0123456789");

        assertThat(wootric.endUser.getPhoneNumber()).isEqualTo("+0123456789");
    }

    @Test
    public void setsEndUserProperties() {
        Wootric wootric = Wootric.singleton;
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("company", "Wootric");
        properties.put("type", "free");
        wootric.setProperties(properties);

        assertThat(wootric.endUser.getProperties()).isEqualTo(properties);
    }

    @Test public void setSurveyImmediately() throws Exception {
        Wootric wootric = Wootric.init(new FragmentActivity(), CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN);
        wootric.setSurveyImmediately(true);

        assertThat(wootric.settings.isSurveyImmediately()).isTrue();
    }

    @Test
    public void setsLocalCustomMessageInSettings() {
        WootricCustomMessage customMessage = new WootricCustomMessage();
        Wootric wootric = Wootric.singleton;

        wootric.setCustomMessage(customMessage);
        assertThat(wootric.settings.getLocalCustomMessage()).isEqualTo(customMessage);
    }

    @Test
    public void setsDailyResponseCapInSettings() {
        Wootric wootric = Wootric.singleton;
        wootric.setDailyResponseCap(20);
        assertThat(wootric.settings.getDailyResponseCap()).isEqualTo(20);
    }

    @Test
    public void setsRegisteredPercentInSettings() {
        Wootric wootric = Wootric.singleton;
        wootric.setRegisteredPercent(20);
        assertThat(wootric.settings.getRegisteredPercent()).isEqualTo(20);
    }

    @Test
    public void setsVisitorPercentInSettings() {
        Wootric wootric = Wootric.singleton;
        wootric.setVisitorPercent(20);
        assertThat(wootric.settings.getVisitorPercent()).isEqualTo(20);
    }

    @Test
    public void setsResurveyThrottleInSettings() {
        Wootric wootric = Wootric.singleton;
        wootric.setResurveyThrottle(20);
        assertThat(wootric.settings.getResurveyThrottle()).isEqualTo(20);
    }

    @Test
    public void setsLanguageCodeInSettings() {
        Wootric wootric = Wootric.singleton;
        wootric.setLanguageCode("PL");
        assertThat(wootric.settings.getLanguageCode()).isEqualTo("PL");
    }

    @Test
    public void setsProductNameInSettings() {
        Wootric wootric = Wootric.singleton;
        wootric.setProductName("Wootric");
        assertThat(wootric.settings.getProductName()).isEqualTo("Wootric");
    }

    @Test
    public void setsRecommendTargetInSettings() {
        Wootric wootric = Wootric.singleton;
        wootric.setRecommendTarget("My Friend");
        assertThat(wootric.settings.getRecommendTarget()).isEqualTo("My Friend");
    }

    @Test
    public void setsFacebookPage() {
        Wootric wootric = Wootric.singleton;
        wootric.setFacebookPageId("https://www.facebook.com/test");
        assertThat(wootric.settings.getFacebookPageId()).isEqualTo("https://www.facebook.com/test");
    }

    @Test
    public void setsTweeterPage() {
        Wootric wootric = Wootric.singleton;
        wootric.setTwitterPage("https://www.tweeter.com/test");
        assertThat(wootric.settings.getTwitterPage()).isEqualTo("https://www.tweeter.com/test");
    }

    @Test public void survey_startsSurvey() throws Exception {
        Wootric.singleton = null;
        Wootric wootric = spy(Wootric.init(TEST_ACTIVITY, CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN));

        doReturn(mockSurveyValidator).when(wootric).buildSurveyValidator(eq(wootric.user),
                eq(wootric.endUser), eq(wootric.settings), any(WootricRemoteClient.class), any(PreferencesUtils.class));

        doReturn(mockSurveyManager).when(wootric).buildSurveyManager(eq(wootric.weakActivity.get()),
                any(WootricRemoteClient.class), eq(wootric.user),
                eq(wootric.endUser), eq(wootric.settings),
                any(PreferencesUtils.class), eq(mockSurveyValidator));

        wootric.permissionsValidator = mockPermissionsValidator;
        doReturn(true).when(wootric.permissionsValidator).check();

        wootric.survey();
        verify(mockSurveyManager, times(1)).start();
        assertThat(wootric.surveyInProgress).isTrue();
    }

    @Test
    public void doesNotStartSurvey_whenSurveyInProgress() {
        Wootric.singleton = null;
        Wootric wootric = spy(Wootric.init(TEST_ACTIVITY, CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN));

        doReturn(mockSurveyValidator).when(wootric).buildSurveyValidator(eq(wootric.user),
                eq(wootric.endUser), eq(wootric.settings), any(WootricRemoteClient.class), any(PreferencesUtils.class));

        doReturn(mockSurveyManager).when(wootric).buildSurveyManager(eq(wootric.weakActivity.get()),
                any(WootricRemoteClient.class), eq(wootric.user),
                eq(wootric.endUser), eq(wootric.settings),
                any(PreferencesUtils.class), eq(mockSurveyValidator));

        wootric.permissionsValidator = mockPermissionsValidator;
        doReturn(true).when(wootric.permissionsValidator).check();
        wootric.surveyInProgress = true;

        wootric.survey();

        verify(mockSurveyManager, times(0)).start();
    }

    @Test
    public void doesNotStartSurvey_whenPermissionsValidatorChecksReturnsFalse() {
        Wootric.singleton = null;
        Wootric wootric = spy(Wootric.init(TEST_ACTIVITY, CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN));

        doReturn(mockSurveyValidator).when(wootric).buildSurveyValidator(eq(wootric.user),
                eq(wootric.endUser), eq(wootric.settings), any(WootricRemoteClient.class), any(PreferencesUtils.class));

        doReturn(mockSurveyManager).when(wootric).buildSurveyManager(eq(wootric.weakActivity.get()),
                any(WootricRemoteClient.class), eq(wootric.user),
                eq(wootric.endUser), eq(wootric.settings),
                any(PreferencesUtils.class), eq(mockSurveyValidator));

        wootric.permissionsValidator = mockPermissionsValidator;
        doReturn(false).when(wootric.permissionsValidator).check();
        wootric.surveyInProgress = false;

        wootric.survey();

        verify(mockSurveyManager, times(0)).start();
    }

    @Test
    public void whenEndUserWasSurveyed_updatesLastSurveyedAndResetsTheSingleton() {
        Wootric wootric = Wootric.singleton;
        wootric.preferencesUtils = mockPreferencesUtils;

        Wootric.notifySurveyFinished(true, true, 0);
        assertThat(Wootric.singleton).isNull();
        verify(wootric.preferencesUtils, times(1)).touchLastSurveyed(true, 0);
    }

    @Test
    public void whenEndUserWasNotSurveyed_doesNotpdateLastSurveyed() {
        Wootric wootric = Wootric.singleton;
        wootric.preferencesUtils = mockPreferencesUtils;

        Wootric.notifySurveyFinished(false, false, 0);
        assertThat(Wootric.singleton).isNull();
        verify(wootric.preferencesUtils, never()).touchLastSurveyed(false, 0);
    }
}
