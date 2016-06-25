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

    Bitmap img1,img2,img3,img4,img5,img6;//画像用

    CardAdapter customAdapter;
    ArrayList<RaamenItems> raamenItemses;
    ArrayList<ShopItems> shopItemses;

    List<RaamenData> objects;//listviewに表示するようのlist
    RaamenData item1, item2, item3, item4, item5, item6;//データ
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // リソースに準備した画像ファイルからBitmapを作成しておく
        img1 = BitmapFactory.decodeResource(getResources(), R.mipmap.ramen1);
        img1 = Bitmap.createScaledBitmap(img1, 300, 300, false);
        img2 = BitmapFactory.decodeResource(getResources(), R.mipmap.ramen10);
        img2 = Bitmap.createScaledBitmap(img2, 300, 300, false);
        img3 = BitmapFactory.decodeResource(getResources(), R.mipmap.ramen3);
        img3 = Bitmap.createScaledBitmap(img3, 300, 300, false);
        img4 = BitmapFactory.decodeResource(getResources(), R.mipmap.ramen11);
        img4 = Bitmap.createScaledBitmap(img4, 300, 300, false);
        img5 = BitmapFactory.decodeResource(getResources(), R.mipmap.ramen7);
        img5 = Bitmap.createScaledBitmap(img5, 300, 300, false);
        img6 = BitmapFactory.decodeResource(getResources(), R.mipmap.ramen12);
        img6 = Bitmap.createScaledBitmap(img6, 300, 300, false);
        // データの作成
        objects = new ArrayList<>();

        // 全て取リだし
        item1 = new RaamenData();
        item1.setImagaData(img1);
        item1.setTextData("宗家一条流 がんこラーメン");
        item1.setTextData2("大阪府大阪市中央区難波千日前10-13");
        item1.setTextData3("不定休で店が狭い");

        item2 = new RaamenData();
        item2.setImagaData(img2);
        item2.setTextData("こがね屋");
        item2.setTextData2("京都府京都市南区東九条西山王町31 京都アバンティ B1F");
        item2.setTextData3("1日限定20食のらーめんがうまい");

        item3 = new RaamenData();
        item3.setImagaData(img3);
        item3.setTextData("人類みな麺類");
        item3.setTextData2("大阪府大阪市淀川区西中島1-12-15");
        item3.setTextData3("チャーシューがえげつないとこ");

        item4 = new RaamenData();
        item4.setImagaData(img4);
        item4.setTextData("らーめん日本");
        item4.setTextData2("大阪府池田市槻木町7-9");
        item4.setTextData3("実は炒飯がすごく有名");

        item5 = new RaamenData();
        item5.setImagaData(img5);
        item5.setTextData("龍旗信RIZE");
        item5.setTextData2("大阪府大阪市浪速区難波中2-10-25");
        item5.setTextData3("濃厚かつ濃厚");

        item6 = new RaamenData();
        item6.setImagaData(img6);
        item6.setTextData("ちゃあしゅうや　亀王");
        item6.setTextData2("京都府京都市下京区烏丸綾小路下る二帖半敷町641");
        item6.setTextData3("チャーシューがうまい");

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
        customAdapter = new CardAdapter(this, 0, objects);


        listView = (ListView)findViewById(R.id.cardList);
        //完成したadapterをlistviewに設置してあげる
        listView.setAdapter(customAdapter);
    }

    public void addMemo(View v) {
        Intent i = new Intent(this, DataInputActivity.class);
        startActivity(i);
    }
}
