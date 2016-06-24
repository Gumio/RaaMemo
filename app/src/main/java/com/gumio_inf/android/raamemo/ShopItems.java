package com.gumio_inf.android.raamemo;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by gumio_inf on 16/06/19.
 */

//必要なDBを作成
@Table(name = "ShopTable")
public class ShopItems extends Model {

    //店のID
    @Column(name = "shopId")
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
    @Column(name = "shopLocate")
    public String shopLocate;

    public ShopItems() {
        super();
    }

    public Long getShopId() {
        return shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public Double getShopLatitude() {
        return shopLatitude;
    }

    public Double getShopLongitue() {
        return shopLongitue;
    }

}