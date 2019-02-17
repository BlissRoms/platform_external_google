package com.google.android.systemui.elmyra.actions;

import android.content.Context;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.UserHandle;
import android.provider.MediaStore;
import android.provider.Settings;

import com.android.internal.utils.ActionHandler;
import com.android.systemui.Dependency;
import com.android.systemui.assist.AssistManager;

import com.google.android.systemui.elmyra.sensors.GestureSensor.DetectionProperties;

import java.util.Objects;

public class CustomActions extends Action {

    private AssistManager mAssistManager;
    private PowerManager pm;

    public CustomActions(Context context) {
        super(context, null);
        mAssistManager = Dependency.get(AssistManager.class);
        pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    public void onTrigger(DetectionProperties detectionProperties) {
        final ContentResolver resolver = getContext().getContentResolver();

        int mActionSelection = Settings.Secure.getIntForUser(resolver,
                Settings.Secure.SQUEEZE_SELECTION, 0, UserHandle.USER_CURRENT);

        // Check if the screen is turned on
        if (pm == null) return;
        boolean isScreenOn = pm.isScreenOn();

        switch (mActionSelection) {
            case 0: // No action
            default:
                break;
            case 1: // Assistant
                ActionHandler.switchScreenOn(getContext());
                mAssistManager.startAssist(new Bundle() /* args */);
                break;
            case 2: // Voice search
                if (isScreenOn) {
                    launchVoiceSearch(getContext());
                }
                break;
            case 3: // Camera
                ActionHandler.switchScreenOn(getContext());
                launchCamera(getContext());
                break;
            case 4: // Flashlight
                ActionHandler.StatusBarHelper.toggleFlashlight();
                break;
            case 5: // Clear notifications
                ActionHandler.StatusBarHelper.clearAllNotifications();
                break;
            case 6: // Volume panel
                if (isScreenOn) {
                    ActionHandler.volumePanel(getContext());
                }
                break;
            case 7: // Screen off
                if (isScreenOn) {
                    ActionHandler.screenOff(getContext());
                }
                break;
            case 8: // Notification panel
                if (isScreenOn) {
                }
                break;
//            case 9: // Screenshot
//                  if (isScreenOn) {
//                    ActionHandler.volumePanel(getContext());
//                  }
//                break;
            case 10: // QS panel
                if (isScreenOn) {
                    ActionHandler.StatusBarHelper.expandSettingsPanel();
                }
                break;
            case 11: // Application
                if (isScreenOn) {
                    launchApp(getContext());
                }
                break;
        }
    }

    private void launchApp(Context context) {
        SharedPreferences sp = context.getSharedPreferences("key", 0);
        // Get the stored selected app from shared preferences
        String selectedApp = sp.getString("selectedAppValue","");

        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(
                Objects.requireNonNull(selectedApp).toLowerCase());
        if (launchIntent != null) {
            context.startActivity(launchIntent);
        }
    }

    private static void launchCamera(Context context) {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA_SECURE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    private void launchVoiceSearch(Context context) {
        Intent intent = new Intent(Intent.ACTION_SEARCH_LONG_PRESS);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}

