package app.web.studia_kr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import app.web.studia_kr.backgroundservice.NotificationRestarter;

public class PopupActivity extends AppCompatActivity {

    private String showDate;
    private String firebaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView(R.layout.activity_popup);

        showDate = getIntent().getStringExtra("date");
        firebaseDate = getIntent().getStringExtra("dbDate");

        TextView tvEmail = findViewById(R.id.tvEmail);
        Button btLogout = findViewById(R.id.btLogout);
        Button btpasswordch = findViewById(R.id.btIforgot);
        ImageButton btClose = findViewById(R.id.btClose);
        Button btSettings = findViewById(R.id.btSettings);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        tvEmail.setText(user.getEmail());

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();

                Intent intent = new Intent(PopupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btpasswordch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://studia.blue/iforgot/"));
                startActivity(intent);
            }
        });

        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PopupActivity.this, CalendarActivity.class);
                intent.putExtra("date", showDate);
                intent.putExtra("dbDate", firebaseDate);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PopupActivity.this, AppInfoActivity.class);
                intent.putExtra("date", showDate);
                intent.putExtra("dbDate", firebaseDate);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }
}