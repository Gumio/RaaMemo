package com.gumio_inf.android.raamemo;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by gumio_inf on 16/06/19.
 */
@Table(name = "RaamenTable")
public class RaamenItems extends Model {
    @Column(name = "raamenId")
    public Long raamenId;
    @Column(name = "createData")
    public Date createdDt;
    @Column(name = "raamenName")
    public String raamenName;
    @Column(name = "taste")
    public String taste;
    @Column(name = "raamenPicture")
    public String picture;
    @Column(name = "raamenMemo")
    public String raamenMemo;
    @Column(name = "shopID")
    public Long shopId = new ShopItems().shopId;

    public RaamenItems() {
        super();
    }
}
