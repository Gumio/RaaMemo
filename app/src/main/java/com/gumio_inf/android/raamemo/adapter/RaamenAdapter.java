package com.gumio_inf.android.raamemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gumio_inf.android.raamemo.R;
import com.gumio_inf.android.raamemo.model.RaamenItem;

import java.util.List;

//Listviewにcardviewを乗せるための手続き
public class RaamenAdapter extends ArrayAdapter<RaamenItem> {

    Bitmap bitmap;
    private LayoutInflater mInflater;
    private List<RaamenItem> mList;

    //コンストラクタ
    public RaamenAdapter(Context context, List<RaamenItem> objects) {
        super(context, R.layout.item_raamen, objects);
        mInflater = LayoutInflater.from(context);
        mList = objects;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public RaamenItem getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_raamen, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        RaamenItem item = (RaamenItem) getItem(position);

        if (!(item.picture == null)) {
            byte[] b = Base64.decode(item.picture, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length).copy(Bitmap.Config.RGB_565, true);
            viewHolder.imageView.setImageBitmap(bitmap);
        } else {
            viewHolder.imageView.setImageResource(R.mipmap.raamemoicon);
        }

        viewHolder.shopTextView.setText(item.shopItem.name);
        viewHolder.locateTextView.setText(item.shopItem.location);
        viewHolder.memoTextView.setText(item.memo);
        // アニメーションをロードする
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.listview_anim);
        // ListViewのアイテム要素にロードしたアニメーションを実行する
        convertView.startAnimation(anim);
        //最後にconvertViewは返してあげる
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView shopTextView;
        TextView locateTextView;
        TextView memoTextView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.icon);
            shopTextView = (TextView) view.findViewById(R.id.text_shop);
            locateTextView = (TextView) view.findViewById(R.id.text_local);
            memoTextView = (TextView) view.findViewById(R.id.text_memo);

        }
    }
}
