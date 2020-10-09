package com.google.android.settings.aware;

import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.google.android.settings.aware.AwareHelper;

public class AwareSettingsFooterPreferenceController extends BasePreferenceController implements LifecycleObserver, OnStart, OnStop, AwareHelper.Callback {
    protected final AwareHelper mHelper = new AwareHelper(mContext);
    private Preference mPreference;
    public void copy() {
        super.copy();
    }

    public Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public boolean isSliceable() {
        return super.isSliceable();
    }

    public boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AwareSettingsFooterPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return mHelper.isSupported() ? 0 : 3;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public CharSequence getSummary() {
        boolean isBatterySaverModeOn = mHelper.isBatterySaverModeOn();
        boolean isAirplaneModeOn = mHelper.isAirplaneModeOn();
        return mContext.getText((!isBatterySaverModeOn || !isAirplaneModeOn) ? isBatterySaverModeOn ? R.string.aware_footer_when_batterysaver_on : isAirplaneModeOn ? R.string.aware_footer_when_airplane_on : R.string.aware_settings_description : R.string.aware_footer_when_airplane_batterysaver_on);
    }

    public void onStart() {
        mHelper.register(this);
    }

    public void onStop() {
        mHelper.unregister();
    }

    public void onChange(Uri uri) {
        updateState(mPreference);
    }
}
