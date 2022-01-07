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

                mPhoneStatelistener = new MyPhoneStateListener(MainActivity.this);
                mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                if (isOnline){
                    Toast.makeText(getApplicationContext(), "Fast : "+NetworkUtil.isConnectedFast(MainActivity.this)+"", Toast.LENGTH_LONG).show();

                    String strength = NetworkUtil.NetworkSpeed(MainActivity.this);

                    mTelephonyManager.listen(mPhoneStatelistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

                    tvMbps.setText(strength);
                }else {
                    if (mTelephonyManager!=null){
                        mTelephonyManager.listen(mPhoneStatelistener, PhoneStateListener.LISTEN_NONE);
                    }
                    Toast.makeText(getApplicationContext(), "Internet Not Working", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void registerNetworkBroadcast() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTelephonyManager.listen(mPhoneStatelistener, PhoneStateListener.LISTEN_NONE);
    }

    @Override
    public void bandwidth(int mSignalStrength) {

//        Toast.makeText(getApplicationContext(), "Signal Strength :"+mSignalStrength, Toast.LENGTH_LONG).show();

        String strength = NetworkUtil.NetworkSpeed(MainActivity.this);

        tvMbps.setText(strength);
    }
}