package com.google.android.settings.connecteddevice.dock;

import android.content.ContentProviderClient;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R;
import com.android.settings.connecteddevice.DevicePreferenceCallback;
import com.android.settings.connecteddevice.dock.DockUpdater;
import com.android.settings.widget.SingleTargetGearPreference;
import com.google.android.settings.connecteddevice.dock.DockAsyncQueryHandler;
import com.google.common.base.Preconditions;
import java.util.List;

public class ConnectedDockUpdater implements DockUpdater, DockAsyncQueryHandler.OnQueryListener {
    private final DockAsyncQueryHandler mAsyncQueryHandler;
    private final ConnectedDockObserver mConnectedDockObserver;
    private final Context mContext;
    private final DevicePreferenceCallback mDevicePreferenceCallback;
    private String mDockId = null;
    private String mDockName = null;
    @VisibleForTesting
    SingleTargetGearPreference mDockPreference = null;
    private final Uri mDockProviderUri;
    @VisibleForTesting
    boolean mIsObserverRegistered;
    private Context mPreferenceContext = null;

    public ConnectedDockUpdater(Context context, DevicePreferenceCallback devicePreferenceCallback) {
        mContext = context;
        mDevicePreferenceCallback = devicePreferenceCallback;
        mDockProviderUri = DockContract.DOCK_PROVIDER_CONNECTED_URI;
        mConnectedDockObserver = new ConnectedDockObserver(new Handler(Looper.getMainLooper()));
        DockAsyncQueryHandler dockAsyncQueryHandler = new DockAsyncQueryHandler(mContext.getContentResolver());
        mAsyncQueryHandler = dockAsyncQueryHandler;
        dockAsyncQueryHandler.setOnQueryListener(this);
    }

    public void registerCallback() {
        ContentProviderClient acquireContentProviderClient = mContext.getContentResolver().acquireContentProviderClient(mDockProviderUri);
        if (acquireContentProviderClient != null) {
            acquireContentProviderClient.release();
            mContext.getContentResolver().registerContentObserver(mDockProviderUri, false, mConnectedDockObserver);
            mIsObserverRegistered = true;
            forceUpdate();
        }
    }

    public void unregisterCallback() {
        if (mIsObserverRegistered) {
            mContext.getContentResolver().unregisterContentObserver(mConnectedDockObserver);
            mIsObserverRegistered = false;
        }
    }

    public void forceUpdate() {
        mAsyncQueryHandler.startQuery(1, mContext, mDockProviderUri, DockContract.DOCK_PROJECTION, (String) null, (String[]) null, (String) null);
    }

    public void setPreferenceContext(Context context) {
        mPreferenceContext = context;
    }

    public void onQueryComplete(int i, List<DockDevice> list) {
        if (list == null || list.isEmpty()) {
            SingleTargetGearPreference singleTargetGearPreference = mDockPreference;
            if (singleTargetGearPreference != null && singleTargetGearPreference.isVisible()) {
                mDockPreference.setVisible(false);
                mDevicePreferenceCallback.onDeviceRemoved(mDockPreference);
                return;
            }
            return;
        }
        DockDevice dockDevice = list.get(0);
        mDockId = dockDevice.getId();
        mDockName = dockDevice.getName();
        updatePreference();
    }

    private void updatePreference() {
        if (mDockPreference == null) {
            initPreference();
        }
        if (!TextUtils.isEmpty(mDockName)) {
            mDockPreference.setIcon(DockUtils.buildRainbowIcon(mPreferenceContext, mDockId));
            mDockPreference.setTitle((CharSequence) mDockName);
            if (!TextUtils.isEmpty(mDockId)) {
                mDockPreference.setIntent(DockContract.buildDockSettingIntent(mDockId));
                mDockPreference.setSelectable(true);
            }
            if (!mDockPreference.isVisible()) {
                mDockPreference.setVisible(true);
                mDevicePreferenceCallback.onDeviceAdded(mDockPreference);
            }
        } else if (mDockPreference.isVisible()) {
            mDockPreference.setVisible(false);
            mDevicePreferenceCallback.onDeviceRemoved(mDockPreference);
        }
    }

    @VisibleForTesting
    public void initPreference() {
        if (mDockPreference == null) {
            Preconditions.checkNotNull(mPreferenceContext, "Preference context cannot be null");
            SingleTargetGearPreference singleTargetGearPreference = new SingleTargetGearPreference(mPreferenceContext, (AttributeSet) null);
            mDockPreference = singleTargetGearPreference;
            singleTargetGearPreference.setSummary((CharSequence) mContext.getString(R.string.dock_summary_charging_phone));
            mDockPreference.setSelectable(false);
            mDockPreference.setVisible(false);
        }
    }

    private class ConnectedDockObserver extends ContentObserver {
        ConnectedDockObserver(Handler handler) {
            super(handler);
        }

        public void onChange(boolean z) {
            super.onChange(z);
            ConnectedDockUpdater.forceUpdate();
        }
    }
}
