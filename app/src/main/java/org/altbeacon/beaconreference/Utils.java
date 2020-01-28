package org.altbeacon.beaconreference;

import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import androidx.annotation.Nullable;

public class Utils {
    private Utils() {
    }

    @SuppressWarnings("WeakerAccess")
    public static String TAG(Object o) {
        return TAG((o == null) ? null : o.getClass());
    }

    @SuppressWarnings("WeakerAccess")
    public static String TAG(Class c) {
        return TAG(getShortClassName(c));
    }

    /**
     * Per http://developer.android.com/reference/android/util/Log.html#isLoggable(java.lang.String, int)
     */
    @SuppressWarnings("WeakerAccess")
    public static int LOG_TAG_LENGTH_LIMIT = 23;

    /**
     * Limits the tag length to {@link #LOG_TAG_LENGTH_LIMIT}
     *
     * @param tag
     * @return the tag limited to {@link #LOG_TAG_LENGTH_LIMIT}
     */
    @SuppressWarnings("WeakerAccess")
    public static String TAG(String tag) {
        int length = tag.length();
        if (length > LOG_TAG_LENGTH_LIMIT) {
            // Turn "ReallyLongClassName" to "ReallyLo...lassName";
            int half = LOG_TAG_LENGTH_LIMIT / 2;
            tag = tag.substring(0, half) + '…' + tag.substring(length - half);
        }
        return tag;
    }

    @SuppressWarnings("WeakerAccess")
    @Nullable
    public static String getShortClassName(Object o) {
        return getShortClassName(o == null ? null : o.getClass());
    }

    @SuppressWarnings("WeakerAccess")
    @Nullable
    public static String getShortClassName(Class c) {
        return c == null ? null : c.getSimpleName();
    }

    public static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static BluetoothManager getBluetoothManager(Context context) {
        return (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
    }

    public static BluetoothAdapter getBluetoothAdapter(Context context) {
        return getBluetoothManager(context).getAdapter();
    }

    /**
     * @return {@link BluetoothAdapter#STATE_OFF}, {@link BluetoothAdapter#STATE_TURNING_ON}, {@link
     * BluetoothAdapter#STATE_ON}, {@link BluetoothAdapter#STATE_TURNING_OFF}, or -1 if Bluetooth is not supported
     */
    public static int getBluetoothAdapterState(BluetoothAdapter bluetoothAdapter) {
        try {
            // TODO:(pv) Known to sometimes throw DeadObjectException
            //  https://code.google.com/p/android/issues/detail?id=67272
            //  https://github.com/RadiusNetworks/android-ibeacon-service/issues/16
            return bluetoothAdapter != null ? bluetoothAdapter.getState() : -1;
        } catch (Exception e) {
            return -1;
        }
    }

    public static boolean isBluetoothAdapterEnabled(Context context) {
        return getBluetoothAdapterState(getBluetoothAdapter(context)) == BluetoothAdapter.STATE_ON;
    }

    public static boolean isBluetoothAdapterEnabled(BluetoothAdapter bluetoothAdapter) {
        return getBluetoothAdapterState(bluetoothAdapter) == BluetoothAdapter.STATE_ON;
    }
}
