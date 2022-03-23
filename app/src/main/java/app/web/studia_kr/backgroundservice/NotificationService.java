package app.web.studia_kr.backgroundservice;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import app.web.studia_kr.R;

public class NotificationService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartService");
        broadcastIntent.setClass(this, NotificationRestarter.class);
        this.sendBroadcast(broadcastIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            Thread.sleep(60000);

            int hour = Integer.parseInt(new SimpleDateFormat("HH").format(Calendar.getInstance()));
            int minute = Integer.parseInt(new SimpleDateFormat("mm").format(Calendar.getInstance()));
            int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

            if (day != 6 || day != 7) {
                if (hour == 7 && minute == 30) {
                    new Notification.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle("스튜디아에서 오늘 일정을 확인하세요!")
                            .setContentText("오늘도 스튜디아와 함께해요! ❤")
                            .setDefaults(Notification.DEFAULT_VIBRATE)
                            .setAutoCancel(true);
                }
            } else {
                if (hour == 8 && minute == 30) {
                    new Notification.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle("스튜디아에서 오늘 일정을 확인하세요!")
                            .setContentText("오늘도 스튜디아와 함께해요! ❤")
                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                            .setAutoCancel(true);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
