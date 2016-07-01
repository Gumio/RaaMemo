package com.gumio_inf.android.raamemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.gumio_inf.android.raamemo.R;
import com.gumio_inf.android.raamemo.adapter.RaamenAdapter;
import com.gumio_inf.android.raamemo.model.RaamenItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    List<RaamenItem> raamenList;
    RaamenAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        raamenList = new Select().from(RaamenItem.class).execute();
        adapter = new RaamenAdapter(this, raamenList);

        listView = (ListView)findViewById(R.id.cardList);
        //完成したadapterをlistviewに設置してあげる
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        raamenList = new Select().from(RaamenItem.class).execute();
        adapter = new RaamenAdapter(this, raamenList);

        listView = (ListView)findViewById(R.id.cardList);
        //完成したadapterをlistviewに設置してあげる
        listView.setAdapter(adapter);
    }

    public void addMemo(View v) {
        Intent i = new Intent(this, DataInputActivity.class);
        startActivity(i);
    }
}
