package com.gumio_inf.android.raamemo.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.gumio_inf.android.raamemo.R;
import com.gumio_inf.android.raamemo.adapter.RaamenAdapter;
import com.gumio_inf.android.raamemo.model.RaamenItem;
import com.gumio_inf.android.raamemo.service.GeofenceService;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSION = 1000;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<RaamenItem> raamenList = new Select().from(RaamenItem.class).execute();
        RaamenAdapter adapter = new RaamenAdapter(this, raamenList);

        listView = (ListView)findViewById(R.id.cardList);
        //完成したadapterをlistviewに設置してあげる
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<RaamenItem> raamenList = new Select().from(RaamenItem.class).execute();
        RaamenAdapter adapter = new RaamenAdapter(this, raamenList);

        listView = (ListView)findViewById(R.id.cardList);
        //完成したadapterをlistviewに設置してあげる
        listView.setAdapter(adapter);
    }

    public void addMemo(View v) {
        Intent i = new Intent(this, DataInputActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.menu_recommend, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(R.id.create_recomend == id) {
            new AlertDialog.Builder(this)
                    .setTitle("おすすめラーメン")
                    .setMessage("この機能はあなたの近くのおすすめのラーメン屋を紹介するよ\n")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            locatePermission();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast toast = Toast.makeText(getApplicationContext(), "でわ、おすすめできません・・・", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    })
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void locatePermission() {
        // Android 6, API 23以上でパーミッシンの確認
        if(Build.VERSION.SDK_INT >= 23){
            checkPermission();
        }
        else{
            locationActivity();
        }
    }

    // 位置情報許可の確認
    public void checkPermission() {
        // 既に許可している
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            locationActivity();
        }
        // 拒否していた場合
        else{
            requestLocationPermission();
        }
    }

    // 許可を求める
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION);

        } else {
            Toast toast = Toast.makeText(this, "でわ、おすすめできません・・・", Toast.LENGTH_SHORT);
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
                locationActivity();
                return;

            } else {
                // それでも拒否された時の対応
                Toast toast = Toast.makeText(this, "でわ、おすすめできません・・・", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    // Intent でLocation
    private void locationActivity() {
        Intent intent = new Intent(getApplication(), GeofenceService.class);
        startService(intent);
    }

}
