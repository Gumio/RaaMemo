package com.gumio_inf.android.raamemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Bitmap image;//画像用
    List<RaamenData> objects;//listviewに表示するようのlist
    RaamenData item1, item2, item3, item4, item5, item6;//データ
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // リソースに準備した画像ファイルからBitmapを作成しておく
        image = BitmapFactory.decodeResource(getResources(), R.drawable.raamen);

        // データの作成
        objects = new ArrayList<>();

        // 全て取リだし

        item1 = new RaamenData();
        item1.setImagaData(image);
        item1.setTextData("The first");
        item1.setTextData2("The second");
        item1.setTextData3("The third");

        item2 = new RaamenData();
        item2.setImagaData(image);
        item2.setTextData("The first");
        item2.setTextData2("The second");
        item2.setTextData3("The third");

        item3 = new RaamenData();
        item3.setImagaData(image);
        item3.setTextData("The first");
        item3.setTextData2("The second");
        item3.setTextData3("The third");

        item4 = new RaamenData();
        item4.setImagaData(image);
        item4.setTextData("The first");
        item4.setTextData2("The second");
        item4.setTextData3("The third");

        item5 = new RaamenData();
        item5.setImagaData(image);
        item5.setTextData("The first");
        item5.setTextData2("The second");
        item5.setTextData3("The third");

        item6 = new RaamenData();
        item6.setImagaData(image);
        item6.setTextData("The first");
        item6.setTextData2("The second");
        item6.setTextData3("The third");

        //作成データをlistにぶち込む
        objects.add(item1);
        objects.add(item2);
        objects.add(item3);
        objects.add(item4);
        objects.add(item5);
        objects.add(item6);

        /*ここでlistviewに表示するためにArrayAdapterを継承した
          CardAdapterに作成したlistを引数に渡す
          */
        CardAdapter customAdapter = new CardAdapter(this, 0, objects);


        listView = (ListView)findViewById(R.id.cardList);
        //完成したadapterをlistviewに設置してあげる
        listView.setAdapter(customAdapter);
    }

    public void addMemo(View v) {
        Intent i = new Intent(this, DataInputActivity.class);
        startActivity(i);
    }
}
