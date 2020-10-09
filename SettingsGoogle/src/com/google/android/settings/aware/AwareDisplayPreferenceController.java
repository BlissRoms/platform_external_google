package com.google.android.settings.aware;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.display.AmbientDisplayConfiguration;
import android.net.Uri;
import android.os.SystemProperties;
import android.os.UserHandle;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.google.android.settings.aware.AwareHelper;

public class AwareDisplayPreferenceController extends BasePreferenceController implements LifecycleObserver, OnStart, OnStop, AwareHelper.Callback {
    private static final int MY_USER = UserHandle.myUserId();
    private static final String PROP_AWARE_AVAILABLE = "ro.vendor.aware_available";
    private final AmbientDisplayConfiguration mConfig;
    private final AwareHelper mHelper;
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

    public AwareDisplayPreferenceController(Context context, String str) {
        super(context, str);
        mHelper = new AwareHelper(context);
        mConfig = new AmbientDisplayConfiguration(context);
    }

    public int getAvailabilityStatus() {
        boolean alwaysOnAvailable = mConfig.alwaysOnAvailable();
        boolean z = SystemProperties.getBoolean(PROP_AWARE_AVAILABLE, false);
        if (alwaysOnAvailable || z) {
            return 0;
        }
        return 3;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public CharSequence getSummary() {
        boolean wakeDisplayGestureEnabled = mConfig.wakeDisplayGestureEnabled(MY_USER);
        boolean alwaysOnEnabled = mConfig.alwaysOnEnabled(MY_USER);
        if (wakeDisplayGestureEnabled && mHelper.isGestureConfigurable()) {
            return mContext.getText(R.string.aware_wake_display_title);
        }
        if (alwaysOnEnabled) {
            return mContext.getText(R.string.doze_always_on_title);
        }
        return mContext.getText(R.string.switch_off_text);
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
