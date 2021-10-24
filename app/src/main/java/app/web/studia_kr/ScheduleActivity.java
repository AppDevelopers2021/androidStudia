package app.web.studia_kr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

public class ScheduleActivity extends AppCompatActivity {

    private EditText content;
    private EditText subject;
    private TextView Bdate;
    private String date;
    private String bdate;
    private String uid;
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
        Bdate = findViewById(R.id.tvScheduleDate);
        Bdate.setText(bdate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat firebaseFormat = new SimpleDateFormat("yyyyMMdd");
    }

    public void scheduleAdd(View view) {
        content = findViewById(R.id.editTextMemo);
        subject = findViewById(R.id.editTextTopic);

        if (content.getText() != null && subject.getText() != null){
            database = FirebaseDatabase.getInstance();
            databaseReference = database.getReference();
            calendarRef = databaseReference.child("calendar") ;

            if (calendarRef.child(uid) == null){
                calendarRef.child(uid);
                uidRef = calendarRef.child(uid);
                uidRef.child(date);
                dateRef = uidRef.child(date);
                dateRef.child("note");
                dateRef.child("memo");
                dateRef.child("reminder");
                noteRef = dateRef.child("note");
                numberRef = noteRef.child("0");
                numberRef.child("content").setValue(content);
                numberRef.child("subject").setValue(subject);

                Intent intent = new Intent(ScheduleActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
            else {
                uidRef = calendarRef.child(uid);

                if (uidRef.child(date) == null) {
                    uidRef.child(date);
                    dateRef = uidRef.child(date);
                    dateRef.child("note");
                    noteRef = dateRef.child("note");
                    dateRef.child("memo");
                    dateRef.child("reminder");
                    noteRef.child("0");
                    numberRef = noteRef.child("0");
                    numberRef.child("content").setValue(content);
                    numberRef.child("subject").setValue(subject);

                    Intent intent = new Intent(ScheduleActivity.this, CalendarActivity.class);
                    startActivity(intent);
                }
                else {
                    uidRef.child(date);
                    dateRef = uidRef.child(date);
                    noteRef = dateRef.child("note");

                    if (noteRef.child("0") == null) {
                        noteRef.child("0");
                        numberRef = noteRef.child("0");
                        numberRef.child("content").setValue(content);
                        numberRef.child("subject").setValue(subject);

                        Intent intent = new Intent(ScheduleActivity.this, CalendarActivity.class);
                        startActivity(intent);
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
                        numberRef.child("content").setValue(content);
                        numberRef.child("subject").setValue(subject);

                        Intent intent = new Intent(ScheduleActivity.this, CalendarActivity.class);
                        startActivity(intent);
                    }
                }
            }
        }
    }
}