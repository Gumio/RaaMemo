package com.gumio_inf.android.raamemo.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.gumio_inf.android.raamemo.R;

/**
 * Created by gumio_inf on 16/06/25.
 */
public class NotificationService extends Service{

    final static String TAG = "NotificationService";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://ramendb.supleks.jp/"));

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("らーめも")
                .setContentText("そこにこんなおすすめ店あるよ！！")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(1000, notification);

        Log.d("Notification", "OK!");
    }
}
