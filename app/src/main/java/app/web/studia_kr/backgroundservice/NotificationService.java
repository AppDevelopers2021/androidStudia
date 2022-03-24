package app.web.studia_kr.backgroundservice;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import app.web.studia_kr.MainActivity;
import app.web.studia_kr.R;

public class NotificationService extends IntentService {
    public boolean isTodayScheduleExists;
    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        FirebaseDatabase.getInstance().getReference()
                .child("calendar").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance())))
                    isTodayScheduleExists = true;
                else
                    isTodayScheduleExists = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                isTodayScheduleExists = false;
            }
        });

        if (isTodayScheduleExists) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "StudiaNotify")
                    .setSmallIcon(R.drawable.iconstudia)
                    .setContentTitle("스튜디아에서 오늘 일정을 확인하세요!")
                    .setContentText("오늘도 스튜디아와 함께해요 ❤")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .setContentIntent(PendingIntent.getActivity(this, 2, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));

            NotificationManagerCompat.from(this).notify(3, builder.build());
        }
    }
}
