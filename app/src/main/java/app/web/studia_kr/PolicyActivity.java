package app.web.studia_kr;

import android.content.SharedPreferences;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class PolicyActivity extends AppCompatActivity {

    private String showDate;
    private String firebaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        ImageButton btBack = findViewById(R.id.btPolicyBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PolicyActivity.this, CalendarActivity.class);
                if(getIntent().getStringExtra("date") != null) {
                    showDate = getIntent().getStringExtra("date");
                    firebaseDate = getIntent().getStringExtra("dbDate");
                    intent.putExtra("date", showDate);
                    intent.putExtra("dbDate", firebaseDate);
                }
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        if(getIntent().getBooleanExtra("showAgreeBtn", false)) {
            // 동의 버튼 보이기
            Button btnAgree = findViewById(R.id.btnAgree);
            btnAgree.setVisibility(View.VISIBLE);
            // 나가기 버튼 숨기기
            btBack.setVisibility(View.INVISIBLE);

            // 동의 버튼을 클릭했을 때
            btnAgree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 사용자가 약관에 동의함, 앞으로 페이지 스킵하기
                    final String PREFS_NAME = "PrefsFile";
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    settings.edit().putBoolean("agreed", true).commit();

                    Intent intent = new Intent(PolicyActivity.this, CalendarActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}