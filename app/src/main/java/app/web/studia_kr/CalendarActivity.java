package app.web.studia_kr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    //Declaration
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Todo> arrayList;
    private Button btDate;
    private String uid;
    private String firebaseDate;
    private String showDate;
    public FirebaseDatabase database;
    public DatabaseReference databaseReference;
    public DatabaseReference dateRef;
    public DatabaseReference noteRef;
    public DatabaseReference memoRef;
    public DatabaseReference reminderRef;
    public DatabaseReference uidRef;
    public Calendar calendar;
    public DateFormat dateFormat;
    public DateFormat firebaseFormat;
    public String Memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //Calendar Instance TimeSet
        calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        showDate = dateFormat.format(calendar.getTime());
        firebaseFormat = new SimpleDateFormat("yyyyMMdd");
        firebaseDate = firebaseFormat.format(calendar.getTime());
        btDate = findViewById(R.id.btDate);
        btDate.setText(showDate);

        //Firebase RecyclerView Declare
        recyclerView = findViewById(R.id.rvTodo);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        //Firebase UID Data Get
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();

        CalendarLoad(uid, firebaseDate, showDate);

        ImageButton btPrevious = (ImageButton)findViewById(R.id.btPrevious);
        btPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DATE, -1);
                firebaseDate = firebaseFormat.format(calendar.getTime());
                showDate = dateFormat.format(calendar.getTime());
                CalendarLoad(uid, firebaseDate, showDate);
            }
        });

        ImageButton btNext = (ImageButton)findViewById(R.id.btNext);
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DATE, +1);
                firebaseDate = firebaseFormat.format(calendar.getTime());
                showDate = dateFormat.format(calendar.getTime());
                CalendarLoad(uid, firebaseDate, showDate);
            }
        });

        ImageButton btSchedule = (ImageButton)findViewById(R.id.btAddSchedule);
        btSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, ScheduleActivity.class);
                intent.putExtra("date", showDate);
                intent.putExtra("dbDate", firebaseDate);
                startActivity(intent);

                overridePendingTransition(0, 0);
            }
        });

        ImageButton btEditMemo = (ImageButton)findViewById(R.id.btEditMemo);
        btEditMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, MemoEditActivity.class);
                intent.putExtra("date", showDate);
                intent.putExtra("dbDate", firebaseDate);
                startActivity(intent);

                overridePendingTransition(0, 0);
            }
        });

        ImageButton btEditAssign = (ImageButton)findViewById(R.id.btEditAssign);
        btEditAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, MemoEditActivity.class);
                intent.putExtra("date", showDate);
                intent.putExtra("dbDate", firebaseDate);
                startActivity(intent);

                overridePendingTransition(0, 0);
            }
        });

        ImageButton btProfile = (ImageButton)findViewById(R.id.btProfile);
        btProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, PopupActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    public void CalendarLoad(String uid, String firebaseDate, String showDate) {
        Log.w("CalendarActivity", "void CalendarLoad started.");
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
                                if (snapshot.hasChild("note")) {
                                    noteRef = dateRef.child("note");

                                    noteRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            arrayList.clear();
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                Todo todo = snapshot.getValue(Todo.class);
                                                arrayList.add(todo);
                                            }
                                            adapter = new CustomAdapter(arrayList, CalendarActivity.this);

                                            adapter.notifyDataSetChanged();
                                            recyclerView.setAdapter(adapter);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Log.e("CalendarActivity", String.valueOf(error.toException()));
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("CalendarActivity", String.valueOf(error.toException()));
                            }
                        });

                        dateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild("memo")) {
                                    memoRef = dateRef.child("memo");

                                    memoRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Memo = snapshot.getValue(String.class);
                                            TextView tvMemo = findViewById(R.id.tvShowMemo);
                                            tvMemo.setText(Memo);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Log.e("CalendarActivity", String.valueOf(error.toException()));
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("CalendarActivity", String.valueOf(error.toException()));
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

                                                TextView tvAssign = findViewById(R.id.tvShowAssign);
                                                tvAssign.append("\n" + "•" + reminder);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Log.e("CalendarActivity", String.valueOf(error.toException()));
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("CalendarActivity", String.valueOf(error.toException()));
                            }
                        });
                    }
                    else {
                        if (!arrayList.isEmpty()) {
                            arrayList.clear();
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);

                            TextView memo = findViewById(R.id.tvShowMemo);
                            memo.setText("");

                            TextView assign = findViewById(R.id.tvShowAssign);
                            assign.setText("");
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CalendarActivity", String.valueOf(error.toException()));
            }
        });

        Log.w("CalendarActivity", "void CalendarLoad finished.");
    }
}