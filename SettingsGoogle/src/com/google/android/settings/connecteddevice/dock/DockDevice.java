package com.google.android.settings.connecteddevice.dock;

public class DockDevice {
    private String mId;
    private String mName;

    private DockDevice() {
    }

    DockDevice(String str, String str2) {
        mId = str;
        mName = str2;
    }

    public String getName() {
        return mName;
    }

    public String getId() {
        return mId;
    }
}
