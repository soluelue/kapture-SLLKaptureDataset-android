package com.sll.sllkapturedataset;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.sll.sllkapturedataset.kapture.io.GlobalPref;

/**
 * @brief Setting
 * @author soluelue
 * @version 1.0
 * @since 2023.04.02
 * */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener{

        private String TAG = getClass().getSimpleName();

        private SwitchPreferenceCompat swShowLog;
        private SwitchPreferenceCompat swShowDepth;

        private CheckBoxPreference chkGNSS;
        private CheckBoxPreference chkLidar;
        private CheckBoxPreference chkWiFi;
        private CheckBoxPreference chkBle;
        private CheckBoxPreference chkAccel;
        private CheckBoxPreference chkGyro;
        private CheckBoxPreference chkMAG;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            swShowLog = (SwitchPreferenceCompat) findPreference(GlobalPref.CONFIG_SHOW_LOG);
            swShowLog.setChecked(GlobalPref.isShowLog());
            swShowLog.setOnPreferenceChangeListener(this);

            swShowDepth = (SwitchPreferenceCompat) findPreference(GlobalPref.CONFIG_USE_DEPTH);
            swShowDepth.setChecked(GlobalPref.isUseDepth());
            swShowDepth.setOnPreferenceChangeListener(this);

            chkGNSS = (CheckBoxPreference) findPreference(GlobalPref.CONFIG_USE_GNSS);
            chkGNSS.setChecked(GlobalPref.isUseGNSS());
            chkGNSS.setOnPreferenceChangeListener(this);

            chkLidar = (CheckBoxPreference) findPreference(GlobalPref.CONFIG_USE_LIDAR);
            chkLidar.setChecked(GlobalPref.isUseLidar());
            chkLidar.setOnPreferenceChangeListener(this);

            chkWiFi = (CheckBoxPreference) findPreference(GlobalPref.CONFIG_USE_WIFI);
            chkWiFi.setChecked(GlobalPref.isUseWiFi());
            chkWiFi.setOnPreferenceChangeListener(this);

            chkBle = (CheckBoxPreference) findPreference(GlobalPref.CONFIG_USE_BLE);
            chkBle.setChecked(GlobalPref.isUseBLE());
            chkBle.setOnPreferenceChangeListener(this);

            chkAccel = (CheckBoxPreference) findPreference(GlobalPref.CONFIG_USE_ACCEL);
            chkAccel.setChecked(GlobalPref.isUseAccel());
            chkAccel.setOnPreferenceChangeListener(this);

            chkGyro = (CheckBoxPreference) findPreference(GlobalPref.CONFIG_USE_GYRO);
            chkGyro.setChecked(GlobalPref.isUseGyro());
            chkGyro.setOnPreferenceChangeListener(this);

            chkMAG = (CheckBoxPreference) findPreference(GlobalPref.CONFIG_USE_MAG);
            chkMAG.setChecked(GlobalPref.isUseMag());
            chkMAG.setOnPreferenceChangeListener(this);

        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onResume() {
            super.onResume();
        }

        @Override
        public void onPause() {
            super.onPause();
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            switch (preference.getKey()){
                case GlobalPref.CONFIG_SHOW_LOG: GlobalPref.setShowLog((boolean) newValue); break;
                case GlobalPref.CONFIG_USE_DEPTH: GlobalPref.setUseDepth((boolean) newValue); break;
                case GlobalPref.CONFIG_USE_GNSS: GlobalPref.setUseGNSS((boolean) newValue); break;
                case GlobalPref.CONFIG_USE_LIDAR: GlobalPref.setUseLidar((boolean) newValue); break;
                case GlobalPref.CONFIG_USE_WIFI: GlobalPref.setUseWiFi((boolean) newValue); break;
                case GlobalPref.CONFIG_USE_BLE: GlobalPref.setUseBLE((boolean) newValue); break;
                case GlobalPref.CONFIG_USE_ACCEL: GlobalPref.setUseAccel((boolean) newValue); break;
                case GlobalPref.CONFIG_USE_GYRO: GlobalPref.setUseGyro((boolean) newValue); break;
                case GlobalPref.CONFIG_USE_MAG: GlobalPref.setUseMag((boolean) newValue); break;
            }
            return true;
        }
    }
}