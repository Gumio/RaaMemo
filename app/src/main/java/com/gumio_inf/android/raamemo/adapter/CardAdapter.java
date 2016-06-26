package com.gumio_inf.android.raamemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gumio_inf.android.raamemo.R;
import com.gumio_inf.android.raamemo.model.RaamenData;

import java.util.List;

/**
 * Created by gumio_inf on 16/06/20.
 */

//Listviewにcardviewを乗せるための手続き
public class CardAdapter extends ArrayAdapter<RaamenData> {
    //別のXMLを取り扱うためのもの
    LayoutInflater inflater;

    //コンストラクタ
    public  CardAdapter(Context context, int layoutResourceId, List<RaamenData> objects) {
        super(context, layoutResourceId, objects);
        //inflaterを取得するにはgetSystemServiceを使わないとダメ
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        RaamenData item = (RaamenData)getItem(position);

        // convertViewは使い回しされている可能性があるのでnullの時だけ新しく作る
        if (convertView == null) {
            //inflaterで指定のxmlのviewを取得する
            convertView = inflater.inflate(R.layout.item_raamen, null);
        }

        //取得後はconvertViewで設定
        ImageView iconImageView;
        iconImageView = (ImageView)convertView.findViewById(R.id.icon);
        iconImageView.setImageBitmap(item.getImageData());

        TextView shopTextView;
        TextView locateTextView;
        TextView memoTextView;

        shopTextView = (TextView)convertView.findViewById(R.id.text_shop);
        locateTextView = (TextView)convertView.findViewById(R.id.text_local);
        memoTextView = (TextView)convertView.findViewById(R.id.text_memo);


        shopTextView.setText(item.getTextData());
        locateTextView.setText(item.getTextData2());
        memoTextView.setText(item.getTextData3());

        //最後にconvertViewは返してあげる
        return convertView;
    }
}
