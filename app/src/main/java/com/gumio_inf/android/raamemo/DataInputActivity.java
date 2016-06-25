package com.gumio_inf.android.raamemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataInputActivity extends AppCompatActivity implements LocationListener {

    static final int REQUEST_CODE_CAMERA = 1;
    static final int REQUEST_CODE_GALLERY = 2;
    public Bitmap photo;

    double latitude;
    double longitue;

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
        shop = (EditText) findViewById(R.id.editShop);
        raamen = (EditText) findViewById(R.id.editRaamenName);
        locate = (EditText) findViewById(R.id.editLocate);
        taste = (EditText) findViewById(R.id.editTaste);
        memo = (EditText) findViewById(R.id.editMemo);


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
    }

    public void getLocateShop(View v) {
        locate.setText(getLocate());
    }

    @Override
    public void onLocationChanged(Location location) {
        // 緯度の取得
        latitude = location.getLatitude();

        // 経度の取得
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

    public void onCameraUp(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    public void onPhotoUp(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(resultCode == RESULT_OK) {
            if(requestCode == REQUEST_CODE_GALLERY) {

                try {
                    InputStream inputStream = getContentResolver().openInputStream(intent.getData());
                    photo = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                    Toast.makeText(getApplicationContext(), "アップロードできました", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "エラー", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_CODE_CAMERA) {
                photo = (Bitmap)intent.getExtras().get("data");
                Toast.makeText(getApplicationContext(), "アップロードできました", Toast.LENGTH_SHORT).show();
            }
        } else if(resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "CANCEL", Toast.LENGTH_SHORT).show();
        }
    }

    public void test(View v) {
        Log.d("picture:", String.valueOf(photo));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_memo_save, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(R.id.create_save == id) {
            saveMemo();
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void saveMemo() {
        ShopItems shopItems = new ShopItems();
        RaamenItems raamenItems = new RaamenItems();
        //店の情報
        shopItems.shopName = shop.getText().toString();
        shopItems.shopLongitue = longitue;
        shopItems.shopLatitude = latitude;
        Log.d("shopName", shopItems.shopName);
        Log.d("shopLongitue", String.valueOf(shopItems.shopLongitue));
        Log.d("shopLatitude", String.valueOf(shopItems.shopLatitude));

        //ラーメンの情報
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.JAPANESE);
        raamenItems.raamenName = raamen.getText().toString();
        raamenItems.createdDt = sdf.format(date);
        raamenItems.picture = String.valueOf(photo);
        raamenItems.taste = taste.getText().toString();
        raamenItems.raamenMemo = memo.getText().toString();
        Log.d("raamenName", raamenItems.raamenName);
        Log.d("createDt", raamenItems.createdDt);
        Log.d("taste", raamenItems.taste);
        Log.d("raamenMemo", raamenItems.raamenMemo);
         //保存
        raamenItems.save();
        shopItems.save();
        Toast.makeText(getApplicationContext(), "保存しました", Toast.LENGTH_SHORT).show();
    }
}
