package com.google.android.settings.aware;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.UserHandle;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.slices.SliceBackgroundWorker;

public class WakeDisplayPreferenceController extends AwareTogglePreferenceController {
    private AmbientDisplayConfiguration mAmbientConfig;
    private final int mUserId = UserHandle.myUserId();

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

    public WakeDisplayPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return (!mHelper.isGestureConfigurable() || !getAmbientConfig().alwaysOnEnabled(mUserId)) ? 5 : 0;
    }

    public boolean isChecked() {
        return getAmbientConfig().wakeDisplayGestureEnabled(mUserId);
    }

    public boolean setChecked(boolean z) {
        mHelper.writeFeatureEnabled("doze_wake_display_gesture", z);
        Settings.Secure.putInt(mContext.getContentResolver(), "doze_wake_display_gesture", z ? 1 : 0);
        return true;
    }

    public CharSequence getSummary() {
        return mContext.getText(getAmbientConfig().alwaysOnEnabled(mUserId) ? R.string.aware_wake_display_summary : R.string.aware_wake_display_summary_aod_off);
    }

    public void setConfig(AmbientDisplayConfiguration ambientDisplayConfiguration) {
        mAmbientConfig = ambientDisplayConfiguration;
    }

    private AmbientDisplayConfiguration getAmbientConfig() {
        if (mAmbientConfig == null) {
            mAmbientConfig = new AmbientDisplayConfiguration(mContext);
        }
        return mAmbientConfig;
    }
}
