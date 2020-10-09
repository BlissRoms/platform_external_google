package com.google.android.settings.aware;

import android.content.Context;
import android.content.IntentFilter;
import android.os.SystemProperties;
import com.android.settings.aware.AwareFeatureProvider;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;

public class AwareGesturesCategoryPreferenceController extends BasePreferenceController {
    final Context mContext;
    final AwareFeatureProvider mFeatureProvider;

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

    public AwareGesturesCategoryPreferenceController(Context context, String str) {
        super(context, str);
        mContext = context;
        mFeatureProvider = FeatureFactory.getFactory(context).getAwareFeatureProvider();
    }

    public int getAvailabilityStatus() {
        return SystemProperties.getBoolean("ro.vendor.aware_available", false) ? 0 : 3;
    }
}
