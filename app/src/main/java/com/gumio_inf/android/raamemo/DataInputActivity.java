package com.gumio_inf.android.raamemo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataInputActivity extends AppCompatActivity implements LocationListener {

    double latitude;
    double longitue;

    long shopId = 0;
    long raamenId = 0;

    EditText shop;
    EditText raamen;
    EditText taste;
    EditText locate;
    EditText memo;

    LocationManager mLocationManager;
    Criteria criteria;
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_input);

        // LocationManagerを取得
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Criteriaオブジェクトを生成
        criteria = new Criteria();

        // Accuracyを指定(低精度)
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        // PowerRequirementを指定(低消費電力)
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        // ロケーションプロバイダの取得
        provider = mLocationManager.getBestProvider(criteria, true);

        // LocationListenerを登録
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
        mLocationManager.requestLocationUpdates(provider, 0, 0, this);



        shop = (EditText) findViewById(R.id.editShop);
        raamen = (EditText) findViewById(R.id.editRaamenName);
        locate = (EditText) findViewById(R.id.editLocate);
        taste = (EditText) findViewById(R.id.editTaste);
        memo = (EditText) findViewById(R.id.editMemo);

    }

    @Override
    protected void onPause() {
        super.onPause();

        ShopItems shopItems = new ShopItems();
        RaamenItems raamenItems = new RaamenItems();

        shopItems.shopName = shop.getText().toString();
        shopItems.shopLongitue = longitue;
        shopItems.shopLatitude = latitude;
        shopItems.shopId = shopId++;
        raamenItems.raamenId = raamenId++;
        raamenItems.raamenName = raamen.getText().toString();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.JAPANESE);
        raamenItems.createdDt = sdf.format(date);
        raamenItems.taste = taste.getText().toString();
        raamenItems.raamenMemo = memo.getText().toString();

        raamenItems.save();
        shopItems.save();

        Toast.makeText(getApplicationContext(), "保存しました", Toast.LENGTH_SHORT).show();
    }

    public void getLocateShop(View v) {

        locate.setText(getLocate().toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        // 緯度の表示
        latitude = location.getLatitude();

        // 経度の表示
        longitue = location.getLongitude();

        Log.d("latitude", String.valueOf(latitude));
        Log.d("longitue", String.valueOf(longitue));
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    protected String getLocate() {
        String ret = "";
        try {

            Geocoder gcd = new Geocoder(this, Locale.JAPAN);
            List<Address> addresses = gcd.getFromLocation(latitude, longitue, 1);
            if (!addresses.isEmpty()) {
                ret = addresses.get(0).getAddressLine(1);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
