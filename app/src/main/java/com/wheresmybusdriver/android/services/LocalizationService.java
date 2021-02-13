package com.wheresmybusdriver.android.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.google.firebase.database.FirebaseDatabase;
import com.wheresmybusdriver.android.R;
import com.wheresmybusdriver.android.activitys.LocalizationActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.gson.JsonObject;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.wheresmybusdriver.android.models.RealtimeLocationModel;
import com.wheresmybusdriver.android.utils.Auth;

import java.util.ArrayList;

import static com.wheresmybusdriver.android.utils.App.CHANNEL_ID;


public class LocalizationService extends Service {

    private static final String TAG = null;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private PubNub pubnub;

    private Auth auth = new Auth();

    final private String clientUUID = java.util.UUID.randomUUID().toString();
    private String userId;
    private String theChannel = "Channel-mpn7srpjv";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        userId = auth.getUserId();

        PNConfiguration pnConfiguration = new PNConfiguration();
        // TODO: REPLACE THESE PUB/SUB KEY PLACEHOLDERS WITH YOUR OWN PUB/SUB KEYS
        pnConfiguration.setPublishKey("pub-c-dc5ac8dd-35dc-4127-8ffe-91565a79701c");
        pnConfiguration.setSubscribeKey("sub-c-c6465674-7c6f-11ea-87e8-c6dd1f7701c5");
        pnConfiguration.setUuid(clientUUID);

        pubnub = new PubNub(pnConfiguration);

        requestLocationUpdates();

        Intent notificationIntent = new Intent(this, LocalizationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Serviço Localização")
                .setContentText("Sua localização esta sendo transmitida com sucesso!")
                .setSmallIcon(R.drawable.ic_notificacao_icone)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        return START_NOT_STICKY;
    }



    // OBTÉM A LOCALIZAÇÃO DO USUÁRIO A CADA 5 SEGUNDOS E A SALVA NO BANCO DE DADOS, ALÉM DE
    // ENVIA-LA PARA TODOS OS DISPOSITIVOS QUE ESTEJAM CONECTADOS NO MESMO CANAL DO PUBNUB.
    private void requestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PermissionChecker.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PermissionChecker.PERMISSION_GRANTED) {
            @SuppressLint("RestrictedApi") FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(this);
            @SuppressLint("RestrictedApi") LocationRequest locationRequest = new LocationRequest();

            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setFastestInterval(2000);
            locationRequest.setInterval(5000);

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);

                    Log.e(TAG, "lat:" + locationResult.getLastLocation().getLatitude()
                            + "lng:" + locationResult.getLastLocation().getLongitude());

                    double latitude = 0;
                    double longitude = 0;
                    double altitude = 0;

                    latitude = locationResult.getLastLocation().getLatitude();
                    longitude = locationResult.getLastLocation().getLongitude();

                    String lat, lng;

                    lat = String.valueOf(latitude);
                    lng = String.valueOf(longitude);

                    RealtimeLocationModel realtimeLocationModel = new RealtimeLocationModel(
                            "praiana", userId, lat, lng, "bcitj3"
                    );

                    // ENVIA OS DADOS OBTIDOS COMO LATITUDE, LONGITUDE E ALTURA, ALÉM DOS PRINCIPAIS
                    // DADOS DO MOTORISTA PARA O BANCO DE DADOS.
                    firebaseDatabase.getReference().child("realtime_locations").child(userId)
                            .setValue(realtimeLocationModel);

                    // TODO: SET COMUNICATION OF THE POSITION
                    JsonObject data = new JsonObject();
                    data.addProperty("lat", latitude);
                    data.addProperty("lng", longitude);
                    data.addProperty("alt", altitude);

                    // ENVIA OS DADOS OBTIDOS COMO LATITUDE, LONGITUDE E ALTURA PARA O CANAL DO
                    // PUBNUB.
                    pubnub.publish().message(data).channel(theChannel)
                            .async(new PNCallback<PNPublishResult>() {
                                @Override
                                public void onResponse(PNPublishResult result, PNStatus status) {
                                    if (status.isError()) {
                                        System.out.println("status code: " + status.getStatusCode());
                                    } else
                                        System.out.println("timetoken: " + result.getTimetoken());
                                }
                            });
                    ;
                }
            }, getMainLooper());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
