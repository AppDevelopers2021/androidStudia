package app.web.studia_kr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

public class AppInfoActivity extends AppCompatActivity {

    private TextView tvVersion;
    private Button btPolicy;
    private Button btPrivatePolicy;
    private Button btOpenSource;
    private ImageButton btBack;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        SharedPreferences settings = getSharedPreferences("PrefsFile", 0);
        tvVersion = findViewById(R.id.tvVersion);
        tvVersion.setText(settings.getString("version", "VERSION UNKNOWN"));

        btPolicy = findViewById(R.id.btPolicy);
        btPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AppInfoActivity.this, PolicyActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        btPrivatePolicy = findViewById(R.id.btPrivatePolicy);
        btPrivatePolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AppInfoActivity.this, PolicyActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        btOpenSource = findViewById(R.id.btLicense);
        btOpenSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OssLicensesMenuActivity.setActivityTitle("Studia Android Open Source License");
                startActivity(new Intent(getApplicationContext(), OssLicensesMenuActivity.class));
            }
        });

        btBack = findViewById(R.id.btAppBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AppInfoActivity.this, CalendarActivity.class));
                overridePendingTransition(0, 0);
            }
        });
    }
}