package app.web.studia_kr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MemoEditActivity extends AppCompatActivity {

    private EditText etMemo;
    private EditText etAssign;
    private TextView Bdate;
    private String showDate;
    private String firebaseDate;
    private String uid;
    private String memoString;
    private String assignString;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference uidRef;
    private DatabaseReference memoRef;
    private DatabaseReference reminderRef;
    private DatabaseReference dateRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_edit);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();

        Intent intent = getIntent();
        showDate = intent.getStringExtra("date");
        firebaseDate = intent.getStringExtra("dbDate");
        Bdate = findViewById(R.id.btDate);
        Bdate.setText(showDate);

        ImageButton btComplete = (ImageButton) findViewById(R.id.btComplete);
        btComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etMemo = findViewById(R.id.etMemo);
                memoString = etMemo.getText().toString();
                etAssign = findViewById(R.id.etAssign);
                assignString = etAssign.getText().toString();

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
                                        if (snapshot.hasChild("memo")) {
                                            memoRef = dateRef.child("memo");

                                            memoRef.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    memoRef.setValue(memoString);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Log.e("MemoEditActivity", String.valueOf(error.toException()));
                                                }
                                            });
                                        }
                                        else {
                                            dateRef.child("memo");
                                            memoRef = dateRef.child("memo");

                                            memoRef.setValue(memoString);

                                            Intent intent = new Intent(MemoEditActivity.this, CalendarActivity.class);
                                            startActivity(intent);
                                            finish();

                                            overridePendingTransition(0, 0);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e("MemoEditActivity", String.valueOf(error.toException()));
                                    }
                                });

                                dateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.hasChild("reminder")) {
                                            reminderRef = dateRef.child("reminder");

                                            reminderRef.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    int lineCount = etAssign.getLineCount();
                                                    int count = 0;

                                                    while (count != lineCount - 1) {
                                                        int lineStart = etAssign.getLayout().getLineStart(count);
                                                        int lineEnd = etAssign.getLayout().getLineEnd(count);

                                                        reminderRef.child(Integer.toString(count)).setValue(etAssign.getText().toString().substring(lineStart, lineEnd));

                                                        ++count;
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Log.e("MemoEditActivity", String.valueOf(error.toException()));
                                                }
                                            });
                                        }
                                        else {
                                            dateRef.child("reminder");
                                            reminderRef = dateRef.child("reminder");

                                            reminderRef.child("0");
                                            DatabaseReference zeroRef = reminderRef.child("0");

                                            zeroRef.setValue(assignString);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                Intent intent = new Intent(MemoEditActivity.this, CalendarActivity.class);
                                startActivity(intent);
                                finish();

                                overridePendingTransition(0, 0);

                            }
                            else {
                                uidRef.child(firebaseDate);
                                dateRef = uidRef.child(firebaseDate);
                                dateRef.child("memo").setValue(memoString);
                                dateRef.child("reminder");
                                reminderRef = dateRef.child("reminder");

                                reminderRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int lineCount = etAssign.getLineCount();
                                        int count = 0;

                                        while (count != lineCount - 1) {
                                            int lineStart = etAssign.getLayout().getLineStart(count);
                                            int lineEnd = etAssign.getLayout().getLineEnd(count);

                                            reminderRef.child(Integer.toString(count)).setValue(etAssign.getText().toString().substring(lineStart, lineEnd));

                                            ++count;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e("MemoEditActivity", String.valueOf(error.toException()));
                                    }
                                });
                            }
                        }
                        else {
                            databaseReference.child("calendar").child(uid);
                            uidRef = databaseReference.child("calendar").child(uid);
                            uidRef.child(firebaseDate);
                            dateRef = uidRef.child(firebaseDate);
                            dateRef.child("memo").setValue(memoString);
                            dateRef.child("reminder");
                            reminderRef = dateRef.child("reminder");

                            reminderRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int lineCount = etAssign.getLineCount();
                                    int count = 0;

                                    while (count != lineCount - 1) {
                                        int lineStart = etAssign.getLayout().getLineStart(count);
                                        int lineEnd = etAssign.getLayout().getLineEnd(count);

                                        reminderRef.child(Integer.toString(count)).setValue(etAssign.getText().toString().substring(lineStart, lineEnd));

                                        ++count;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e("MemoEditActivity", String.valueOf(error.toException()));
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("MemoEditActivity", String.valueOf(error.toException()));
                    }
                });
            }
        });
    }

    private void CalendarLoad(String uid, String showDate, String firebaseDate) {
        Log.w("MemoEditActivity", "void CalendarLoad started.");

        Button btDate = findViewById(R.id.btDate);
        btDate.setText(showDate);

        //Firebase Database Refresh
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
                                if (snapshot.hasChild("memo")) {
                                    memoRef = dateRef.child("memo");

                                    memoRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String Memo = snapshot.getValue(String.class);
                                            EditText tvMemo = findViewById(R.id.etMemo);
                                            tvMemo.setText(Memo);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Log.e("MemoEditActivity", String.valueOf(error.toException()));
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("MemoEditActivity", String.valueOf(error.toException()));
                            }
                        });


                        dateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild("reminder")) {
                                    reminderRef = dateRef.child("reminder");

                                    reminderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                                                String reminder = datasnapshot.getValue().toString();

                                                EditText tvAssign = findViewById(R.id.etAssign);
                                                tvAssign.append("\n" + reminder);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Log.e("MemoEditActivity", String.valueOf(error.toException()));
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("MemoEditActivity", String.valueOf(error.toException()));
                            }
                        });
                    }
                    else {
                        EditText memo = findViewById(R.id.etMemo);
                        memo.setText("");

                        EditText assign = findViewById(R.id.etAssign);
                        assign.setText("");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MemoEditActivity", String.valueOf(error.toException()));
            }
        });

        Log.w("MemoEditActivity", "void CalendarLoad finished.");
    }
}