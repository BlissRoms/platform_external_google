package com.google.android.settings.aware;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.slices.SliceBackgroundWorker;

public class AwareSilencePreferenceController extends AwareBasePreferenceController {
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

    public AwareSilencePreferenceController(Context context, String str) {
        super(context, str);
    }

    public CharSequence getSummary() {
        return mContext.getText(isSilenceGestureEnabled() ? R.string.gesture_silence_on_summary : R.string.gesture_setting_off);
    }

    private boolean isSilenceGestureEnabled() {
        if (!mFeatureProvider.isEnabled(mContext) || Settings.Secure.getInt(mContext.getContentResolver(), "silence_gesture", 1) != 1) {
            return false;
        }
        return true;
    }
}
