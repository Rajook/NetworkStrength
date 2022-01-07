package com.example.networkstrength;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.networkstrength.utilities.NetworkChangeReceiver;
import com.example.networkstrength.utilities.NetworkUtil;
import com.example.networkstrength.utilities.Utilities;

public class MainActivity extends AppCompatActivity implements BandwidthListener{

    private BroadcastReceiver mNetworkReceiver;
    private Button mBtnCheckNetwork,mBtnPing;
    TelephonyManager mTelephonyManager;
    MyPhoneStateListener mPhoneStatelistener;
    int mSignalStrength = 0;
    private TextView tvMbps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNetworkReceiver = new NetworkChangeReceiver();

        registerNetworkBroadcast();

        mBtnCheckNetwork = findViewById(R.id.btn_check_network);
        tvMbps = findViewById(R.id.tv_speed);
        mBtnPing = findViewById(R.id.btn_ping);

        mBtnCheckNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                boolean isNetworkConnected = Utilities.Companion.isNetworkConnected(getApplicationContext());
//                int connectedNetwork = Utilities.Companion.getNetworkType(getApplicationContext());
                int connectedNetwork = NetworkUtil.getConnectivityStatus(getApplicationContext());

                if (connectedNetwork != NetworkUtil.TYPE_NOT_CONNECTED){
                    switch (connectedNetwork){
                        case NetworkUtil.TYPE_WIFI:{
                            Toast.makeText(getApplicationContext(), "Wifi Connected", Toast.LENGTH_LONG).show();
                        }break;
                        case NetworkUtil.TYPE_MOBILE:{
                            Toast.makeText(getApplicationContext(), "Mobile Connected", Toast.LENGTH_LONG).show();
                        }break;
                        case NetworkUtil.TYPE_ETHERNET:{
                            Toast.makeText(getApplicationContext(), "ETHERNET Connected", Toast.LENGTH_LONG).show();
                        }break;
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "No Internet Connected", Toast.LENGTH_LONG).show();
                }

            }
        });
        
        mBtnPing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                boolean isOnline = Utilities.Companion.isNetworkReachAble();
                boolean isOnline = NetworkUtil.isOnline();

                if (isOnline){
                    Toast.makeText(getApplicationContext(), NetworkUtil.isConnectedFast(MainActivity.this)+"", Toast.LENGTH_LONG).show();
                    // DownSpeed in MBPS
                    double downSpeed = NetworkUtil.NetworkSpeed(MainActivity.this)[0];
                    // UpSpeed  in MBPS
                    double upSpeed = NetworkUtil.NetworkSpeed(MainActivity.this)[1];

                    tvMbps.setText("Up Speed:"+ upSpeed+"Mbps and Down Speed:"  +downSpeed+" Mbps");
                }else {
                    Toast.makeText(getApplicationContext(), "Not Working", Toast.LENGTH_LONG).show();
                }

                mPhoneStatelistener = new MyPhoneStateListener(MainActivity.this);
                mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                mTelephonyManager.listen(mPhoneStatelistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

            }
        });
    }

    private void registerNetworkBroadcast() {
        IntentFilter filter = new IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        this.registerReceiver(mNetworkReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetworkReceiver);
        mTelephonyManager.listen(mPhoneStatelistener, PhoneStateListener.LISTEN_NONE);
    }

    @Override
    public void bandwidth(int mSignalStrength) {

//        Toast.makeText(getApplicationContext(), "Signal Strength :"+mSignalStrength, Toast.LENGTH_LONG).show();

        // DownSpeed in MBPS
        double downSpeed = NetworkUtil.NetworkSpeed(MainActivity.this)[0];
        // UpSpeed  in MBPS
        double upSpeed = NetworkUtil.NetworkSpeed(MainActivity.this)[1];

        tvMbps.setText("Up Speed:"+ upSpeed+"Mbps and Down Speed:"  +downSpeed+" Mbps");
    }
}