package app.web.studia_kr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import app.web.studia_kr.backgroundservice.NotificationRestarter;

public class AppInfoActivity extends AppCompatActivity {

    private TextView tvCopyright;
    private Button btPolicy;
    private Button btPrivatePolicy;
    private Button btOpenSource;
    private ImageButton btBack;
    private String showDate;
    private String firebaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        tvCopyright = findViewById(R.id.tvCopyright);
        Calendar calendar = Calendar.getInstance();
        DateFormat yearFormat = new SimpleDateFormat("yyyy");
        tvCopyright.setText("Copyright © 2021-" + yearFormat.format(calendar.getTime()) + " App Developers. All Rights Reserved.");

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
                overridePendingTransition(0, 0);
                finish();
            }
        });

        btBack = findViewById(R.id.btBack);
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

        //TODO Add account delete function
    }

    @Override
    protected void onDestroy() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartService");
        broadcastIntent.setClass(this, NotificationRestarter.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }
}