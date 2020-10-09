package com.google.android.settings.aware;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.R;
import com.android.settings.core.SubSettingLauncher;

public class AwareSettingsDialogPreference extends AwareDialogPreferenceBase {
    public AwareSettingsDialogPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public boolean isAvailable() {
        return mHelper.isAvailable();
    }

    public void performEnabledClick() {
        super.performEnabledClick();
        SubSettingLauncher subSettingLauncher = new SubSettingLauncher(getContext());
        subSettingLauncher.setDestination(AwareSettings.class.getName());
        subSettingLauncher.setSourceMetricsCategory(getSourceMetricsCategory());
        subSettingLauncher.launch();
    }

    public void onPrepareDialogBuilder(AlertDialog.Builder builder, DialogInterface.OnClickListener onClickListener) {
        super.onPrepareDialogBuilder(builder, onClickListener);
        builder.setTitle((int) R.string.aware_settings_disabled_info_dialog_title);
        builder.setMessage((int) R.string.aware_settings_disabled_info_dialog_content);
        builder.setPositiveButton((int) R.string.nfc_how_it_works_got_it, (DialogInterface.OnClickListener) null);
        builder.setNegativeButton((CharSequence) "", (DialogInterface.OnClickListener) null);
    }
}
