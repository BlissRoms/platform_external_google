package com.google.android.settings.aware;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.CustomDialogPreferenceCompat;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.google.android.settings.aware.AwareHelper;

public class AwareDialogPreferenceBase extends CustomDialogPreferenceCompat {
    protected AwareHelper mHelper;
    private View mInfoIcon;
    private MetricsFeatureProvider mMetricsFeatureProvider;
    private View mSummary;
    private View mTitle;

    protected boolean isAvailable() {
        return false;
    }

    public AwareDialogPreferenceBase(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init();
    }

    public AwareDialogPreferenceBase(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public AwareDialogPreferenceBase(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        mTitle = preferenceViewHolder.findViewById(16908310);
        mSummary = preferenceViewHolder.findViewById(16908304);
        mInfoIcon = preferenceViewHolder.findViewById(R.id.info_button);
        updatePreference();
    }

    public void performClick() {
        if (isAvailable()) {
            performEnabledClick();
        } else {
            super.performClick();
        }
    }

    protected void updatePreference() {
        View view = mTitle;
        if (view != null) {
            view.setEnabled(isAvailable());
        }
        View view2 = mSummary;
        if (view2 != null) {
            view2.setEnabled(isAvailable());
        }
        if (mInfoIcon != null) {
            int i = 0;
            boolean z = isAvailable() || mHelper.isAirplaneModeOn() || mHelper.isBatterySaverModeOn();
            View view3 = mInfoIcon;
            if (z) {
                i = 8;
            }
            view3.setVisibility(i);
        }
    }

    protected void performEnabledClick() {
    }

    private void init() {
        Context context = getContext();
        mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
        setWidgetLayoutResource(R.layout.preference_widget_info);
        AwareHelper awareHelper = new AwareHelper(context);
        mHelper = awareHelper;
        awareHelper.register(new AwareHelper.Callback() {
            public void onChange(Uri uri) {
                AwareDialogPreferenceBase.updatePreference();
                CharSequence summary = AwareDialogPreferenceBase.getSummary();
                if (!TextUtils.isEmpty(summary)) {
                    AwareDialogPreferenceBase.setSummary(summary);
                }
            }
        });
    }
}
