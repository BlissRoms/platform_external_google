package com.google.android.settings.aware;

import android.content.Context;
import android.content.IntentFilter;
import android.os.SystemProperties;
import com.android.settings.R;
import com.android.settings.slices.SliceBackgroundWorker;

public abstract class AwareGesturePreferenceController extends AwareBasePreferenceController {
    public void copy() {
        super.copy();
    }

    public Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public abstract CharSequence getGestureSummary();

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

    public AwareGesturePreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        if (!SystemProperties.getBoolean("ro.vendor.aware_available", false)) {
            return 3;
        }
        if (mHelper.isAirplaneModeOn() || mHelper.isBatterySaverModeOn()) {
            return 5;
        }
        return 0;
    }

    public CharSequence getSummary() {
        boolean isBatterySaverModeOn = mHelper.isBatterySaverModeOn();
        boolean isAirplaneModeOn = mHelper.isAirplaneModeOn();
        if (!isBatterySaverModeOn && !isAirplaneModeOn) {
            return getGestureSummary();
        }
        return mContext.getText((!isBatterySaverModeOn || !isAirplaneModeOn) ? isBatterySaverModeOn ? R.string.aware_summary_when_batterysaver_on : isAirplaneModeOn ? R.string.aware_summary_when_airplane_on : 0 : R.string.aware_summary_when_airplane_batterysaver_on);
    }
}
