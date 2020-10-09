package com.google.android.settings.aware;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.DeviceConfig;
import android.provider.Settings;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.aware.AwareFeatureProvider;
import com.android.settings.overlay.FeatureFactory;

public class AwareHelper {
    @VisibleForTesting
    static final String FLAG_TAP_ENABLE = "enable_tap";
    @VisibleForTesting
    static final String NAMESPACE = "oslo";
    public final Context mContext;
    private final AwareFeatureProvider mFeatureProvider = FeatureFactory.getFactory(mContext).getAwareFeatureProvider();
    private final SettingsObserver mSettingsObserver = new SettingsObserver(new Handler(Looper.getMainLooper()));

    public interface Callback {
        void onChange(Uri uri);
    }

    public AwareHelper(Context context) {
        mContext = context;
    }

    public boolean isGestureConfigurable() {
        return isEnabled() && isAvailable();
    }

    public boolean isAvailable() {
        return isSupported() && !isAirplaneModeOn() && !isBatterySaverModeOn();
    }

    public boolean isSupported() {
        return mFeatureProvider.isSupported(mContext);
    }

    public boolean isEnabled() {
        return mFeatureProvider.isEnabled(mContext);
    }

    public void register(Callback callback) {
        mSettingsObserver.observe();
        mSettingsObserver.setCallback(callback);
    }

    public void unregister() {
        mContext.getContentResolver().unregisterContentObserver(mSettingsObserver);
    }

    public void writeFeatureEnabled(String str, boolean z) {
        mContext.getSharedPreferences("aware_settings", 0).edit().putBoolean(str, z).apply();
    }

    public boolean readFeatureEnabled(String str) {
        return mContext.getSharedPreferences("aware_settings", 0).getBoolean(str, true);
    }

    public static boolean isTapAvailableOnTheDevice() {
        return DeviceConfig.getBoolean(NAMESPACE, FLAG_TAP_ENABLE, true);
    }

    public boolean isAirplaneModeOn() {
        return Settings.Global.getInt(mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
    }

    public boolean isBatterySaverModeOn() {
        return Settings.Global.getInt(mContext.getContentResolver(), "low_power", 0) == 1;
    }

    private final class SettingsObserver extends ContentObserver {
        private final Uri mAirplaneMode = Settings.Global.getUriFor("airplane_mode_on");
        private final Uri mAwareAllowed = Settings.Global.getUriFor("aware_allowed");
        private final Uri mAwareEnabled = Settings.Secure.getUriFor("aware_enabled");
        private final Uri mBatterySaver = Settings.Global.getUriFor("low_power");
        private Callback mCallback;

        public SettingsObserver(Handler handler) {
            super(handler);
        }

    
        public void setCallback(Callback callback) {
            mCallback = callback;
        }

        public void observe() {
            ContentResolver contentResolver = AwareHelper.mContext.getContentResolver();
            contentResolver.registerContentObserver(mAwareEnabled, false, this);
            contentResolver.registerContentObserver(mAwareAllowed, false, this);
            contentResolver.registerContentObserver(mAirplaneMode, false, this);
            contentResolver.registerContentObserver(mBatterySaver, false, this);
        }

        public void onChange(boolean z, Uri uri) {
            Callback callback = mCallback;
            if (callback != null) {
                callback.onChange(uri);
            }
        }
    }
}
