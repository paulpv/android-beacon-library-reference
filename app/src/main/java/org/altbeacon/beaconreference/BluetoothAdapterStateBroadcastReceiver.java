package org.altbeacon.beaconreference;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BluetoothAdapterStateBroadcastReceiver
        extends BroadcastReceiver {

    private static final String TAG = Utils.TAG(BluetoothAdapterStateBroadcastReceiver.class);

    interface Callbacks {
        void onBluetoothAdapterEnabled();

        void onBluetoothAdapterDisabled();
    }

    private final Context context;
    private final BluetoothManager bluetoothManager;
    private final BluetoothAdapter bluetoothAdapter;

    private Callbacks callbacks;
    private boolean isStarted;

    private int     previousBluetoothAdapterState;
    private boolean previousBluetoothAdapterEnabled;

    public BluetoothAdapterStateBroadcastReceiver(Context context) {
        this.context = context;
        bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    public void start(Callbacks callbacks) {
        this.callbacks = callbacks;
        if (!isStarted) {
            isStarted = true;

            previousBluetoothAdapterState = getBluetoothAdapterState();
            previousBluetoothAdapterEnabled = isBluetoothAdapterEnabled();

            //onBluetoothAdapterStateChanged();

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            context.registerReceiver(this, intentFilter);
        }
    }

    public void stop() {
        if (isStarted) {
            isStarted = false;
            context.unregisterReceiver(this);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int bluetoothAdapterState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
        onBluetoothAdapterStateChanged(bluetoothAdapterState);
    }

    public static boolean isBluetoothAdapterEnabled(int bluetoothAdapterState) {
        return bluetoothAdapterState == BluetoothAdapter.STATE_ON;
    }

    public boolean isBluetoothAdapterEnabled() {
        return getBluetoothAdapterState() == BluetoothAdapter.STATE_ON;
    }

    private void onBluetoothAdapterStateChanged() {
        onBluetoothAdapterStateChanged(getBluetoothAdapterState());
    }

    /**
     * @return {@link BluetoothAdapter#STATE_OFF}, {@link BluetoothAdapter#STATE_TURNING_ON}, {@link
     * BluetoothAdapter#STATE_ON}, {@link BluetoothAdapter#STATE_TURNING_OFF}, or -1 if Bluetooth is not supported
     */
    public int getBluetoothAdapterState() {
        try {
            // TODO:(pv) Known to sometimes throw DeadObjectException
            //  https://code.google.com/p/android/issues/detail?id=67272
            //  https://github.com/RadiusNetworks/android-ibeacon-service/issues/16
            return bluetoothAdapter != null ? bluetoothAdapter.getState() : -1;
        } catch (Exception e) {
            return -1;
        }
    }

    private void onBluetoothAdapterStateChanged(int bluetoothAdapterState) {
        if (!isStarted) {
            return;
        }

        if (bluetoothAdapterState == previousBluetoothAdapterState) {
            return;
        }

        previousBluetoothAdapterState = bluetoothAdapterState;

        boolean isBluetoothAdapterEnabled = isBluetoothAdapterEnabled(bluetoothAdapterState);

        if (isBluetoothAdapterEnabled == previousBluetoothAdapterEnabled) {
            return;
        }

        previousBluetoothAdapterEnabled = isBluetoothAdapterEnabled;

        if (callbacks == null) {
            return;
        }

        if (isBluetoothAdapterEnabled) {
            callbacks.onBluetoothAdapterEnabled();
        } else {
            callbacks.onBluetoothAdapterDisabled();
        }
    }
}
