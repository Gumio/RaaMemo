package com.gumio_inf.android.raamemo.activity;

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
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gumio_inf.android.raamemo.R;
import com.gumio_inf.android.raamemo.model.RaamenItem;
import com.gumio_inf.android.raamemo.model.ShopItem;

import java.io.ByteArrayOutputStream;
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
    double longitued;

    EditText shop;
    EditText raamen;
    EditText taste;
    EditText locate;
    EditText memo;
    ImageView picture;

    LocationManager mLocationManager;
    Criteria criteria;
    String provider;

    String bitmapStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_input);

        shop = (EditText) findViewById(R.id.editShop);
        raamen = (EditText) findViewById(R.id.editRaamenName);
        locate = (EditText) findViewById(R.id.editLocate);
        taste = (EditText) findViewById(R.id.editTaste);
        memo = (EditText) findViewById(R.id.editMemo);
        picture = (ImageView) findViewById(R.id.icon);


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
        longitued = location.getLongitude();

        Log.d("latitude", String.valueOf(latitude));
        Log.d("longitue", String.valueOf(longitued));
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
            List<Address> addresses = gcd.getFromLocation(latitude, longitued, 1);
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
        Log.d("Camera = ", String.valueOf(intent));
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    public void onPhotoUp(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Log.d("Photo = ", String.valueOf(intent));
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_GALLERY) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(intent.getData());
                    // 画像サイズ情報を取得する
                    BitmapFactory.Options imageOptions = new BitmapFactory.Options();
                    imageOptions.inPreferredConfig = Bitmap.Config.RGB_565;
                    imageOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(inputStream, null, imageOptions);
                    Log.v("image", "Original Image Size: " + imageOptions.outWidth + " x " + imageOptions.outHeight);

                    inputStream.close();

                    // もし、画像が大きかったら縮小して読み込む
                    //  今回はimageSizeMaxの大きさに合わせる
                    int imageSizeMax = 500;
                    inputStream = getContentResolver().openInputStream(intent.getData());
                    float imageScaleWidth = (float)imageOptions.outWidth / imageSizeMax;
                    float imageScaleHeight = (float)imageOptions.outHeight / imageSizeMax;

                    // もしも、縮小できるサイズならば、縮小して読み込む
                    if (imageScaleWidth > 2 && imageScaleHeight > 2) {
                        BitmapFactory.Options imageOptions2 = new BitmapFactory.Options();

                        // 縦横、小さい方に縮小するスケールを合わせる
                        int imageScale = (int)Math.floor((imageScaleWidth > imageScaleHeight ? imageScaleHeight : imageScaleWidth));

                        // inSampleSizeには2のべき上が入るべきなので、imageScaleに最も近く、かつそれ以下の2のべき上の数を探す
                        for (int i = 2; i <= imageScale; i *= 2) {
                            imageOptions2.inSampleSize = i;
                        }

                        photo = BitmapFactory.decodeStream(inputStream, null, imageOptions2);
                        Log.v("image", "Sample Size: 1/" + imageOptions2.inSampleSize);
                    } else {
                        photo = BitmapFactory.decodeStream(inputStream);
                    }

                    inputStream.close();

                } catch(Exception e) {
                    Toast.makeText(getApplicationContext(), "エラー", Toast.LENGTH_SHORT).show();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
                bitmapStr = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                Log.d("photo:", bitmapStr);

            } else if (requestCode == REQUEST_CODE_CAMERA) {

                photo = (Bitmap) intent.getExtras().get("data");
                Log.d("photo:", String.valueOf(photo));

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
                bitmapStr = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                Log.d("photo:", bitmapStr);
                Toast.makeText(getApplicationContext(), "アップロードできました", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == RESULT_CANCELED) {
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

        if (R.id.create_save == id) {
            saveMemo();
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void saveMemo() {
        ShopItem shopItem = new ShopItem();
        //店の情報
        shopItem.name = shop.getText().toString();
        shopItem.longitude = longitued;
        shopItem.latitude = latitude;
        shopItem.save();
        Log.d("shopName", shopItem.name);
        Log.d("shopLongitue", String.valueOf(shopItem.longitude));
        Log.d("shopLatitude", String.valueOf(shopItem.latitude));

        //ラーメンの情報
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.JAPANESE);
        RaamenItem raamenItem = new RaamenItem();
        raamenItem.name = raamen.getText().toString();
        raamenItem.createdDt = new Date();
        raamenItem.picture = bitmapStr;
        raamenItem.taste = taste.getText().toString();
        raamenItem.memo = memo.getText().toString();
        raamenItem.shopItem = shopItem;
        //Log.d("raamenName", raamenItem.name);
        //Log.d("createDt", sdf.format(raamenItem.createdDt));
        //Log.d("taste", raamenItem.taste);
        //Log.d("raamenMemo", raamenItem.memo);
        //Log.d("picture", raamenItem.picture);
        //保存
        raamenItem.save();
        Toast.makeText(getApplicationContext(), "保存しました", Toast.LENGTH_SHORT).show();
    }
}
