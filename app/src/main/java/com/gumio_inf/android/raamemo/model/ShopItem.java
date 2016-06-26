package com.gumio_inf.android.raamemo.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by gumio_inf on 16/06/19.
 */

//必要なDBを作成
@Table(name = "ShopItems")
public class ShopItem extends Model {
    //店の名前
    @Column(name = "Name")
    public String name;
    //店の緯度
    @Column(name = "Latitude")
    public Double latitude;
    //店の経度
    @Column(name = "Longitude")
    public Double longitude;
}