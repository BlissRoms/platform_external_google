package com.google.android.settings.aware;

import android.app.Activity;
import android.content.Context;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.Bundle;
import android.os.UserHandle;
import com.android.settings.core.SubSettingLauncher;

public class WakeScreenSuggestionActivity extends Activity {
    public static boolean isSuggestionComplete(Context context) {
        return new AmbientDisplayConfiguration(context).wakeLockScreenGestureEnabled(UserHandle.myUserId()) || !new AwareHelper(context).isGestureConfigurable();
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SubSettingLauncher subSettingLauncher = new SubSettingLauncher(this);
        subSettingLauncher.setDestination(WakeScreenGestureSettings.class.getName());
        subSettingLauncher.setSourceMetricsCategory(0);
        subSettingLauncher.launch();
        finish();
    }
}
