package com.google.android.settings.aware;

import android.content.Context;
import android.util.AttributeSet;
import com.android.settings.R;

public class TapGestureDialogPreference extends AwareGestureDialogPreference {
    /* access modifiers changed from: package-private */
    public int getDialogDisabledMessage() {
        return R.string.gesture_aware_disabled_info_dialog_content;
    }

    public int getGestureDialogMessage() {
        return R.string.gesture_aware_off_dialog_content;
    }

    public int getGestureDialogTitle() {
        return R.string.gesture_aware_off_dialog_title;
    }

    public TapGestureDialogPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public String getDestination() {
        return TapGestureSettings.class.getName();
    }
}
