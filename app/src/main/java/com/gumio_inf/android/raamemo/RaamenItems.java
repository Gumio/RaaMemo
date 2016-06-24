package com.gumio_inf.android.raamemo;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by gumio_inf on 16/06/19.
 */
//必要なDBを作成
@Table(name = "RaamenTable")
public class RaamenItems extends Model {
    @Column(name = "raamenId")
    public Long raamenId;
    @Column(name = "shopId")
    public Long shopId;
    @Column(name = "createData")
    public String createdDt;
    @Column(name = "raamenName")
    public String raamenName;
    @Column(name = "taste")
    public String taste;
    @Column(name = "raamenPicture")
    public String picture;
    @Column(name = "raamenMemo")
    public String raamenMemo;

    //shopidで関連付け
    //public List<ShopItems> shopItemses() {
    //    return getMany(ShopItems.class, "shopID");
    //};

    public RaamenItems() {
        super();
    }

    public Long getRaamenId() {
        return raamenId;
    }

    public String getCreatedDt() {
        return createdDt;
    }

    public String getRaamenName() {
        return raamenName;
    }

    public String getTaste() {
        return taste;
    }

    public String getPicture() {
        return picture;
    }

    public String getRaamenMemo() {
        return raamenMemo;
    }
}
