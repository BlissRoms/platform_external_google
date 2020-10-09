package com.google.android.settings.aware;

import android.content.Context;
import android.content.IntentFilter;
import com.android.settings.aware.AwareFeatureProvider;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.widget.PreferenceCategoryController;

public class AwarePreferenceCategoryController extends PreferenceCategoryController {
    private final AwareFeatureProvider mFeatureProvider;

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

    public AwarePreferenceCategoryController(Context context, String str) {
        super(context, str);
        mFeatureProvider = FeatureFactory.getFactory(context).getAwareFeatureProvider();
    }

    public int getAvailabilityStatus() {
        return mFeatureProvider.isSupported(mContext) ? 0 : 3;
    }
}
