package app.web.studia_kr;

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

        showDate = getIntent().getStringExtra("date");
        firebaseDate = getIntent().getStringExtra("dbDate");

        ImageButton btBack = findViewById(R.id.btAppBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PolicyActivity.this, AppInfoActivity.class);
                intent.putExtra("date", showDate);
                intent.putExtra("dbDate", firebaseDate);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }
}