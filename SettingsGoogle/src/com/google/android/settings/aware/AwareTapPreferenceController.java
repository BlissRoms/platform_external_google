package com.google.android.settings.aware;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.slices.SliceBackgroundWorker;

public class AwareTapPreferenceController extends AwareBasePreferenceController {
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

    public AwareTapPreferenceController(Context context, String str) {
        super(context, str);
    }

    public CharSequence getSummary() {
        return mContext.getText(isTapGestureEnabled() ? R.string.gesture_tap_on_summary : R.string.gesture_setting_off);
    }

    public int getAvailabilityStatus() {
        if (AwareHelper.isTapAvailableOnTheDevice()) {
            return super.getAvailabilityStatus();
        }
        return 3;
    }

    private boolean isTapGestureEnabled() {
        return mFeatureProvider.isEnabled(mContext) && Settings.Secure.getInt(mContext.getContentResolver(), "tap_gesture", 0) == 1;
    }
}
