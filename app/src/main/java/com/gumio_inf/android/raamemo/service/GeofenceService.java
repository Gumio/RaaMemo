package com.gumio_inf.android.raamemo.service;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.gumio_inf.android.raamemo.activity.MainActivity;

import java.util.ArrayList;

/**
 * Created by gumio_inf on 16/06/25.
 */
public class GeofenceService extends Service implements LocationListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    final static String TAG = "GeoService";
    GoogleApiClient locationClient;
    ArrayList<Geofence> ramenGeofenceList;
    double latitude = 34.778739;
    double longuitued = 135.390513;
    float radius = 500;

    private LocationManager locationManager;

    private static final int MinTime = 1000;
    private static final float MinDistance = 1;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // LocationManager インスタンス生成
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        startGPS();
        locationClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationClient.connect();

        Log.d(TAG, "onCreate");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    protected void startGPS() {

        Log.d("LocationActivity", "gpsEnabled");

        if (locationManager != null) {
            Log.d("LocationActivity", "locationManager.requestLocationUpdates");
            // バックグラウンドから戻ってしまうと例外が発生する場合がある
            try {
                // minTime = 1000msec, minDistance = 50m
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MinTime, MinDistance, this);
            } catch (Exception e) {
                e.printStackTrace();

                Toast toast = Toast.makeText(this, "例外が発生、位置情報のPermissionを許可していますか？", Toast.LENGTH_SHORT);
                toast.show();

                //MainActivityに戻す
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Latitude=", String.valueOf(location.getLatitude()));
        Log.d("Longitude=", String.valueOf(location.getLongitude()));


        // Get the estimated accuracy of this location, in meters.
        // We define accuracy as the radius of 68% confidence. In other words,
        // if you draw a circle centered at this location's latitude and longitude,
        // and with a radius equal to the accuracy, then there is a 68% probability
        // that the true location is inside the circle.
        Log.d("Accuracy=", String.valueOf(location.getAccuracy()));
        Log.d("Altitude=", String.valueOf(location.getAltitude()));
        Log.d("Time=", String.valueOf(location.getTime()));
        Log.d("Speed=", String.valueOf(location.getSpeed()));

        // Get the bearing, in degrees.
        // Bearing is the horizontal direction of travel of this device,
        // and is not related to the device orientation.
        // It is guaranteed to be in the range (0.0, 360.0] if the device has a bearing.
        Log.d("Bearing=", String.valueOf(location.getBearing()));
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("Connected:", "OK");
        ramenGeofenceList = new ArrayList<>();
        Geofence geofence = new Geofence.Builder()
                .setRequestId("RamenShop-1")
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setCircularRegion(latitude, longuitued, radius)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
        ramenGeofenceList.add(geofence);

        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofence(geofence);

        Intent intent = new Intent(this , NotificationService.class);
        startService(intent);
        Log.d("Gefence:", "enter");
        PendingIntent pendingIntent = PendingIntent.getService(this, 0,
                                    intent, PendingIntent.FLAG_UPDATE_CURRENT);

        LocationServices.GeofencingApi.addGeofences(locationClient, ramenGeofenceList, pendingIntent);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
