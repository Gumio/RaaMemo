package com.gumio_inf.android.raamemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gumio_inf on 16/06/20.
 */
public class CardAdapter extends ArrayAdapter<RaamenData> {
    LayoutInflater inflater;

    public  CardAdapter(Context context, int layoutResourceId, List<RaamenData> objects) {
        super(context, layoutResourceId, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        RaamenData item = (RaamenData)getItem(position);

        // convertViewは使い回しされている可能性があるのでnullの時だけ新しく作る
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.card_list, null);
        }

        ImageView iconImageView;
        iconImageView = (ImageView)convertView.findViewById(R.id.icon);
        iconImageView.setImageBitmap(item.getImageData());

        TextView shopTextView;
        TextView locateTextView;
        TextView memoTextView;

        shopTextView = (TextView)convertView.findViewById(R.id.textView);
        locateTextView = (TextView)convertView.findViewById(R.id.textView2);
        memoTextView = (TextView)convertView.findViewById(R.id.textView3);


        shopTextView.setText(item.getTextData());
        locateTextView.setText(item.getTextData2());
        memoTextView.setText(item.getTextData3());

        return convertView;
    }
}
