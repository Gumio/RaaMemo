package com.gumio_inf.android.raamemo.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ListView

import com.activeandroid.query.Select
import com.gumio_inf.android.raamemo.R
import com.gumio_inf.android.raamemo.adapter.RaamenAdapter
import com.gumio_inf.android.raamemo.model.RaamenItem

class MainActivity : AppCompatActivity() {

    private var listView: ListView? = null
    private var raamenList: List<RaamenItem>? = null
    private var adapter: RaamenAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        raamenList = Select().from(RaamenItem::class.java).execute<RaamenItem>()
        adapter = RaamenAdapter(this, (raamenList as MutableList<RaamenItem>?)!!)

        listView = findViewById(R.id.cardList) as ListView?
        //完成したadapterをlistviewに設置してあげる
        listView?.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        raamenList = Select().from(RaamenItem::class.java).execute<RaamenItem>()
        adapter = RaamenAdapter(this, (raamenList as MutableList<RaamenItem>?)!!)

        listView = findViewById(R.id.cardList) as ListView?
        //完成したadapterをlistviewに設置してあげる
        listView?.adapter = adapter
    }

    fun addMemo(v: View) {
        val i = Intent(this, DataInputActivity::class.java)
        startActivity(i)
    }
}
