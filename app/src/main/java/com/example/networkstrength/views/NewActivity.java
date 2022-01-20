package com.example.networkstrength.views;

import static com.example.networkstrength.utilities.ViewUtils.applyTheme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.example.networkstrength.R;
import com.example.networkstrength.views.fragments.FirstFragment;

public class NewActivity extends AppCompatActivity {

    private Button mBtnChangeTheme;
    private int theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        theme = prefs.getInt("theme", 0);

        applyTheme(this,theme);

        setContentView(R.layout.activity_new2);

        loadFragment(new FirstFragment());

        mBtnChangeTheme = findViewById(R.id.btn_change_theme);

        mBtnChangeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragments, fragment)
                .addToBackStack(null)
                .commit();
    }
}