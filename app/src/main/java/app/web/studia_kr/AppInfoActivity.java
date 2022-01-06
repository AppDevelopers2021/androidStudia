package app.web.studia_kr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AppInfoActivity extends AppCompatActivity {

    private TextView tvVersion;
    private TextView tvCopyright;
    private Button btPolicy;
    private Button btPrivatePolicy;
    private Button btOpenSource;
    private ImageButton btBack;
    private SharedPreferences settings;
    private String showDate;
    private String firebaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        showDate = getIntent().getStringExtra("date");
        firebaseDate = getIntent().getStringExtra("dbDate");

        settings = getSharedPreferences("PrefsFile", 0);
        tvVersion = findViewById(R.id.tvVersion);
        tvVersion.setText(settings.getString("version", "VERSION UNKNOWN"));

        Calendar calendar = Calendar.getInstance();
        DateFormat yearOnly = new SimpleDateFormat("yyyy");
        tvCopyright = findViewById(R.id.tvCopyright);
        tvCopyright.setText("Copyright © " + yearOnly.format(calendar.getTime()) +" App Developers. All Rights Reserved.");

        btPolicy = findViewById(R.id.btPolicy);
        btPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AppInfoActivity.this, PolicyActivity.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });

        btPrivatePolicy = findViewById(R.id.btPrivatePolicy);
        btPrivatePolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AppInfoActivity.this, PolicyActivity.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });

        btOpenSource = findViewById(R.id.btLicense);
        btOpenSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OssLicensesMenuActivity.setActivityTitle("Studia Android Open Source License");
                startActivity(new Intent(getApplicationContext(), OssLicensesMenuActivity.class));
                finish();
            }
        });

        btBack = findViewById(R.id.btAppBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppInfoActivity.this, CalendarActivity.class);
                intent.putExtra("date", showDate);
                intent.putExtra("dbDate", firebaseDate);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }
}