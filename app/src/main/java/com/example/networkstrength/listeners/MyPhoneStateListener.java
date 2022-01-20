package com.example.networkstrength.listeners;

import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;

public class MyPhoneStateListener extends PhoneStateListener {

    private int mSignalStrength;
    BandwidthListener bandwidthListener;

    public MyPhoneStateListener(BandwidthListener bandwidthListener) {
        this.bandwidthListener = bandwidthListener;
    }

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        mSignalStrength = signalStrength.getGsmSignalStrength();
        mSignalStrength = (2 * mSignalStrength) - 113; // -> dBm


        if (bandwidthListener!=null)bandwidthListener.bandwidth(mSignalStrength);
    }
}
