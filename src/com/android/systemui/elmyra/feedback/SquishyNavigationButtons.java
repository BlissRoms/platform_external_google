package com.google.android.systemui.elmyra.feedback;

import android.content.ContentResolver;
import android.content.Context;
import android.os.PowerManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.view.View;

import com.android.internal.utils.ActionHandler;
import com.android.internal.utils.Config.ActionConfig;
import com.android.systemui.SysUiServiceProvider;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.navigation.Navigator;
import com.android.systemui.statusbar.phone.NavigationBarView;

import java.util.Arrays;
import java.util.List;

public class SquishyNavigationButtons extends NavigationBarEffect {

    private final KeyguardViewMediator mKeyguardViewMediator;
    private final SquishyViewController mViewController;

    private ContentResolver mResolver;
    private PowerManager mPm;

    public SquishyNavigationButtons(Context context) {
        super(context);
        mResolver = context.getContentResolver();
        mPm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mViewController = new SquishyViewController(context);
        mKeyguardViewMediator = (KeyguardViewMediator) SysUiServiceProvider.getComponent(
            context, KeyguardViewMediator.class);
        mPm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    }

    protected List<FeedbackEffect> findFeedbackEffects(Navigator navigationBarView) {
        mViewController.clearViews();
        // Back button views
        List views = navigationBarView.getBackButton().getViews();
        views.forEach(v -> mViewController.addLeftView((View) v));
        // Recent button views
        views = navigationBarView.getRecentsButton().getViews();
        views.forEach(v -> mViewController.addRightView((View) v));

        return Arrays.asList(new FeedbackEffect[]{mViewController});
    }

    @Override
    protected boolean isActiveFeedbackEffect(FeedbackEffect feedbackEffect) {
        return !mPm.isPowerSaveMode() && !isSqueezeTurnedOff()
                && mPm.isScreenOn() && !mKeyguardViewMediator.isShowingAndNotOccluded();
    }

    @Override
    protected boolean validateFeedbackEffects(List<FeedbackEffect> list) {
        return mViewController.isAttachedToWindow();
    }


    private boolean isSqueezeTurnedOff() {
        String actionConfig = Settings.Secure.getStringForUser(mResolver,
                Settings.Secure.SQUEEZE_SELECTION_SMART_ACTIONS, UserHandle.USER_CURRENT);
        String action = ActionConfig.getActionFromDelimitedString(getContext(), actionConfig,
                ActionHandler.SYSTEMUI_TASK_NO_ACTION);
        String longActionConfig = Settings.Secure.getStringForUser(mResolver,
                Settings.Secure.LONG_SQUEEZE_SELECTION_SMART_ACTIONS, UserHandle.USER_CURRENT);
        String longAction = ActionConfig.getActionFromDelimitedString(getContext(), longActionConfig,
                ActionHandler.SYSTEMUI_TASK_NO_ACTION);
        return action.equals(ActionHandler.SYSTEMUI_TASK_NO_ACTION)
                && longAction.equals(ActionHandler.SYSTEMUI_TASK_NO_ACTION);
    }
}
