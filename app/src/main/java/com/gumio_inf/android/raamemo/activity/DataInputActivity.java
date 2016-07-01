package com.gumio_inf.android.raamemo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataInputActivity extends AppCompatActivity implements android.location.LocationListener {
    static final int REQUEST_CODE_CAMERA = 1;
    static final int REQUEST_CODE_GALLERY = 2;
    private final int REQUEST_PERMISSION = 1000;
    private LocationManager locationManager;

    private static final int MinTime = 1000;
    private static final float MinDistance = 3;
    public Bitmap photo;

    double latitude;
    double longitued;

    EditText shop;
    EditText raamen;
    EditText taste;
    EditText locate;
    EditText memo;
    ImageView picture;

    String bitmapStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_input);
        // LocationManager インスタンス生成
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        shop = (EditText) findViewById(R.id.editShop);
        raamen = (EditText) findViewById(R.id.editRaamenName);
        locate = (EditText) findViewById(R.id.editLocate);
        taste = (EditText) findViewById(R.id.editTaste);
        memo = (EditText) findViewById(R.id.editMemo);
        picture = (ImageView) findViewById(R.id.icon);
    }

    public void getLocateShop(View v) {
        Toast.makeText(this, "少しまってね〜", Toast.LENGTH_LONG).show();
        // Android 6, API 23以上でパーミッシンの確認
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();
        } else {
            locationStart();
        }
    }

    // 位置情報許可の確認
    public void checkPermission() {
        // 既に許可している
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationStart();
        }
        // 拒否していた場合
        else {
            requestLocationPermission();
        }
    }

    // 許可を求める
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION);

        } else {
            Toast toast = Toast.makeText(this, "許可してくれないとわからないよ〜", Toast.LENGTH_SHORT);
            toast.show();

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, REQUEST_PERMISSION);

        }
    }

    // 結果の受け取り
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "場所によっては取得できないよ！！", Toast.LENGTH_LONG).show();
                locationStart();
                return;

            } else {
                // それでも拒否された時の対応
                Toast toast = Toast.makeText(this, "なんで拒否するの！？", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private void locationStart() {
        // LocationManager インスタンス生成
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        startGPS();
    }

    protected void startGPS() {
        Log.d("LocationActivity", "gpsEnabled");
        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            // GPSを設定するように促す
            enableLocationSettings();
        }

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

                enableLocationSettings();
            }
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (locationManager != null) {
            Log.d("LocationActivity", "locationManager.removeUpdates");
            // update を止める
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
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitued = location.getLongitude();

        locate.setText(getLocate());
        stopGPS();
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    private void enableLocationSettings() {
        Toast.makeText(this, "GPSをオンにしてね！", Toast.LENGTH_LONG).show();
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }

    private void stopGPS(){
        if (locationManager != null) {
            Log.d("LocationActivity", "onStop()");
            // update を止める
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(this);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        stopGPS();
    }

    protected String getLocate() {
        String ret = "";
        try {
            Geocoder gcd = new Geocoder(this, Locale.JAPAN);
            List<Address> addresses = gcd.getFromLocation(latitude, longitued, 1);
            if (addresses.size() > 0) {
                ret = addresses.get(0).getAddressLine(1);
                if(ret.length() > 0) {
                    ret = ret.substring(10);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("現在地:", ret);
        return ret;
    }

    //カメラで撮影した情報を渡す
    public void onCameraUp(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    //ギャラリーから選択された情報を渡す
    public void onPhotoUp(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    //暗黙的インテントで選択された時に呼ばれる
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_GALLERY) {
                //ギャラリーから画像を取得したとき
                try {
                    InputStream inputStream = getContentResolver().openInputStream(intent.getData());
                    // 画像サイズ情報を取得する
                    BitmapFactory.Options imageOptions = new BitmapFactory.Options();
                    imageOptions.inPreferredConfig = Bitmap.Config.RGB_565;
                    imageOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(inputStream, null, imageOptions);

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
                    } else {
                        photo = BitmapFactory.decodeStream(inputStream);
                    }

                    inputStream.close();

                } catch(Exception e) {
                    Toast.makeText(getApplicationContext(), "エラー", Toast.LENGTH_SHORT).show();
                }

                //BitmapをStringにしてDBに格納できるようにする
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
                bitmapStr = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                Toast.makeText(getApplicationContext(), "アップロードしたよ！", Toast.LENGTH_SHORT).show();

            } else if (requestCode == REQUEST_CODE_CAMERA) {
                //カメラで撮影された時の対処
                Bitmap photo = (Bitmap)intent.getExtras().get("data");
                Bitmap photoRe = Bitmap.createScaledBitmap(photo, 300, 300, false);

                //BitmapをStringにしてDBに格納できるようにする
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photoRe.compress(Bitmap.CompressFormat.PNG, 100, baos);
                bitmapStr = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                Toast.makeText(getApplicationContext(), "アップロードしたよ！", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "CANCEL", Toast.LENGTH_SHORT).show();
        }
    }

    //保存ボタンをメニューバーに設置
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_memo_save, menu);
        return true;
    }

    //保存ボタンが押されたことを検知する
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

    //DBへの格納
    void saveMemo() {
        ShopItem shopItem = new ShopItem();
        //店の情報
        shopItem.name = shop.getText().toString();
        shopItem.longitude = longitued;
        shopItem.latitude = latitude;
        shopItem.location = locate.getText().toString();
        shopItem.save();

        //ラーメンの情報
        RaamenItem raamenItem = new RaamenItem();
        raamenItem.name = raamen.getText().toString();
        raamenItem.createdDt = new Date();
        raamenItem.picture = bitmapStr;
        raamenItem.taste = taste.getText().toString();
        raamenItem.memo = memo.getText().toString();
        raamenItem.shopItem = shopItem;

        //保存
        raamenItem.save();
        Toast.makeText(getApplicationContext(), "保存しました", Toast.LENGTH_SHORT).show();
    }
}
