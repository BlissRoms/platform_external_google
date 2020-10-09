package com.google.android.settings.aware;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.slices.SliceBackgroundWorker;

public class WakeScreenDialogGesturePreferenceController extends AwareGesturePreferenceController {
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

    public WakeScreenDialogGesturePreferenceController(Context context, String str) {
        super(context, str);
    }

    protected CharSequence getGestureSummary() {
        return mContext.getText(isGestureEnabled() ? R.string.ambient_display_wake_screen_summary_on : R.string.gesture_setting_off);
    }

    private boolean isGestureEnabled() {
        if (!mFeatureProvider.isEnabled(mContext) || Settings.Secure.getInt(mContext.getContentResolver(), "doze_wake_screen_gesture", 1) != 1) {
            return false;
        }
        return true;
    }
}
