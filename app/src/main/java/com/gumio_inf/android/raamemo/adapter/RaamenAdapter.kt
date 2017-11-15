package com.gumio_inf.android.raamemo.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.gumio_inf.android.raamemo.R
import com.gumio_inf.android.raamemo.model.RaamenItem

//Listviewにcardviewを乗せるための手続き
class RaamenAdapter//コンストラクタ
(context: Context, private val mList: List<RaamenItem>) : ArrayAdapter<RaamenItem>(context, R.layout.item_raamen, mList) {

    private var bitmap: Bitmap? = null
    private var mInflater: LayoutInflater? = null

    init {
        mInflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return mList.size
    }

    override fun getItem(position: Int): RaamenItem? {
        return mList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val viewHolder: ViewHolder
        if (convertView == null) {
            convertView = mInflater?.inflate(R.layout.item_raamen, parent, false)
            viewHolder = ViewHolder(convertView!!)
            convertView!!.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        val item = getItem(position)

        if (item!!.picture != null) {
            val b = Base64.decode(item.picture, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.size).copy(Bitmap.Config.RGB_565, true)
            viewHolder.imageView.setImageBitmap(bitmap)
        } else {
            viewHolder.imageView.setImageResource(R.mipmap.raamemoicon)
        }

        viewHolder.shopTextView.text = item.shopItem!!.name
        viewHolder.locateTextView.text = item.shopItem!!.location
        viewHolder.memoTextView.text = item.memo
        // アニメーションをロードする
        val anim = AnimationUtils.loadAnimation(context, R.anim.listview_anim)
        // ListViewのアイテム要素にロードしたアニメーションを実行する
        convertView.startAnimation(anim)
        //最後にconvertViewは返してあげる
        return convertView
    }

     inner class ViewHolder(view: View) {
        var imageView: ImageView
        var shopTextView: TextView
        var locateTextView: TextView
        var memoTextView: TextView

        init {
            imageView = view.findViewById(R.id.icon) as ImageView
            shopTextView = view.findViewById(R.id.text_shop) as TextView
            locateTextView = view.findViewById(R.id.text_local) as TextView
            memoTextView = view.findViewById(R.id.text_memo) as TextView

        }
    }
}
