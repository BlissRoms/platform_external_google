package com.google.android.settings.aware;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.view.View;
import com.android.settings.R;
import com.android.settings.aware.AwareFeatureProvider;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.utils.AnnotationSpan;

abstract class AwareFooterPreferenceController extends BasePreferenceController {
    public static final String TIPS_LINK = "tips_link";
    private final AwareFeatureProvider mFeatureProvider = FeatureFactory.getFactory(mContext).getAwareFeatureProvider();

    public void copy() {
        super.copy();
    }

    public Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    private abstract int getText();

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

    public AwareFooterPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return mFeatureProvider.isSupported(mContext) ? 0 : 3;
    }

    public CharSequence getSummary() {
        AnnotationSpan.LinkInfo linkInfo = getLinkInfo();
        AnnotationSpan.LinkInfo tipsLinkInfo = getTipsLinkInfo();
        CharSequence text = mContext.getText(getText());
        if (linkInfo != null) {
            text = AnnotationSpan.linkify(text, linkInfo);
        }
        if (tipsLinkInfo == null) {
            return text;
        }
        return AnnotationSpan.linkify(text, tipsLinkInfo);
    }

    private AnnotationSpan.LinkInfo getLinkInfo() {
        return new AnnotationSpan.LinkInfo("link", new View.OnClickListener() {
            public final void onClick(View view) {
                AwareFooterPreferenceController.lambda$getLinkInfo$0$AwareFooterPreferenceController(view);
            }
        });
    }

    /* access modifiers changed from: private */
    /* renamed from: lambda$getLinkInfo$0 */
    public /* synthetic */ void lambda$getLinkInfo$0$AwareFooterPreferenceController(View view) {
        SubSettingLauncher subSettingLauncher = new SubSettingLauncher(mContext);
        subSettingLauncher.setDestination(AwareSettings.class.getName());
        subSettingLauncher.setSourceMetricsCategory(getMetricsCategory());
        subSettingLauncher.launch();
    }

    /* access modifiers changed from: protected */
    public AnnotationSpan.LinkInfo getTipsLinkInfo() {
        return new AnnotationSpan.LinkInfo(TIPS_LINK, new View.OnClickListener() {
            public final void onClick(View view) {
                AwareFooterPreferenceController.lambda$getTipsLinkInfo$1$AwareFooterPreferenceController(view);
            }
        });
    }

    /* access modifiers changed from: private */
    /* renamed from: lambda$getTipsLinkInfo$1 */
    public /* synthetic */ void lambda$getTipsLinkInfo$1$AwareFooterPreferenceController(View view) {
        mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(mContext.getString(R.string.tips_help_url_gesture))));
    }
}
