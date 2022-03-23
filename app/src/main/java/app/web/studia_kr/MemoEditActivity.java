package app.web.studia_kr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
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

import java.util.ArrayList;

import app.web.studia_kr.backgroundservice.NotificationRestarter;

public class MemoEditActivity extends AppCompatActivity {

    private EditText etMemo;
    private EditText etAssign;
    private TextView btDate;
    private String showDate;
    private String firebaseDate;
    private String uid;
    private String memoString;
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

        // Get rid of the 'flashing' effect
        getWindow().setEnterTransition(null);
        getWindow().getSharedElementEnterTransition().setDuration(200);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();

        Intent intent = getIntent();
        showDate = intent.getStringExtra("date");
        firebaseDate = intent.getStringExtra("dbDate");
        btDate = findViewById(R.id.btDate);
        btDate.setText(showDate);

        CalendarLoad(uid, firebaseDate);

        ImageButton btBack = findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MemoEditActivity.this, CalendarActivity.class);
                intent.putExtra("date", showDate);
                intent.putExtra("dbDate", firebaseDate);
                getWindow().setExitTransition(null);
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(MemoEditActivity.this, btDate, "date");
                startActivity(intent, options.toBundle());
                finishAfterTransition();
            }
        });

        Button btComplete = findViewById(R.id.btComplete);
        btComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etMemo = findViewById(R.id.etMemo);
                memoString = etMemo.getText().toString();
                etAssign = findViewById(R.id.etAssign);

                database = FirebaseDatabase.getInstance();
                databaseReference = database.getReference();
                uidRef = databaseReference.child("calendar").child(uid);

                clearReminderRef();

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
                                            memoRef.setValue(memoString);

                                            Intent intent = new Intent(MemoEditActivity.this, CalendarActivity.class);
                                            intent.putExtra("date", showDate);
                                            intent.putExtra("dbDate", firebaseDate);
                                            getWindow().setExitTransition(null);
                                            ActivityOptions options = ActivityOptions
                                                    .makeSceneTransitionAnimation(MemoEditActivity.this, btDate, "date");
                                            startActivity(intent, options.toBundle());
                                            finishAfterTransition();
                                        }
                                        else {
                                            dateRef.child("memo");
                                            memoRef = dateRef.child("memo");
                                            memoRef.setValue(memoString);

                                            Intent intent = new Intent(MemoEditActivity.this, CalendarActivity.class);
                                            intent.putExtra("date", showDate);
                                            intent.putExtra("dbDate", firebaseDate);
                                            getWindow().setExitTransition(null);
                                            ActivityOptions options = ActivityOptions
                                                    .makeSceneTransitionAnimation(MemoEditActivity.this, btDate, "date");
                                            startActivity(intent, options.toBundle());
                                            finishAfterTransition();
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
                                                    String getReminder = etAssign.getText().toString();
                                                    if (getReminder != "") {
                                                        ArrayList lines = new ArrayList<>();
                                                        String[] extract;
                                                        extract = getReminder.split("\n");

                                                        for (int k = 0; k<extract.length; k++) {
                                                            lines.add(extract[k]);
                                                        }

                                                        for(int i=0; i<lines.size(); i++) {
                                                            String data = lines.get(i).toString();
                                                            reminderRef.child(Integer.toString(i)).setValue(data);
                                                        }
                                                    }
                                                    else {
                                                        reminderRef.removeValue();
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

                                            String getReminder = etAssign.getText().toString();
                                            if (getReminder != "") {
                                                ArrayList lines = new ArrayList<>();
                                                String[] extract;
                                                extract = getReminder.split("\n");

                                                for (int k = 0; k<extract.length; k++) {
                                                    lines.add(extract[k]);
                                                }

                                                for(int i=0; i<lines.size(); i++) {
                                                    String data = lines.get(i).toString();
                                                    reminderRef.child(Integer.toString(i)).setValue(data);
                                                }
                                            }
                                            else {
                                                reminderRef.removeValue();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e("MemoEditActivity", String.valueOf(error.toException()));
                                    }
                                });

                                Intent intent = new Intent(MemoEditActivity.this, CalendarActivity.class);
                                intent.putExtra("date", showDate);
                                intent.putExtra("dbDate", firebaseDate);
                                getWindow().setExitTransition(null);
                                ActivityOptions options = ActivityOptions
                                        .makeSceneTransitionAnimation(MemoEditActivity.this, btDate, "date");
                                startActivity(intent, options.toBundle());
                                finishAfterTransition();
                            }
                            else {
                                dateRef = uidRef.child(firebaseDate);
                                dateRef.child("memo").setValue(memoString);
                                dateRef.child("reminder");
                                reminderRef = dateRef.child("reminder");

                                reminderRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String getReminder = etAssign.getText().toString();
                                        if (getReminder != "") {
                                            ArrayList lines = new ArrayList<>();
                                            String[] extract;
                                            extract = getReminder.split("\n");

                                            for (int k = 0; k<extract.length; k++) {
                                                lines.add(extract[k]);
                                            }

                                            for(int i=0; i<lines.size(); i++) {
                                                String data = lines.get(i).toString();
                                                reminderRef.child(Integer.toString(i)).setValue(data);
                                            }
                                        }
                                        else {
                                            reminderRef.removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e("MemoEditActivity", String.valueOf(error.toException()));
                                    }
                                });

                                Intent intent = new Intent(MemoEditActivity.this, CalendarActivity.class);
                                intent.putExtra("date", showDate);
                                intent.putExtra("dbDate", firebaseDate);
                                getWindow().setExitTransition(null);
                                ActivityOptions options = ActivityOptions
                                        .makeSceneTransitionAnimation(MemoEditActivity.this, btDate, "date");
                                startActivity(intent, options.toBundle());
                                finishAfterTransition();
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
                                    String getReminder = etAssign.getText().toString();
                                    if (getReminder != "") {
                                        ArrayList lines = new ArrayList<>();
                                        String[] extract;
                                        extract = getReminder.split("\n");

                                        for (int k = 0; k<extract.length; k++) {
                                            lines.add(extract[k]);
                                        }

                                        for(int i=0; i<lines.size(); i++) {
                                            String data = lines.get(i).toString();
                                            reminderRef.child(Integer.toString(i)).setValue(data);
                                        }
                                    }
                                    else {
                                        reminderRef.removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e("MemoEditActivity", String.valueOf(error.toException()));
                                }
                            });

                            Intent intent = new Intent(MemoEditActivity.this, CalendarActivity.class);
                            intent.putExtra("date", showDate);
                            intent.putExtra("dbDate", firebaseDate);
                            getWindow().setExitTransition(null);
                            ActivityOptions options = ActivityOptions
                                    .makeSceneTransitionAnimation(MemoEditActivity.this, btDate, "date");
                            startActivity(intent, options.toBundle());
                            finishAfterTransition();
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

    private void CalendarLoad(String uid, String firebaseDate) {
        Log.w("MemoEditActivity", "void CalendarLoad started.");

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
                                            int i = 1;
                                            TextView etAssign = findViewById(R.id.etAssign);
                                            etAssign.setText("");

                                            for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                                                String reminder = datasnapshot.getValue().toString();

                                                if (i != snapshot.getChildrenCount()) {
                                                    etAssign.append(reminder + "\n");
                                                    i++;
                                                }
                                                else {
                                                    etAssign.append(reminder);
                                                }
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

    public void clearReminderRef() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("calendar").child(uid).child(firebaseDate).child("reminder").removeValue();
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