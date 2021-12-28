package app.web.studia_kr;

import android.content.SharedPreferences;
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

        // 처음 Splash Screen이 띄워지는 시간 설정 타이머(1초)
        introtimer = new Timer();
        introtimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent;

                // 앱을 처음 실행했을 때 약관 동의 페이지로 이동
                final String PREFS_NAME = "PrefsFile";
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                if(!settings.getBoolean("agreed", false)) {
                    // 사용자가 약관에 동의하지 않음
                    intent = new Intent(MainActivity.this, PolicyActivity.class);
                    intent.putExtra("showAgreeBtn", true);
                } else if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                    // 사용자가 로그인되지 않음
                    intent = new Intent(MainActivity.this, CalendarActivity.class);
                } else {
                    // 정상적으로 로그인됨
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}