package com.google.android.settings.aware;

import android.content.Context;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.search.BaseSearchIndexProvider;

public class SilenceGestureSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.silence_gesture_settings) {

        public boolean isPageSearchEnabled(Context context) {
            return FeatureFactory.getFactory(context).getAwareFeatureProvider().isSupported(context);
        }
    };

    public String getLogTag() {
        return "SilenceGestureSettings";
    }

    public int getMetricsCategory() {
        return 1625;
    }

    public int getPreferenceScreenResId() {
        return R.xml.silence_gesture_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
