package com.google.android.settings.aware;

import android.content.Context;
import android.content.IntentFilter;
import com.android.settings.R;
import com.android.settings.slices.SliceBackgroundWorker;

public class SkipGestureFooterPreferenceController extends AwareFooterPreferenceController {
    public void copy() {
        super.copy();
    }

    public Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public int getText() {
        return R.string.gesture_aware_footer;
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

    public int getAvailabilityStatus() {
        return super.getAvailabilityStatus();
    }

    public CharSequence getSummary() {
        return super.getSummary();
    }

    public SkipGestureFooterPreferenceController(Context context, String str) {
        super(context, str);
    }
}
