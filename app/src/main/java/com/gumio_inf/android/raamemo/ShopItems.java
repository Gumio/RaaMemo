package com.gumio_inf.android.raamemo;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by gumio_inf on 16/06/19.
 */
@Table(name = "ShopTable")
public class ShopItems extends Model {

    //店のID！
    @Column(name = "shopID")
    public Long shopId;
    //店の名前
    @Column(name = "shopName")
    public String shopName;
    //店の緯度
    @Column(name = "shopLatitude")
    public Double shopLatitude;
    //店の経度
    @Column(name = "shopLongitue")
    public Double shopLongitue;

    public ShopItems() {
        super();
    }

    public Long getShopId() {
        return shopId;
    }
}