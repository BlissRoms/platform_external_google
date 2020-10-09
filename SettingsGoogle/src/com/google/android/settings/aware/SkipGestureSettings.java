package com.google.android.settings.aware;

import android.content.Context;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.search.BaseSearchIndexProvider;

public class SkipGestureSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.skip_gesture_settings) {

        public boolean isPageSearchEnabled(Context context) {
            return FeatureFactory.getFactory(context).getAwareFeatureProvider().isSupported(context);
        }
    };

    public String getLogTag() {
        return "SkipGestureSettings";
    }

    public int getMetricsCategory() {
        return 1624;
    }


    public int getPreferenceScreenResId() {
        return R.xml.skip_gesture_settings;
    }
}
