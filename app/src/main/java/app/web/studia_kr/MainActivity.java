package app.web.studia_kr;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Timer introtimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //처음 Splash Screen이 띄워지는 시간 설정 타이머(1초)
        introtimer = new Timer();
        introtimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent;

                //FirebaseUser가 현재 로그인 상태인지 확인하여 실행하는 Activity를 나눔
                if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                    intent = new Intent(MainActivity.this, CalendarActivity.class);
                } else {
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}