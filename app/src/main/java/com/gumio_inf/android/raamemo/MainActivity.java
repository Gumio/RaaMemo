package com.gumio_inf.android.raamemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // リソースに準備した画像ファイルからBitmapを作成しておく
        Bitmap image;
        image = BitmapFactory.decodeResource(getResources(), R.drawable.raamen);

        // データの作成
        List<RaamenData> objects = new ArrayList<>();
        RaamenData item1 = new RaamenData();
        item1.setImagaData(image);
        item1.setTextData("The first");
        item1.setTextData2("The second");
        item1.setTextData3("The third");

        RaamenData item2 = new RaamenData();
        item2.setImagaData(image);
        item2.setTextData("The first");
        item2.setTextData2("The second");
        item2.setTextData3("The third");

        RaamenData item3 = new RaamenData();
        item3.setImagaData(image);
        item3.setTextData("The first");
        item3.setTextData2("The second");
        item3.setTextData3("The third");

        RaamenData item4 = new RaamenData();
        item4.setImagaData(image);
        item4.setTextData("The first");
        item4.setTextData2("The second");
        item4.setTextData3("The third");

        RaamenData item5 = new RaamenData();
        item5.setImagaData(image);
        item5.setTextData("The first");
        item5.setTextData2("The second");
        item5.setTextData3("The third");

        RaamenData item6 = new RaamenData();
        item6.setImagaData(image);
        item6.setTextData("The first");
        item6.setTextData2("The second");
        item6.setTextData3("The third");


        objects.add(item1);
        objects.add(item2);
        objects.add(item3);
        objects.add(item4);
        objects.add(item5);
        objects.add(item6);

        CardAdapter customAdapter = new CardAdapter(this, 0, objects);

        ListView listView = (ListView)findViewById(R.id.cardList);
        listView.setAdapter(customAdapter);
    }
}
