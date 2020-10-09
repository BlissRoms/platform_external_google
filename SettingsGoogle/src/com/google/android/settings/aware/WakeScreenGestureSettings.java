package com.google.android.settings.aware;

import android.content.Context;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.search.BaseSearchIndexProvider;

public class WakeScreenGestureSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.wake_screen_gesture_settings) {
        protected boolean isPageSearchEnabled(Context context) {
            return FeatureFactory.getFactory(context).getAwareFeatureProvider().isSupported(context);
        }
    };

    protected String getLogTag() {
        return "WakeScreenGestureSettings";
    }

    public int getMetricsCategory() {
        return 1570;
    }

    protected int getPreferenceScreenResId() {
        return R.xml.wake_screen_gesture_settings;
    }
}
