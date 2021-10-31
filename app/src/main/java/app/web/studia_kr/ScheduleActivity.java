package app.web.studia_kr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

public class ScheduleActivity extends AppCompatActivity {

    private EditText content;
    private Spinner subject;
    private TextView Bdate;
    private String date;
    private String bdate;
    private String uid;
    private String content1;
    private String subject1;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference calendarRef;
    private DatabaseReference uidRef;
    private DatabaseReference noteRef;
    private DatabaseReference numberRef;
    private DatabaseReference dateRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Intent intent = getIntent();
        uid = intent.getStringExtra("firebaseUID");
        date = intent.getStringExtra("date");
        bdate = intent.getStringExtra("bdate");
        Bdate = findViewById(R.id.btDate);
        Bdate.setText(bdate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat firebaseFormat = new SimpleDateFormat("yyyyMMdd");
    }

    public void scheduleAdd(View view) {
        content = findViewById(R.id.etAssign);
        content1 = content.getText().toString();
        subject = findViewById(R.id.snSubject);

        subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subject1 = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), "과목이 정해지지 않았습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        if (content1 != null && subject1 != null){
            database = FirebaseDatabase.getInstance();
            databaseReference = database.getReference();
            calendarRef = databaseReference.child("calendar") ;

            if (calendarRef.child(uid) == null){
                calendarRef.child(uid);
                uidRef = calendarRef.child(uid);
                uidRef.child(date);
                dateRef = uidRef.child(date);
                dateRef.child("note");
                noteRef = dateRef.child("note");
                numberRef = noteRef.child("0");
                numberRef.child("content").setValue(content1);
                numberRef.child("subject").setValue(subject1);

                Intent intent = new Intent(ScheduleActivity.this, CalendarActivity.class);
                startActivity(intent);

                overridePendingTransition(0, 0);
            }
            else {
                uidRef = calendarRef.child(uid);

                if (uidRef.child(date) == null) {
                    uidRef.child(date);
                    dateRef = uidRef.child(date);
                    dateRef.child("note");
                    noteRef = dateRef.child("note");
                    noteRef.child("0");
                    numberRef = noteRef.child("0");
                    numberRef.child("content").setValue(content1);
                    numberRef.child("subject").setValue(subject1);

                    Intent intent = new Intent(ScheduleActivity.this, CalendarActivity.class);
                    startActivity(intent);

                    overridePendingTransition(0, 0);
                }
                else {
                    uidRef.child(date);
                    dateRef = uidRef.child(date);
                    noteRef = dateRef.child("note");

                    if (noteRef.child("0") == null) {
                        noteRef.child("0");
                        numberRef = noteRef.child("0");
                        numberRef.child("content").setValue(content1);
                        numberRef.child("subject").setValue(subject1);

                        Intent intent = new Intent(ScheduleActivity.this, CalendarActivity.class);
                        startActivity(intent);

                        overridePendingTransition(0, 0);
                    }
                    else {
                        int intcount = 0;
                        String stringcount = Integer.toString(intcount);

                        while (noteRef.child(stringcount) != null) {
                            stringcount = Integer.toString(intcount);
                            ++intcount;
                        }

                        ++intcount;
                        stringcount = Integer.toString(intcount);

                        noteRef.child(stringcount);
                        numberRef = noteRef.child(stringcount);
                        numberRef.child("content").setValue(content1);
                        numberRef.child("subject").setValue(subject1);

                        Intent intent = new Intent(ScheduleActivity.this, CalendarActivity.class);
                        startActivity(intent);

                        overridePendingTransition(0, 0);
                    }
                }
            }
        }
    }
}