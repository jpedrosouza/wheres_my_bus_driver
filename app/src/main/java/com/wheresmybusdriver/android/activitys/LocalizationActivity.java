package com.wheresmybusdriver.android.activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.wheresmybusdriver.android.R;
import com.wheresmybusdriver.android.services.LocalizationService;

import java.util.ArrayList;


public class LocalizationActivity<global> extends AppCompatActivity {

    private static final String TAG = null;
    private TextView mensagemStatus;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localization);

        mensagemStatus = findViewById(R.id.textStatus);
    }

    public void startService(View v) {
        callPermissions();
    }

    @SuppressLint("SetTextI18n")
    public void stopService(View v) {
        Intent serviceIntent = new Intent(this, LocalizationService.class);
        stopService(serviceIntent);
        mensagemStatus.setText("DESLIGADO");
    }

    private void callPermissions() {
        Permissions.check(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                "É necessário que você autorize o uso de sua Localização", new Permissions.Options()
                        .setSettingsDialogTitle("Marning!").setRationaleDialogTitle("Atenção!"),
                new PermissionHandler() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onGranted() {
                        //do your task
                        Intent serviceIntent = new Intent(LocalizationActivity.this, LocalizationService.class);
                        startService(serviceIntent);
                        mensagemStatus.setText("LIGADO");
                    }

                    @Override
                    public void onDenied(Context context, ArrayList<String> deniedPermissons) {
                        super.onDenied(context, deniedPermissons);
                        callPermissions();
                    }

                });
    }

    public void sairLogin(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}

