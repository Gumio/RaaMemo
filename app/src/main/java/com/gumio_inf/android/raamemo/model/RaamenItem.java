package com.gumio_inf.android.raamemo.model;

import android.graphics.Bitmap;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by gumio_inf on 16/06/19.
 */
//必要なDBを作成
@Table(name = "RaamenItems")
public class RaamenItem extends Model {
    @Column(name = "ShopItems")
    public ShopItem shopItem;
    @Column(name = "CreateData")
    public Date createdDt;
    @Column(name = "Name")
    public String name;
    @Column(name = "Taste")
    public String taste;
    @Column(name = "Picture")
    public Bitmap picture;
    @Column(name = "Memo")
    public String memo;

    //shopidで関連付け
    //public List<ShopItem> shopItemses() {
    //    return getMany(ShopItem.class, "shopID");
    //};
}
