package com.gumio_inf.android.raamemo.model

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import java.util.*

/**
 * Created by gumio_inf on 16/06/19.
 */
//必要なDBを作成
@Table(name = "RaamenItems")
class RaamenItem : Model() {
    @Column(name = "ShopItems")
    var shopItem: ShopItem? = null
    @Column(name = "CreateData")
    var createdDt: Date? = null
    @Column(name = "Name")
    var name: String? = null
    @Column(name = "Taste")
    var taste: String? = null
    @Column(name = "Picture")
    var picture: String? = null
    @Column(name = "Memo")
    var memo: String? = null

    //shopidで関連付け
    //public List<ShopItem> shopItemses() {
    //    return getMany(ShopItem.class, "shopID");
    //};
}
