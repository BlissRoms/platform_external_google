package com.google.android.settings.aware;

import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.google.android.settings.aware.AwareHelper;

public abstract class AwareTogglePreferenceController extends TogglePreferenceController implements LifecycleObserver, OnStart, OnStop, AwareHelper.Callback {
    protected static final int OFF = 0;
    protected static final int ON = 1;
    protected final AwareHelper mHelper;
    protected SwitchPreference mPreference;

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

    public boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AwareTogglePreferenceController(Context context, String str) {
        super(context, str);
        mHelper = new AwareHelper(context);
    }

    public int getAvailabilityStatus() {
        return mHelper.isGestureConfigurable() ? 0 : 5;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        mPreference = (SwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        refreshSummary(preference);
        int availabilityStatus = getAvailabilityStatus();
        boolean z = true;
        if (!(availabilityStatus == 0 || availabilityStatus == 1)) {
            z = false;
        }
        preference.setEnabled(z);
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
