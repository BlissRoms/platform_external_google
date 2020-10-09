package com.google.android.settings.aware;

import android.content.ContentResolver;
import android.content.Context;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.utils.CandidateInfoExtra;
import com.android.settings.widget.RadioButtonPickerFragment;
import com.android.settingslib.widget.CandidateInfo;
import com.android.settingslib.widget.RadioButtonPreference;
import java.util.ArrayList;
import java.util.List;

public class AwareDisplaySettings extends RadioButtonPickerFragment {
    @VisibleForTesting
    static final String KEY_ALWAYS_ON = "aware_always_on";
    @VisibleForTesting
    static final String KEY_OFF = "aware_wake_off";
    @VisibleForTesting
    static final String KEY_WAKE_DISPLAY = "aware_wake_display";
    private static final int MY_USER = UserHandle.myUserId();
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.aware_wake_display_settings) {

        protected boolean isPageSearchEnabled(Context context) {
            return SystemProperties.getBoolean("ro.vendor.aware_available", false);
        }
    };
    private AmbientDisplayConfiguration mConfig;
    private AwareHelper mHelper;

    public int getMetricsCategory() {
        return 1750;
    }

    protected int getPreferenceScreenResId() {
        return R.xml.aware_wake_display_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        mHelper = new AwareHelper(context);
        mConfig = new AmbientDisplayConfiguration(context);
        setIllustration(R.raw.aware_display, R.drawable.aware_display);
    }

    public RadioButtonPreference bindPreference(RadioButtonPreference radioButtonPreference, String str, CandidateInfo candidateInfo, String str2) {
        if (candidateInfo instanceof CandidateInfoExtra) {
            radioButtonPreference.setSummary(((CandidateInfoExtra) candidateInfo).loadSummary());
            radioButtonPreference.setAppendixVisibility(8);
        }
        super.bindPreference(radioButtonPreference, str, candidateInfo, str2);
        return radioButtonPreference;
    }

    protected List<? extends CandidateInfo> getCandidates() {
        Context context = getContext();
        ArrayList arrayList = new ArrayList();
        if (mHelper.isSupported()) {
            arrayList.add(new CandidateInfoExtra(context.getText(R.string.aware_wake_display_title), context.getText(R.string.aware_wake_display_summary), KEY_WAKE_DISPLAY, mHelper.isGestureConfigurable()));
        }
        if (mConfig.alwaysOnAvailableForUser(MY_USER)) {
            arrayList.add(new CandidateInfoExtra(context.getText(R.string.doze_always_on_title), context.getText(R.string.doze_always_on_summary), KEY_ALWAYS_ON, true));
        }
        arrayList.add(new CandidateInfoExtra(context.getText(R.string.switch_off_text), (CharSequence) null, KEY_OFF, true));
        return arrayList;
    }

    protected String getDefaultKey() {
        boolean wakeDisplayGestureEnabled = mConfig.wakeDisplayGestureEnabled(MY_USER);
        boolean alwaysOnEnabled = mConfig.alwaysOnEnabled(MY_USER);
        if (!wakeDisplayGestureEnabled || !mHelper.isGestureConfigurable() || !alwaysOnEnabled) {
            return alwaysOnEnabled ? KEY_ALWAYS_ON : KEY_OFF;
        }
        return KEY_WAKE_DISPLAY;
    }

    protected boolean setDefaultKey(String str) {
        char c;
        ContentResolver contentResolver = getContext().getContentResolver();
        int hashCode = str.hashCode();
        if (hashCode != -2133849746) {
            if (hashCode != -1652972184) {
                if (hashCode == 598899989 && str.equals(KEY_OFF)) {
                    c = 2;
                    if (c != 0) {
                        mHelper.writeFeatureEnabled("doze_always_on", true);
                        Settings.Secure.putInt(contentResolver, "doze_always_on", 1);
                        mHelper.writeFeatureEnabled("doze_wake_display_gesture", true);
                        Settings.Secure.putInt(contentResolver, "doze_wake_display_gesture", 1);
                    } else if (c == 1) {
                        mHelper.writeFeatureEnabled("doze_always_on", true);
                        Settings.Secure.putInt(contentResolver, "doze_always_on", 1);
                        mHelper.writeFeatureEnabled("doze_wake_display_gesture", false);
                        Settings.Secure.putInt(contentResolver, "doze_wake_display_gesture", 0);
                    } else if (c == 2) {
                        mHelper.writeFeatureEnabled("doze_always_on", false);
                        Settings.Secure.putInt(contentResolver, "doze_always_on", 0);
                        mHelper.writeFeatureEnabled("doze_wake_display_gesture", false);
                        Settings.Secure.putInt(contentResolver, "doze_wake_display_gesture", 0);
                    }
                    return true;
                }
            } else if (str.equals(KEY_WAKE_DISPLAY)) {
                c = 0;
                if (c != 0) {
                }
                return true;
            }
        } else if (str.equals(KEY_ALWAYS_ON)) {
            c = 1;
            if (c != 0) {
            }
            return true;
        }
        c = 65535;
        if (c != 0) {
        }
        return true;
    }
}
