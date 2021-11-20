package app.web.studia_kr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

public class ScheduleActivity extends AppCompatActivity {

    private EditText content;
    private Spinner subject;
    private TextView Bdate;
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

        Intent intent = getIntent();
        showDate = intent.getStringExtra("date");
        firebaseDate = intent.getStringExtra("dbDate");
        Bdate = findViewById(R.id.btDate);
        Bdate.setText(showDate);

        ImageButton btScheduleAdd = (ImageButton)findViewById(R.id.btAddSchedule);
        btScheduleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content = findViewById(R.id.etAssign);
                contentString = content.getText().toString();
                subject = findViewById(R.id.snSubject);

                subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        subjectString = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Toast.makeText(getApplicationContext(), "과목이 정해지지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

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

                                            noteRef.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.hasChild("1")) {
                                                        int Number = (int) snapshot.getChildrenCount() - 1;

                                                        DatabaseReference addRef = noteRef.child(Integer.toString(Number));

                                                        addRef.child("content").setValue(contentString);
                                                        addRef.child("subject").setValue(subjectString);

                                                        Intent intent = new Intent(ScheduleActivity.this, CalendarActivity.class);
                                                        startActivity(intent);

                                                        overridePendingTransition(0, 0);
                                                    } else {
                                                        noteRef.child("0");
                                                        DatabaseReference zeroRef = noteRef.child("0");

                                                        zeroRef.child("content").setValue(contentString);
                                                        zeroRef.child("subject").setValue(subjectString);

                                                        Intent intent = new Intent(ScheduleActivity.this, CalendarActivity.class);
                                                        startActivity(intent);

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
                                            startActivity(intent);

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
                                startActivity(intent);

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
                            startActivity(intent);

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