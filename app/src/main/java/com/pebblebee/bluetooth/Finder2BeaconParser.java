/**
 * Radius Networks, Inc.
 * http://www.radiusnetworks.com
 *
 * @author David G. Young
 * <p/>
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.pebblebee.bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;

/**
 * A specific beacon parser designed to parse only AltBeacons from raw BLE packets detected by
 * Android.
 * Additional <code>BeaconParser</code> instances can get created and registered with the library.
 * {@link BeaconParser See BeaconParser for more information.}
 */
public class Finder2BeaconParser extends BeaconParser {
    public static final String TAG = "StoneFooBeaconParser";

    /**
     * Constructs an AltBeacon Parser and sets its layout
     */
    public Finder2BeaconParser() {
        super();
        //mHardwareAssistManufacturers = new int[]{0x0118};
        //setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");

        /*
        Data Message- Sent every 60 secs for 5 secs or after interrupt
        advData[0]= AD_TYPE_MANUF
        advData[1]= MSB Major
        advData[2]= LSB Major
        advData[3]= MSB Minor
        advData[4]= LSB Minor
        advData[5]= action (1 single press, 2 hold press)
        advData[6]= motion
        advData[7]= MSB temperature (in Celsius)
        advData[8]= LSB temperature
        advData[9]= MSB battery (in mV)
        advData[10]= LSB battery
        advData[11]= MSB Minute Count
        advData[12]= LSB Minute Count
        advData[13]= MSB small motion
        advData[14]= LSB small motion
        advData[15]= MSB medium motion
        advData[16]= LSB medium motion
        advData[17]= MSB large motion
        advData[18]= LSB large motion


        Stone Button Press:
        scanData[0] = 2 (0x2)     ?
        scanData[1] = 1 (0x1)     ?
        scanData[2] = 4 (0x4)     ?
        scanData[3] = 3 (0x3)     ?
        scanData[4] = 3 (0x3)     ?
        scanData[5] = -120 (0x88) \ service?
        scanData[6] = -120 (0x88) /
        scanData[7] = 19 (0x13)   ?
        scanData[8] = -1 (0xFF)   ?
        scanData[9] = 0 (0x0)     ?
        scanData[10] = 22 (0x16)  ?
        scanData[11] = 0 (0x0)    ?
        scanData[12] = 33 (0x21)  ?
        scanData[13] = 1 (0x1)    | action?
        scanData[14] = 0 (0x0)    | motion?
        scanData[15] = 0 (0x0)    \ temperature?
        scanData[16] = 23 (0x17)  /
        scanData[17] = 11 (0xB)   \ battery?
        scanData[18] = -88 (0xA8) /
        */
        //setBeaconLayout("x,s:-4--5=8888,m:2-2=00,d:3-3,d:4-4,d:5-6,d:7-8");
        //setBeaconLayout("x,m:0-1=0016,d:4-4,d:5-5,d:6-7,d:8-9");

        /*
ScanHelper: #SCAN processScanResult: device=0E:06:E5:75:F0:AE, rssi=-78, scanRecord=05 09 46 4e 44 52 03 02 25 fa 08 ff 0e 06 e5 75 f0 ae 01 05 09 46 4e 44 52 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
BeaconParser: Ignoring pdu type 09
BeaconParser: Ignoring pdu type 02
BeaconParser: Processing pdu type FF: 05 09 46 4e 44 52 03 02 25 fa 08 ff 0e06e575f0ae010509464e445200000000000000000000000000000000000000000000000000000000000000000000000000 with startIndex: 12, endIndex: 18
BeaconParser: This is not a matching Beacon advertisement. (Was expecting be ac.  The bytes I see are: 0509464e4452030225fa08ff0e06e575f0ae010509464e445200000000000000000000000000000000000000000000000000000000000000000000000000
BeaconReferenceApp: onNonBeaconLeScan: device=0E:06:E5:75:F0:AE, rssi=-78, scanRecord=05 09 46 4e 44 52 03 02 25 fa 08 ff 0e 06 e5 75 f0 ae 01 05 09 46 4e 44 52 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00



ScanHelper: #SCAN processScanResult: device=0E:06:E5:E6:E7:AE, rssi=-60, scanRecord=05 09 46 4e 44 52 03 02 25 fa 08 ff 0e 06 e5 e6 e7 ae 01 05 09 46 4e 44 52 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
BeaconReferenceApp: onNonBeaconLeScan: device=0E:06:E5:E6:E7:AE, rssi=-60, scanRecord=05 09 46 4e 44 52 03 02 25 fa 08 ff 0e 06 e5 e6 e7 ae 01 05 09 46 4e 44 52 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
05 09
46 4e 44 52                             FNDR
03 02 25 fa 08 ff
0e 06 e5 e6 e7 ae                       macAddress
01                                      macAddressExtraByte
05                                      actionSequenceAndData
09
46 4e 44 52                             FNDR
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00


https://docs.google.com/spreadsheets/d/1-2w8WIVnXpfKR1aN0r7qQ3-AR_cE_tXG2LRec-UmZcw/edit#gid=84972672

ScanHelper: #SCAN processScanResult: device=0E:06:E5:75:F0:AE, rssi=-77, scanRecord=05 09 46 4e 44 52 03 02 25 fa 08 ff 0e 06 e5 75 f0 ae 01 05 09 46 4e 44 52 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
05 09
46 4e 44 52             FNDR
03 02 25 fa 08 ff
0e 06 e5 75 f0 ae       macAddress
01                      macAddressExtraByte
05 09 46 4e 44 52 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
PbDeviceScannerManager: onDeviceScanned(0E:06:E5:75:F0:AE, rssi=-77, scanRecord=ScanRecord [mAdvertiseFlags=-1, mServiceUuids=[0000fa25-0000-1000-8000-00805f9b34fb], mManufacturerSpecificData={1550=[-27, 117, -16, -82, 1]}, mServiceData={}, mTxPowerLevel=-2147483648, mDeviceName=FNDR])
PbDeviceScannerManager: onDeviceScanned: mThreadWorker.enqueue(0E:06:E5:75:F0:AE, rssi=-77, scanRecord=ScanRecord [mAdvertiseFlags=-1, mServiceUuids=[0000fa25-0000-1000-8000-00805f9b34fb], mManufacturerSpecificData={1550=[-27, 117, -16, -82, 1]}, mServiceData={}, mTxPowerLevel=-2147483648, mDeviceName=FNDR])
PbBleDevice…rserFinder2: 0E:06:E5:75:F0:AE parse: #TIMING:                                                          00-01-02-03-04
PbBleDevice…rserFinder2: 0E:06:E5:75:F0:AE parse: #TIMING: manufacturerSpecificDataBytes[manufacturerId=0x060E](5)=[E5-75-F0-AE-01]
PbBleDevice…rserFinder2: 0E:06:E5:75:F0:AE parse: #TIMING: serviceUuids=[0000fa25-0000-1000-8000-00805f9b34fb]
PbBleDevice…rserFinder2: 0E:06:E5:75:F0:AE parse: #TIMING: serviceData={}
PbBleDevice…rserFinder2: 0E:06:E5:75:F0:AE parse: elapsedSinceLastParseTimeMillis=990
PbBleDevice…rserFinder2: 0E:06:E5:75:F0:AE parse: #FINDER2 bluetoothDeviceName="fndr"
PbBleDevice…rserFinder2: 0E:06:E5:75:F0:AE parse: #FINDER2 DATA
PbBleDevice…rserFinder2: 0E:06:E5:75:F0:AE parse: #FINDER2 DATA -- BEGIN --------
PbBleDevice…rserFinder2: 0E:06:E5:75:F0:AE parse: #FINDER2 DATA macAddress="0E:06:E5:75:F0:AE"
PbBleDevice…rserFinder2: 0E:06:E5:75:F0:AE parse: #FINDER2 DATA macAddressExtraByte=1 (0x01)
PbBleDevice…rserFinder2: 0E:06:E5:75:F0:AE parse: #FINDER2 DATA -- END ----------
        */
        //mHardwareAssistManufacturers = new int[] { 0x060E };
        // NOTE: Per David Young "Just define a parser that looks like the AltBeacon or iBeacon layout.
        // You can point the i: and p: fields to any garbage byte you want"
        setBeaconLayout("m:0-1=0e06,d:0-5,d:6-6,i:0-5,p:0-0");
        // TODO:(pv) Find way to call setParserIdentifier...
    }

    /**
     * Construct an AltBeacon from a Bluetooth LE packet collected by Android's Bluetooth APIs,
     * including the raw Bluetooth device info
     *
     * @param scanData The actual packet bytes
     * @param rssi     The measured signal strength of the packet
     * @param device   The Bluetooth device that was detected
     * @return An instance of an <code>Beacon</code>
     */
    @TargetApi(5)
    @Override
    public Beacon fromScanData(byte[] scanData, int rssi, BluetoothDevice device) {
        return fromScanData(scanData, rssi, device, new Finder2Beacon());
    }
}
