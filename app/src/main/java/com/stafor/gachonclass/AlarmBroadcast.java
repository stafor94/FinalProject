package com.stafor.gachonclass;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by 황우상 on 2017-06-02.
 */

public class AlarmBroadcast extends BroadcastReceiver {
    String INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED;
    DBHelper_Profile dbHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        Uri soundUri= RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.gachon)
                .setTicker("가천 Class 알림")
                .setWhen(System.currentTimeMillis())
                .setContentTitle("수업 알림")
                .setContentText("설정한 시간이 되었습니다!")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        boolean sound, vibe;
        sound = (dbHelper.printData(4).equals("1") ? true:false);
        vibe = (dbHelper.printData(5).equals("1") ? true:false);

        if (sound && !vibe) {  // 소리on, 진동off
            builder.setSound(soundUri);
            Toast.makeText(context, "소리 on, 진동 off", Toast.LENGTH_SHORT).show();
        }
        else if (!sound && vibe) { //소리off, 진동on
            builder.setVibrate(new long[]{100, 200, 50, 200, 300, 200, 50, 200, 200, 500});
            Toast.makeText(context, "소리 off, 진동 on", Toast.LENGTH_SHORT).show();
        }
        else if (sound && vibe) { //소리on, 진동on
            builder.setSound(soundUri);
            builder.setVibrate(new long[]{100,200, 50,200, 300, 200, 50, 200, 200, 300});
            Toast.makeText(context, "소리 on, 진동 on", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "소리 off, 진동 off", Toast.LENGTH_SHORT).show();
        }
        notificationmanager.notify(1, builder.build());
    }
}
