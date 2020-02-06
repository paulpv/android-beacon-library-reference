package org.altbeacon.beaconreference;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.RemoteException;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.pebblebee.bluetooth.Finder2BeaconParser;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.logging.LogManager;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.service.scanner.NonBeaconLeScanCallback;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.Collection;
import java.util.List;

/**
 * Created by dyoung on 12/13/13.
 */
public class BeaconReferenceApplication extends Application implements BootstrapNotifier, RangeNotifier {
    private static final String TAG = "BeaconReferenceApp";

    public static final int FOREGROUND_NOTIFICATION_ID = 456;

    public static Notification makeNotification(Context context, String text) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle(text);
        builder.setOngoing(true);
        Intent intent = new Intent(context, MonitoringActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification Channel ID", "My Notification Name", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("My Notification Channel Description");
            NotificationManager notificationManager = Utils.getNotificationManager(context);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channel.getId());
        }
        return builder.build();
    }

    public static void updateNotification(Context context) {
        String text = getNotificationText(context);
        Notification notification = makeNotification(context, text);
        NotificationManager notificationManager = Utils.getNotificationManager(context);
        notificationManager.notify(FOREGROUND_NOTIFICATION_ID, notification);
    }

    public static String getNotificationText(Context context) {
        return Utils.isBluetoothAdapterEnabled(context) ? "Scanning for Beacons" : "Waiting for Bluetooth";
    }

    private BeaconManager beaconManager;
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private MonitoringActivity monitoringActivity = null;
    private String cumulativeLog = "";

    private BluetoothAdapterStateBroadcastReceiver bluetoothAdapterStateBroadcastReceiver;

    public void onCreate() {
        super.onCreate();

        //BeaconManager.setDebug(true);

        beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);

        beaconManager.setNonBeaconLeScanCallback(new NonBeaconLeScanCallback() {
            @Override
            public void onNonBeaconLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                BeaconReferenceApplication.this.onNonBeaconLeScan(device, rssi, scanRecord);
            }
        });

        //beaconManager.setForegroundScanPeriod(BeaconManager.DEFAULT_FOREGROUND_SCAN_PERIOD);
        //beaconManager.setForegroundBetweenScanPeriod(BeaconManager.DEFAULT_FOREGROUND_BETWEEN_SCAN_PERIOD);
        //beaconManager.setBackgroundScanPeriod(BeaconManager.DEFAULT_FOREGROUND_BETWEEN_SCAN_PERIOD * 2);
        //beaconManager.setBackgroundBetweenScanPeriod(BeaconManager.DEFAULT_FOREGROUND_BETWEEN_SCAN_PERIOD * 2);

        // By default the AndroidBeaconLibrary will only find AltBeacons.  If you wish to make it
        // find a different type of beacon, you must specify the byte layout for that beacon's
        // advertisement with a line like below.  The example shows how to find a beacon with the
        // same byte layout as AltBeacon but with a beaconTypeCode of 0xaabb.  To find the proper
        // layout expression for other beacon types, do a web search for "setBeaconLayout"
        // including the quotes.
        //
        //beaconManager.getBeaconParsers().clear();
        //beaconManager.getBeaconParsers().add(new BeaconParser().
        //        setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        List<BeaconParser> beaconParsers = beaconManager.getBeaconParsers();
        beaconParsers.clear(); // For testing purposes I currently don't care about the default AltBeacon beacons
        beaconParsers.add(new Finder2BeaconParser());

        // Uncomment the code below to use a foreground service to scan for beacons. This unlocks
        // the ability to continually scan for long periods of time in the background on Andorid 8+
        // in exchange for showing an icon at the top of the screen and a always-on notification to
        // communicate to users that your app is using resources in the background.
        //

        if (true) {
            String text = getNotificationText(this);
            Notification notification = makeNotification(this, text);
            beaconManager.enableForegroundServiceScanning(notification, FOREGROUND_NOTIFICATION_ID);

        // For the above foreground scanning service to be useful, you need to disable
        // JobScheduler-based scans (used on Android 8+) and set a fast background scan
        // cycle that would otherwise be disallowed by the operating system.
        //
            //beaconManager.setEnableScheduledScanJobs(false); // Unnecessary; Already called w/ false by enableForegroundServiceScanning
            beaconManager.setBackgroundBetweenScanPeriod(6200);
            beaconManager.setBackgroundScanPeriod(6200);
        }

        // simply constructing this class and holding a reference to it in your custom Application
        // class will automatically cause the BeaconLibrary to save battery whenever the application
        // is not visible.  This reduces bluetooth power usage by about 60%
        backgroundPowerSaver = new BackgroundPowerSaver(this);

        if (isMonitoring()) {
            Log.d(TAG, "setting up background monitoring for beacons and power saving");
            enableMonitoring();
        }

        // If you wish to test beacon detection in the Android Emulator, you can use code like this:
        // BeaconManager.setBeaconSimulator(new TimedBeaconSimulator() );
        // ((TimedBeaconSimulator) BeaconManager.getBeaconSimulator()).createTimedSimulatedBeacons();
    }

    public BeaconManager getBeaconManager() {
        return beaconManager;
    }

    public boolean isMonitoring() {
        return beaconManager.getMonitoredRegions().size() > 0;
    }

    public void disableMonitoring() {
        if (bluetoothAdapterStateBroadcastReceiver != null) {
            bluetoothAdapterStateBroadcastReceiver.stop();
            bluetoothAdapterStateBroadcastReceiver = null;
        }
        if (regionBootstrap != null) {
            regionBootstrap.disable();
            regionBootstrap = null;
        }
    }
    public void enableMonitoring() {
        bluetoothAdapterStateBroadcastReceiver = new BluetoothAdapterStateBroadcastReceiver(this);
        bluetoothAdapterStateBroadcastReceiver.start(new BluetoothAdapterStateBroadcastReceiver.Callbacks() {
            @Override
            public void onBluetoothAdapterEnabled() {
                updateNotification(getApplicationContext());
            }

            @Override
            public void onBluetoothAdapterDisabled() {
                updateNotification(getApplicationContext());
            }
        });

        Region region = new Region("backgroundRegion", null, null, null);
        regionBootstrap = new RegionBootstrap(this, region);
    }

    public void onNonBeaconLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        Log.e(TAG, "onNonBeaconLeScan: device=" + device + ", rssi=" + rssi + ", scanRecord=" + BeaconParser.byteArrayToString(scanRecord));
    }

    @Override
    public void didEnterRegion(Region region) {
        // In this example, this class sends a notification to the user whenever a Beacon
        // matching a Region (defined above) are first seen.
        Log.e(TAG, "#REGION didEnterRegion(" + region + ")");
        /*
        if (!haveDetectedBeaconsSinceBoot) {
            Log.d(TAG, "auto launching MainActivity");
            // The very first time since boot that we detect an beacon, we launch the MainActivity
            Intent intent = new Intent(this, MonitoringActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Important:  make sure to add android:launchMode="singleInstance" in the manifest
            // to keep multiple copies of this activity from getting created if the user has
            // already manually launched the app.
            startActivity(intent);
            haveDetectedBeaconsSinceBoot = true;
        } else {
            if (monitoringActivity != null) {
                // If the Monitoring Activity is visible, we log info about the beacons we have seen on its display
                //logToDisplay("I see a beacon again" );
            } else {
                // If we have already seen beacons before, but the monitoring activity is not in
                // the foreground, we send a notification to the user on subsequent detections.
                Log.d(TAG, "Sending notification.");
                sendNotification();
            }
        }
        */
        try {
            Log.e(TAG, "#REGION addRangeNotifier/startRangingBeaconsInRegion");
            beaconManager.addRangeNotifier(this);
            beaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            Log.e(TAG, "#REGION addRangeNotifier/startRangingBeaconsInRegion", e);
        }
    }

    @Override
    public void didExitRegion(Region region) {
        Log.e(TAG, "#REGION didExitRegion(" + region + ")");
        try {
            Log.e(TAG, "#REGION stopRangingBeaconsInRegion/removeRangeNotifier");
            beaconManager.stopRangingBeaconsInRegion(region);
            beaconManager.removeRangeNotifier(this);
        } catch (RemoteException e) {
            Log.e(TAG, "#REGION stopRangingBeaconsInRegion/removeRangeNotifier", e);
        }
        //logToDisplay("I no longer see a beacon.");
    }

    @Override
    public void didDetermineStateForRegion(int state, Region region) {
        String stateString = Utils.monitorNotifierStateToString(state);
        Log.e(TAG, "#REGION didDetermineStateForRegion(" + stateString + ", " + region + ")");
        //logToDisplay("Current region state is: " + (state == 1 ? "INSIDE" : "OUTSIDE ("+state+")"));
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        Log.e(TAG, "#REGION didRangeBeaconsInRegion(beacons=" + beacons + ", region=" + region + ")");
        for (Beacon beacon : beacons) {
            Log.e(TAG, "#REGION didRangeBeaconsInRegion: beacon=" + beacon + ": rssi=" + beacon.getRunningAverageRssi());
        }
    }

    private void sendNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "channelId")
                        .setContentTitle("Beacon Reference Application")
                        .setContentText("A beacon is nearby.")
                        .setSmallIcon(R.drawable.ic_launcher);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(new Intent(this, MonitoringActivity.class));
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    public void setMonitoringActivity(MonitoringActivity activity) {
        LogManager.i(TAG, "Setting monitoringActivity = " + activity);
        this.monitoringActivity = activity;
    }

    private void logToDisplay(String line) {
        cumulativeLog += (line + "\n");
        if (this.monitoringActivity != null) {
            this.monitoringActivity.updateLog(cumulativeLog);
        }
    }

    public String getLog() {
        return cumulativeLog;
    }
}
