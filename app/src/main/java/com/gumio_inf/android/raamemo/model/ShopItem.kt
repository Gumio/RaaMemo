package com.gumio_inf.android.raamemo.model

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table

/**
 * Created by gumio_inf on 16/06/19.
 */

//必要なDBを作成
@Table(name = "ShopItems")
class ShopItem : Model() {
    //店の名前
    @Column(name = "Name")
    var name: String? = null
    //店の緯度
    @Column(name = "Latitude")
    var latitude: Double? = null
    //店の経度
    @Column(name = "Longitude")
    var longitude: Double? = null
    @Column(name = "Location")
    var location: String? = null
}