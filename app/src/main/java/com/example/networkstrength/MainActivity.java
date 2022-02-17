package com.example.networkstrength;

import static com.example.networkstrength.utilities.ViewUtils.applyTheme;
import static com.example.networkstrength.utilities.ViewUtils.changeAppIcon;
import static com.example.networkstrength.utilities.ViewUtils.returnOld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.networkstrength.databinding.ActivityMainBinding;
import com.example.networkstrength.listeners.BandwidthListener;
import com.example.networkstrength.listeners.MyPhoneStateListener;
import com.example.networkstrength.utilities.DataStoreManager;
import com.example.networkstrength.utilities.NetworkChangeReceiver;
import com.example.networkstrength.utilities.NetworkUtil;
import com.example.networkstrength.utilities.ViewExtensionsKt;
import com.example.networkstrength.utilities.ViewUtils;
import com.example.networkstrength.views.BasicActivity;

public class MainActivity extends AppCompatActivity implements BandwidthListener {

    private static final int REQUEST_READ_PHONE_STATE = 123;
    private BroadcastReceiver mNetworkReceiver;
    TelephonyManager mTelephonyManager;
    MyPhoneStateListener mPhoneStatelistener;

    private ActivityMainBinding binding;

    int theme = 0; // 0 for genixian 1 for daneenian

    SharedPreferences prefs;
    DataStoreManager mDataStoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();


        //Disabling ability to Screenshot
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);


        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        theme = prefs.getInt("theme", 0);

        applyTheme(this,theme);

        setContentView(view);
//        setContentView(R.layout.activity_main);

        mNetworkReceiver = new NetworkChangeReceiver();

        registerNetworkBroadcast();

        checkPermissions();

//        ViewExtensionsKt.enable(binding.btnChangeTheme1,false);
        ViewExtensionsKt.loadButton(binding.btnChangeTheme1,0);

        binding.btnChangeAppIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAppIcon(MainActivity.this);
            }
        });
        binding.btnReturnOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnOld(MainActivity.this);
            }
        });

        binding.btnChangeTheme1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.edit().putInt("theme",0).apply();
                applyTheme(MainActivity.this,theme);
//                recreate();
                Intent intent = new Intent(MainActivity.this, BasicActivity.class);
                startActivity(intent);
            }
        });

        binding.btnChangeTheme2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.edit().putInt("theme",1).apply();
                applyTheme(MainActivity.this,theme);
//                recreate();
                Intent intent = new Intent(MainActivity.this,BasicActivity.class);
                startActivity(intent);
            }
        });

        binding.btnCheckNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                int red= new Random().nextInt(255);
//                int green= new Random().nextInt(255);
//                int blue= new Random().nextInt(255);

//                getSupportActionBar().setBackgroundDrawable(
//                        new ColorDrawable(Color.parseColor("#AA3939")));
//
//                setStatusBarColor("#AA3939");

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
        
        binding.btnPing.setOnClickListener(new View.OnClickListener() {
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

                    binding.tvSpeed.setText(strength);
                }else {
                    if (mTelephonyManager!=null){
                        mTelephonyManager.listen(mPhoneStatelistener, PhoneStateListener.LISTEN_NONE);
                    }
                    Toast.makeText(getApplicationContext(), "Internet Not Working", Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.tvImei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });
    }

    private void checkPermissions() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
//            binding.tvImei.setText(NetworkUtil.getDeviceIMEI(MainActivity.this));
        }
    }

    private void registerNetworkBroadcast() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTelephonyManager!=null)
        mTelephonyManager.listen(mPhoneStatelistener, PhoneStateListener.LISTEN_NONE);
    }

    @Override
    public void bandwidth(int mSignalStrength) {
//        Toast.makeText(getApplicationContext(), "Signal Strength :"+mSignalStrength, Toast.LENGTH_LONG).show();
        String strength = NetworkUtil.NetworkSpeed(MainActivity.this);
        binding.tvSpeed.setText(strength);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        theme = prefs.getInt("theme", 0);
        applyTheme(this,theme);
    }
}