package app.web.studia_kr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

public class MemoEditActivity extends AppCompatActivity {

    private EditText etMemo;
    private EditText etAssign;
    private Button btComplete;
    private TextView Bdate;
    private String date;
    private String bdate;
    private String uid;
    private String memo1;
    private String assign1;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference calendarRef;
    private DatabaseReference uidRef;
    private DatabaseReference memoRef;
    private DatabaseReference reminderRef;
    private DatabaseReference dateRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_edit);

        Intent intent = getIntent();
        uid = intent.getStringExtra("firebaseUID");
        date = intent.getStringExtra("date");
        bdate = intent.getStringExtra("bdate");
        Bdate = findViewById(R.id.btDate);
        Bdate.setText(bdate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat firebaseFormat = new SimpleDateFormat("yyyyMMdd");
    }

    public void update(View view) {
        etMemo = findViewById(R.id.etMemo);
        memo1 = etMemo.getText().toString();
        etAssign = findViewById(R.id.etAssign);
        assign1 = etAssign.getText().toString();

        if (memo1 != "" || assign1 != "") {
            database = FirebaseDatabase.getInstance();
            databaseReference = database.getReference();
            calendarRef = databaseReference.child("calendar") ;

            if (memo1 != null) {
                if (calendarRef.child(uid) == null){
                    calendarRef.child(uid);
                    uidRef = calendarRef.child(uid);
                    uidRef.child(date);
                    dateRef = uidRef.child(date);
                    dateRef.child("memo");
                    memoRef = dateRef.child("memo");
                    memoRef.setValue(memo1);

                    Intent intent = new Intent(MemoEditActivity.this, CalendarActivity.class);
                    startActivity(intent);

                    overridePendingTransition(0, 0);
                }
                else {
                    uidRef = calendarRef.child(uid);
                    if (uidRef.child(date) == null) {
                        uidRef.child(date);
                        dateRef = uidRef.child(date);
                        dateRef.child("memo");
                        memoRef = dateRef.child("memo");
                        memoRef.setValue(memo1);

                        Intent intent = new Intent(MemoEditActivity.this, CalendarActivity.class);
                        startActivity(intent);

                        overridePendingTransition(0, 0);
                    }
                    else {
                        uidRef.child(date);
                        dateRef = uidRef.child(date);

                        if (dateRef.child("memo") == null) {
                            dateRef.child("memo");
                            memoRef = dateRef.child("memo");
                            memoRef.setValue(memo1);

                            Intent intent = new Intent(MemoEditActivity.this, CalendarActivity.class);
                            startActivity(intent);

                            overridePendingTransition(0, 0);
                        }
                        else {
                            memoRef = dateRef.child("memo");
                            memoRef.setValue(memo1);

                            Intent intent = new Intent(MemoEditActivity.this, CalendarActivity.class);
                            startActivity(intent);

                            overridePendingTransition(0, 0);
                        }
                    }
                }
            }

            if (assign1 != null) {
                if (calendarRef.child(uid) == null){
                    calendarRef.child(uid);
                    uidRef = calendarRef.child(uid);
                    uidRef.child(date);
                    dateRef = uidRef.child(date);
                    dateRef.child("reminder");
                    reminderRef = dateRef.child("reminder");

                    int intcount = 0;
                    String stringcount = Integer.toString(intcount);

                    while (reminderRef.child(stringcount) != null) {
                        stringcount = Integer.toString(intcount);
                        reminderRef.child(stringcount).removeValue();
                        ++intcount;
                    }

                    intcount = etAssign.getLineCount();
                    int intadd = 0;
                    stringcount = Integer.toString(intadd);

                    while (intadd != intcount) {
                        stringcount = Integer.toString(intadd);
                        reminderRef.child(stringcount);
                        DatabaseReference assignment = reminderRef.child(stringcount);

                        int beginIndex = etAssign.getLayout().getLineStart(intadd);
                        int endIndex = etAssign.getLayout().getLineEnd(intadd);

                        assignment.setValue(etAssign.getText().toString().substring(beginIndex, endIndex));
                    }

                    Intent intent = new Intent(MemoEditActivity.this, CalendarActivity.class);
                    startActivity(intent);

                    overridePendingTransition(0, 0);
                }
                else {
                    uidRef = calendarRef.child(uid);
                    if (uidRef.child(date) == null) {
                        uidRef.child(date);
                        dateRef = uidRef.child(date);
                        dateRef.child("reminder");
                        reminderRef = dateRef.child("reminder");

                        int intcount = 0;
                        String stringcount = Integer.toString(intcount);

                        while (reminderRef.child(stringcount) != null) {
                            stringcount = Integer.toString(intcount);
                            reminderRef.child(stringcount).removeValue();
                            ++intcount;
                        }

                        intcount = etAssign.getLineCount();
                        int intadd = 0;
                        stringcount = Integer.toString(intadd);

                        while (intadd != intcount) {
                            stringcount = Integer.toString(intadd);
                            reminderRef.child(stringcount);
                            DatabaseReference assignment = reminderRef.child(stringcount);

                            int beginIndex = etAssign.getLayout().getLineStart(intadd);
                            int endIndex = etAssign.getLayout().getLineEnd(intadd);

                            assignment.setValue(etAssign.getText().toString().substring(beginIndex, endIndex));
                        }

                        Intent intent = new Intent(MemoEditActivity.this, CalendarActivity.class);
                        startActivity(intent);

                        overridePendingTransition(0, 0);
                    }
                    else {
                        uidRef.child(date);
                        dateRef = uidRef.child(date);

                        if (dateRef.child("reminder") == null) {
                            dateRef.child("reminder");
                            reminderRef = dateRef.child("reminder");

                            int intcount = 0;
                            String stringcount = Integer.toString(intcount);

                            while (reminderRef.child(stringcount) != null) {
                                stringcount = Integer.toString(intcount);
                                reminderRef.child(stringcount).removeValue();
                                ++intcount;
                            }

                            intcount = etAssign.getLineCount();
                            int intadd = 0;
                            stringcount = Integer.toString(intadd);

                            while (intadd != intcount) {
                                stringcount = Integer.toString(intadd);
                                reminderRef.child(stringcount);
                                DatabaseReference assignment = reminderRef.child(stringcount);

                                int beginIndex = etAssign.getLayout().getLineStart(intadd);
                                int endIndex = etAssign.getLayout().getLineEnd(intadd);

                                assignment.setValue(etAssign.getText().toString().substring(beginIndex, endIndex));
                            }

                            Intent intent = new Intent(MemoEditActivity.this, CalendarActivity.class);
                            startActivity(intent);

                            overridePendingTransition(0, 0);
                        }
                        else {
                            reminderRef = dateRef.child("reminder");

                            int intcount = 0;
                            String stringcount = Integer.toString(intcount);

                            while (reminderRef.child(stringcount) != null) {
                                stringcount = Integer.toString(intcount);
                                reminderRef.child(stringcount).removeValue();
                                ++intcount;
                            }

                            intcount = etAssign.getLineCount();
                            int intadd = 0;
                            stringcount = Integer.toString(intadd);

                            while (intadd != intcount) {
                                stringcount = Integer.toString(intadd);
                                reminderRef.child(stringcount);
                                DatabaseReference assignment = reminderRef.child(stringcount);

                                int beginIndex = etAssign.getLayout().getLineStart(intadd);
                                int endIndex = etAssign.getLayout().getLineEnd(intadd);

                                assignment.setValue(etAssign.getText().toString().substring(beginIndex, endIndex));
                            }

                            Intent intent = new Intent(MemoEditActivity.this, CalendarActivity.class);
                            startActivity(intent);

                            overridePendingTransition(0, 0);
                        }
                    }
                }
            }
        }
        else {
            if (calendarRef.child(uid) == null){
                calendarRef.child(uid);
                uidRef = calendarRef.child(uid);
                uidRef.child(date);
                dateRef = uidRef.child(date);
                dateRef.child("reminder");
                reminderRef = dateRef.child("reminder");

                dateRef.child("memo");
                memoRef = dateRef.child("memo");
                memoRef.setValue(memo1);

                int intcount = 0;
                String stringcount = Integer.toString(intcount);

                while (reminderRef.child(stringcount) != null) {
                    stringcount = Integer.toString(intcount);
                    reminderRef.child(stringcount).removeValue();
                    ++intcount;
                }

                intcount = etAssign.getLineCount();
                int intadd = 0;
                stringcount = Integer.toString(intadd);

                while (intadd != intcount) {
                    stringcount = Integer.toString(intadd);
                    reminderRef.child(stringcount);
                    DatabaseReference assignment = reminderRef.child(stringcount);

                    int beginIndex = etAssign.getLayout().getLineStart(intadd);
                    int endIndex = etAssign.getLayout().getLineEnd(intadd);

                    assignment.setValue(etAssign.getText().toString().substring(beginIndex, endIndex));
                }

                Intent intent = new Intent(MemoEditActivity.this, CalendarActivity.class);
                startActivity(intent);

                overridePendingTransition(0, 0);
            }
            else {
                uidRef = calendarRef.child(uid);
                if (uidRef.child(date) == null) {
                    uidRef.child(date);
                    dateRef = uidRef.child(date);
                    dateRef.child("reminder");
                    reminderRef = dateRef.child("reminder");

                    dateRef.child("memo");
                    memoRef = dateRef.child("memo");
                    memoRef.setValue(memo1);

                    int intcount = 0;
                    String stringcount = Integer.toString(intcount);

                    while (reminderRef.child(stringcount) != null) {
                        stringcount = Integer.toString(intcount);
                        reminderRef.child(stringcount).removeValue();
                        ++intcount;
                    }

                    intcount = etAssign.getLineCount();
                    int intadd = 0;
                    stringcount = Integer.toString(intadd);

                    while (intadd != intcount) {
                        stringcount = Integer.toString(intadd);
                        reminderRef.child(stringcount);
                        DatabaseReference assignment = reminderRef.child(stringcount);

                        int beginIndex = etAssign.getLayout().getLineStart(intadd);
                        int endIndex = etAssign.getLayout().getLineEnd(intadd);

                        assignment.setValue(etAssign.getText().toString().substring(beginIndex, endIndex));
                    }

                    Intent intent = new Intent(MemoEditActivity.this, CalendarActivity.class);
                    startActivity(intent);

                    overridePendingTransition(0, 0);
                } else {
                    uidRef.child(date);
                    dateRef = uidRef.child(date);

                    if (dateRef.child("reminder") == null) {
                        dateRef.child("reminder");
                        reminderRef = dateRef.child("reminder");

                        int intcount = 0;
                        String stringcount = Integer.toString(intcount);

                        while (reminderRef.child(stringcount) != null) {
                            stringcount = Integer.toString(intcount);
                            reminderRef.child(stringcount).removeValue();
                            ++intcount;
                        }

                        intcount = etAssign.getLineCount();
                        int intadd = 0;
                        stringcount = Integer.toString(intadd);

                        while (intadd != intcount) {
                            stringcount = Integer.toString(intadd);
                            reminderRef.child(stringcount);
                            DatabaseReference assignment = reminderRef.child(stringcount);

                            int beginIndex = etAssign.getLayout().getLineStart(intadd);
                            int endIndex = etAssign.getLayout().getLineEnd(intadd);

                            assignment.setValue(etAssign.getText().toString().substring(beginIndex, endIndex));
                        }

                        if (dateRef.child("memo") == null) {
                            dateRef.child("memo");
                            memoRef = dateRef.child("memo");
                            memoRef.setValue(memo1);
                        }
                        else {
                            memoRef = dateRef.child("memo");
                            memoRef.setValue(memo1);
                        }

                        Intent intent = new Intent(MemoEditActivity.this, CalendarActivity.class);
                        startActivity(intent);

                        overridePendingTransition(0, 0);
                    } else {
                        reminderRef = dateRef.child("reminder");

                        int intcount = 0;
                        String stringcount = Integer.toString(intcount);

                        while (reminderRef.child(stringcount) != null) {
                            stringcount = Integer.toString(intcount);
                            reminderRef.child(stringcount).removeValue();
                            ++intcount;
                        }

                        intcount = etAssign.getLineCount();
                        int intadd = 0;
                        stringcount = Integer.toString(intadd);

                        while (intadd != intcount) {
                            stringcount = Integer.toString(intadd);
                            reminderRef.child(stringcount);
                            DatabaseReference assignment = reminderRef.child(stringcount);

                            int beginIndex = etAssign.getLayout().getLineStart(intadd);
                            int endIndex = etAssign.getLayout().getLineEnd(intadd);

                            assignment.setValue(etAssign.getText().toString().substring(beginIndex, endIndex));
                        }

                        if (dateRef.child("memo") == null) {
                            dateRef.child("memo");
                            memoRef = dateRef.child("memo");
                            memoRef.setValue(memo1);
                        }
                        else {
                            memoRef = dateRef.child("memo");
                            memoRef.setValue(memo1);
                        }

                        Intent intent = new Intent(MemoEditActivity.this, CalendarActivity.class);
                        startActivity(intent);

                        overridePendingTransition(0, 0);
                    }
                }
            }
        }
    }
}