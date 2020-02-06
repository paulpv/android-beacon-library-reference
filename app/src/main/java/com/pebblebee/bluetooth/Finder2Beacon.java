/**
 * Radius Networks, Inc.
 * http://www.radiusnetworks.com
 *
 * @author David G. Young
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.pebblebee.bluetooth;

import org.altbeacon.beacon.Beacon;

public class Finder2Beacon extends Beacon {
    private static final String TAG = "Finder2Beacon";

    public Finder2Beacon(Beacon otherBeacon) {
        super(otherBeacon);
    }

    public Finder2Beacon() {
        //setParserIdentifier
    }

    /*
    /**
     * Required for making object Parcelable.  If you override this class, you must provide an
     * equivalent version of this method.
     * /
    public static final Creator<Finder2Beacon> CREATOR
            = new Creator<Finder2Beacon>() {
        public Finder2Beacon createFromParcel(Parcel in) {
            return new Finder2Beacon(in);
        }

        public Finder2Beacon[] newArray(int size) {
            return new Finder2Beacon[size];
        }
    };
    */

    /*
    /**
     * Copy constructor from base class
     * @param beacon
     * /
    protected Finder2Beacon(Beacon beacon) {
        super();
        this.mBluetoothAddress = beacon.getBluetoothAddress();
        this.mIdentifiers = beacon.getIdentifiers();
        this.mBeaconTypeCode = beacon.getBeaconTypeCode();
        this.mDataFields = beacon.getDataFields();
        this.mDistance = beacon.getDistance();
        this.mRssi = beacon.getRssi();
        this.mTxPower = beacon.getTxPower();
    }

    /**
     * @see Finder2Beacon.Builder to make AltBeacon instances
     * /
    protected Finder2Beacon() {
    }
    */

    /*
    /**
     * Required for making object Parcelable
     ** /
    protected Finder2Beacon(Parcel in) {
        super(in);
    }
    */

    /*
    /**
     * Returns a field with a value from 0-255 that can be used for the purposes specified by the
     * manufacturer.  The manufacturer specifications for the beacon should be checked before using
     * this field, and the manufacturer should be checked against the Beacon#mManufacturer
     * field
     * @return mfgReserved
     * /
    public int getMfgReserved() {
        return mDataFields.get(0).intValue();
    }
    */

    /*
     * Required for making object Parcelable
     * @return
    @Override
    public int describeContents() {
        return 0;
    }
    */

    /*
     * Required for making object Parcelable
     @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
    }
    */

    /*
    /**
     * Builder class for AltBeacon objects. Provides a convenient way to set the various fields of a
     * Beacon
     *
     * <p>Example:
     *
     * <pre>
     * Beacon beacon = new Beacon.Builder()
     *         .setId1(&quot;2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6&quot;)
     *         .setId2("1")
     *         .setId3("2")
     *         .setMfgReserved(3);
     *         .build();
     * </pre>
     * /
    public static class Builder extends Beacon.Builder {
        @Override
        public Beacon build() {
            return new Finder2Beacon(super.build());
        }
        /*
        public Builder setMfgReserved(int mfgReserved) {
            if (mBeacon.getDataFields().size() != 0) {
                mBeacon.getDataFields() = new ArrayList<Long>();
            }
            mBeacon.getDataFields().add((long)mfgReserved);
            return this;
        }
        * /
    }
    */

}
