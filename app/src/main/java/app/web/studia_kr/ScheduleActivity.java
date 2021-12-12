package app.web.studia_kr;

import android.app.ActivityOptions;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ScheduleActivity extends AppCompatActivity {

    private EditText content;
    private Spinner subject;
    private Button Bdate;
    private String showDate;
    private String firebaseDate;
    private String uid;
    private String contentString;
    private String subjectString;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference uidRef;
    private DatabaseReference noteRef;
    private DatabaseReference dateRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();

        getWindow().setEnterTransition(null);

        Intent intent = getIntent();
        showDate = intent.getStringExtra("date");
        firebaseDate = intent.getStringExtra("dbDate");
        Bdate = findViewById(R.id.btDate);
        Bdate.setText(showDate);

        Bdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "서프라이즈!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.studia.blue/notice/intro-blog/"));
                startActivity(intent);
            }
        });

        ImageButton btBack = (ImageButton)findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScheduleActivity.this, CalendarActivity.class);
                intent.putExtra("date", showDate);
                intent.putExtra("dbDate", firebaseDate);
                getWindow().setExitTransition(null);
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(ScheduleActivity.this, Bdate, "date");
                startActivity(intent, options.toBundle());
                finishAfterTransition();
            }
        });


        Button btScheduleAdd = (Button)findViewById(R.id.btComplete);
        btScheduleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content = findViewById(R.id.etAssign);
                contentString = content.getText().toString();
                subject = findViewById(R.id.snSubject);

                subjectString = subject.getSelectedItem().toString();

                database = FirebaseDatabase.getInstance();
                databaseReference = database.getReference();
                uidRef = databaseReference.child("calendar").child(uid);

                uidRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            if (snapshot.hasChild(firebaseDate)) {
                                dateRef = uidRef.child(firebaseDate);

                                dateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.hasChild("note")) {
                                            noteRef = dateRef.child("note");

                                            noteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.hasChild("0")) {
                                                        int Number = (int) snapshot.getChildrenCount();

                                                        DatabaseReference addRef = noteRef.child(Integer.toString(Number));

                                                        addRef.child("content").setValue(contentString);
                                                        addRef.child("subject").setValue(subjectString);

                                                        Intent intent = new Intent(ScheduleActivity.this, CalendarActivity.class);
                                                        intent.putExtra("date", showDate);
                                                        intent.putExtra("dbDate", firebaseDate);
                                                        startActivity(intent);
                                                        finish();

                                                        overridePendingTransition(0, 0);
                                                    } else {
                                                        noteRef.child("0");
                                                        DatabaseReference zeroRef = noteRef.child("0");

                                                        zeroRef.child("content").setValue(contentString);
                                                        zeroRef.child("subject").setValue(subjectString);

                                                        Intent intent = new Intent(ScheduleActivity.this, CalendarActivity.class);
                                                        intent.putExtra("date", showDate);
                                                        intent.putExtra("dbDate", firebaseDate);
                                                        startActivity(intent);
                                                        finish();

                                                        overridePendingTransition(0, 0);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Log.e("ScheduleActivity", String.valueOf(error.toException()));
                                                }
                                            });
                                        } else {
                                            dateRef.child("note");
                                            noteRef = dateRef.child("note");
                                            noteRef.child("0");
                                            DatabaseReference zeroRef = noteRef.child("0");

                                            zeroRef.child("content").setValue(contentString);
                                            zeroRef.child("subject").setValue(subjectString);

                                            Intent intent = new Intent(ScheduleActivity.this, CalendarActivity.class);
                                            intent.putExtra("date", showDate);
                                            intent.putExtra("dbDate", firebaseDate);
                                            startActivity(intent);
                                            finish();

                                            overridePendingTransition(0, 0);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e("ScheduleActivity", String.valueOf(error.toException()));
                                    }
                                });
                            } else {
                                uidRef.child(firebaseDate);
                                dateRef = uidRef.child(firebaseDate);
                                dateRef.child("note");
                                noteRef = dateRef.child("note");
                                noteRef.child("0");
                                DatabaseReference zeroRef = noteRef.child("0");

                                zeroRef.child("content").setValue(contentString);
                                zeroRef.child("subject").setValue(subjectString);

                                Intent intent = new Intent(ScheduleActivity.this, CalendarActivity.class);
                                intent.putExtra("date", showDate);
                                intent.putExtra("dbDate", firebaseDate);
                                startActivity(intent);
                                finish();

                                overridePendingTransition(0, 0);
                            }
                        }
                        else {
                            databaseReference.child("calendar").child(uid);
                            uidRef = databaseReference.child("calendar").child(uid);
                            uidRef.child(firebaseDate);
                            dateRef = uidRef.child(firebaseDate);
                            dateRef.child("note");
                            noteRef = dateRef.child("note");
                            noteRef.child("0");
                            DatabaseReference zeroRef = noteRef.child("0");

                            zeroRef.child("content").setValue(contentString);
                            zeroRef.child("subject").setValue(subjectString);

                            Intent intent = new Intent(ScheduleActivity.this, CalendarActivity.class);
                            intent.putExtra("date", showDate);
                            intent.putExtra("dbDate", firebaseDate);
                            startActivity(intent);
                            finish();

                            overridePendingTransition(0, 0);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("ScheduleActivity", String.valueOf(error.toException()));
                    }
                });
            }
        });
    }
}