package app.web.studia_kr;

import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

import app.web.studia_kr.network.NetworkConnection;

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
                SharedPreferences settings = getSharedPreferences("PrefsFile", 0);

                if(!settings.getBoolean("agreed", false)) {
                    // 사용자가 약관에 동의하지 않음
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra("showAgreeBtn", true);
                } else if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                    // 사용자가 로그인됨
                    intent = new Intent(MainActivity.this, CalendarActivity.class);
                    if (!NetworkConnection.isConnected(getApplicationContext())) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "연결 가능한 네트워크를 찾을 수 없습니다. 오프라인 지속성을 자동 실행합니다.", Toast.LENGTH_SHORT).show();
                            }
                        }, 0);
                    }
                } else {
                    // 정상적으로 로그인되지 않음
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}