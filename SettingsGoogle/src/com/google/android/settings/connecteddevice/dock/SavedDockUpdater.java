package com.google.android.settings.connecteddevice.dock;

import android.content.ContentProviderClient;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.connecteddevice.DevicePreferenceCallback;
import com.android.settings.connecteddevice.dock.DockUpdater;
import com.android.settings.widget.SingleTargetGearPreference;
import com.google.android.settings.connecteddevice.dock.DockAsyncQueryHandler;
import com.google.common.base.Preconditions;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class SavedDockUpdater implements DockUpdater, DockAsyncQueryHandler.OnQueryListener {
    private final DockAsyncQueryHandler mAsyncQueryHandler;
    private String mConnectedDockId = null;
    private final DockObserver mConnectedDockObserver;
    private final Context mContext;
    private final DevicePreferenceCallback mDevicePreferenceCallback;
    @VisibleForTesting
    boolean mIsObserverRegistered;
    private Context mPreferenceContext = null;
    @VisibleForTesting
    final Map<String, SingleTargetGearPreference> mPreferenceMap;
    private Map<String, String> mSavedDevices = null;
    private final DockObserver mSavedDockObserver;

    public SavedDockUpdater(Context context, DevicePreferenceCallback devicePreferenceCallback) {
        mContext = context;
        mDevicePreferenceCallback = devicePreferenceCallback;
        mPreferenceMap = new ArrayMap();
        Handler handler = new Handler(Looper.getMainLooper());
        mConnectedDockObserver = new DockObserver(handler, 1, DockContract.DOCK_PROVIDER_CONNECTED_URI);
        mSavedDockObserver = new DockObserver(handler, 2, DockContract.DOCK_PROVIDER_SAVED_URI);
        if (isRunningOnMainThread()) {
            DockAsyncQueryHandler dockAsyncQueryHandler = new DockAsyncQueryHandler(mContext.getContentResolver());
            mAsyncQueryHandler = dockAsyncQueryHandler;
            dockAsyncQueryHandler.setOnQueryListener(this);
            return;
        }
        mAsyncQueryHandler = null;
    }

    public void registerCallback() {
        ContentProviderClient acquireContentProviderClient = mContext.getContentResolver().acquireContentProviderClient(DockContract.DOCK_PROVIDER_SAVED_URI);
        if (acquireContentProviderClient != null) {
            acquireContentProviderClient.release();
            mContext.getContentResolver().registerContentObserver(DockContract.DOCK_PROVIDER_CONNECTED_URI, false, mConnectedDockObserver);
            mContext.getContentResolver().registerContentObserver(DockContract.DOCK_PROVIDER_SAVED_URI, false, mSavedDockObserver);
            mIsObserverRegistered = true;
            forceUpdate();
        }
    }

    public void unregisterCallback() {
        if (mIsObserverRegistered) {
            mContext.getContentResolver().unregisterContentObserver(mConnectedDockObserver);
            mContext.getContentResolver().unregisterContentObserver(mSavedDockObserver);
            mIsObserverRegistered = false;
        }
    }

    public void forceUpdate() {
        startQuery(1, DockContract.DOCK_PROVIDER_CONNECTED_URI);
        startQuery(2, DockContract.DOCK_PROVIDER_SAVED_URI);
    }

    public void setPreferenceContext(Context context) {
        mPreferenceContext = context;
    }

    public void onQueryComplete(int i, List<DockDevice> list) {
        if (list == null) {
            return;
        }
        if (i == 2) {
            updateSavedDevicesList(list);
        } else if (i == 1) {
            updateConnectedDevice(list);
        }
    }

    private SingleTargetGearPreference initPreference(String str, String str2) {
        Preconditions.checkNotNull(mPreferenceContext, "Preference context cannot be null");
        SingleTargetGearPreference singleTargetGearPreference = new SingleTargetGearPreference(mPreferenceContext, (AttributeSet) null);
        singleTargetGearPreference.setIcon(DockUtils.buildRainbowIcon(mPreferenceContext, str));
        singleTargetGearPreference.setTitle((CharSequence) str2);
        if (!TextUtils.isEmpty(str)) {
            singleTargetGearPreference.setIntent(DockContract.buildDockSettingIntent(str));
            singleTargetGearPreference.setSelectable(true);
        }
        return singleTargetGearPreference;
    }

    private boolean isRunningOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public void startQuery(int i, Uri uri) {
        Cursor query;
        if (isRunningOnMainThread()) {
            mAsyncQueryHandler.startQuery(i, mContext, uri, DockContract.DOCK_PROJECTION, (String) null, (String[]) null, (String) null);
            return;
        }
        try {
            query = mContext.getApplicationContext().getContentResolver().query(uri, DockContract.DOCK_PROJECTION, (String) null, (String[]) null, (String) null);
            onQueryComplete(i, DockAsyncQueryHandler.parseCursorToDockDevice(query));
            if (query != null) {
                query.close();
                return;
            }
            return;
        } catch (Exception e) {
            Log.w("SavedDockUpdater", "Query dockProvider fail", e);
            return;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    private void updateConnectedDevice(List<DockDevice> list) {
        if (list.isEmpty()) {
            mConnectedDockId = null;
            updateDevices();
            return;
        }
        String id = list.get(0).getId();
        mConnectedDockId = id;
        if (mPreferenceMap.containsKey(id)) {
            mDevicePreferenceCallback.onDeviceRemoved(mPreferenceMap.get(mConnectedDockId));
            mPreferenceMap.remove(mConnectedDockId);
        }
    }

    private void updateSavedDevicesList(List<DockDevice> list) {
        if (mSavedDevices == null) {
            mSavedDevices = new ArrayMap();
        }
        mSavedDevices.clear();
        for (DockDevice next : list) {
            String name = next.getName();
            if (!TextUtils.isEmpty(name)) {
                mSavedDevices.put(next.getId(), name);
            }
        }
        updateDevices();
    }

    private void updateDevices() {
        Map<String, String> map = mSavedDevices;
        if (map != null) {
            for (String next : map.keySet()) {
                if (!TextUtils.equals(next, mConnectedDockId)) {
                    String str = mSavedDevices.get(next);
                    if (mPreferenceMap.containsKey(next)) {
                        mPreferenceMap.get(next).setTitle((CharSequence) str);
                    } else {
                        mPreferenceMap.put(next, initPreference(next, str));
                        mDevicePreferenceCallback.onDeviceAdded(mPreferenceMap.get(next));
                    }
                }
            }
            mPreferenceMap.keySet().removeIf(new Predicate() {
                public final boolean test(Object obj) {
                    return SavedDockUpdater.hasDeviceBeenRemoved((String) obj);
                }
            });
        }
    }

    public boolean hasDeviceBeenRemoved(String str) {
        if (mSavedDevices.containsKey(str)) {
            return false;
        }
        mDevicePreferenceCallback.onDeviceRemoved(mPreferenceMap.get(str));
        return true;
    }

    private class DockObserver extends ContentObserver {
        private final int mToken;
        private final Uri mUri;

        DockObserver(Handler handler, int i, Uri uri) {
            super(handler);
            mToken = i;
            mUri = uri;
        }

        public void onChange(boolean z) {
            super.onChange(z);
            SavedDockUpdater.startQuery(mToken, mUri);
        }
    }
}
