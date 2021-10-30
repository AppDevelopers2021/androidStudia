package app.web.studia_kr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class CalendarActivity extends AppCompatActivity {

    //Declaration
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Todo> arrayList;
    private Button btDate;
    private String uid;
    private String Date;
    private String bDate;
    public FirebaseDatabase database;
    public DatabaseReference databaseReference;
    public DatabaseReference calendarRef;
    public DatabaseReference dateRef;
    public DatabaseReference noteRef;
    public DatabaseReference memoRef;
    public DatabaseReference assignRef;
    public DatabaseReference reminderRef;
    public DatabaseReference uidRef;
    public Calendar calendar;
    public DateFormat dateFormat;
    public DateFormat firebaseFormat;
    public String Memo;
    public String Assign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //Calendar Instance TimeSet
        calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        firebaseFormat = new SimpleDateFormat("yyyyMMdd");
        Date = firebaseFormat.format(calendar.getTime());
        bDate = dateFormat.format(calendar.getTime());
        btDate = findViewById(R.id.btDateManager);
        btDate.setText(dateFormat.format(calendar.getTime()));

        //Firebase RecyclerView Declare
        recyclerView = findViewById(R.id.rvTodoLIst);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        //Intent Firebase UID Data Get
        Intent intent = getIntent();
        uid = intent.getStringExtra("firebaseUID");

        //Firebase Database RecyclerView
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        calendarRef = databaseReference.child("calendar");
        uidRef = calendarRef.child(uid);

        if (uidRef.child(firebaseFormat.format(calendar.getTime())) != null) {
            dateRef = uidRef.child(firebaseFormat.format(calendar.getTime())).child("note");
            if (dateRef.child("memo") != null) {
                noteRef = dateRef.child("memo");

                noteRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        arrayList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Todo todo = snapshot.getValue(Todo.class);
                            arrayList.add(todo);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("CalendarActivity", String.valueOf(error.toException()));
                    }
                });

                adapter = new CustomAdapter(arrayList, this);
                recyclerView.setAdapter(adapter);
            }

            if (dateRef.child("memo") != null) {
                //Firebase Database Memo
                memoRef = dateRef.child("memo");

                memoRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Memo = snapshot.getValue(String.class);
                        TextView tvMemo = findViewById(R.id.tvMemo);
                        tvMemo.setText(Memo);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("CalendarActivity", String.valueOf(error.toException()));
                    }
                });
            }

            if (dateRef.child("reminder") != null) {
                //Firebase Database Assignment(Reminder)
                assignRef = dateRef.child("reminder");
                int Count = 0;
                String Reference = Integer.toString(Count);

                while (assignRef.child(Reference) != null) {
                    reminderRef = assignRef.child(Reference);

                    reminderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Assign = snapshot.getValue(String.class);
                            TextView tvAssign = findViewById(R.id.tvAssign);
                            tvAssign.append("\n" + "•" + Assign);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("CalendarActivity", String.valueOf(error.toException()));
                        }
                    });
                }
            }
        }
    }

    public void scheduleSetup(View view) {
        //btTodoAdd OnClick Event
        //Calendar → Schedule Activity Change
        Intent intent = new Intent(CalendarActivity.this, ScheduleActivity.class);
        intent.putExtra("firebaseUID", uid);
        intent.putExtra("date", Date);
        intent.putExtra("bdate", bDate);
        startActivity(intent);
    }

    public void yesterdayClick(View view) {
        //btCalendarLeft OnClick Event
        //Subtract one day from calendar
        calendar.add(Calendar.DATE, -1);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        firebaseFormat = new SimpleDateFormat("yyyyMMdd");
        Date = dateFormat.format(calendar.getTime());
        btDate.setText(dateFormat.format(calendar.getTime()));

        //Firebase Database Refresh
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        calendarRef = databaseReference.child("calendar");
        uidRef = calendarRef.child(uid);

        if (uidRef.child(firebaseFormat.format(calendar.getTime())) != null) {
            dateRef = uidRef.child(firebaseFormat.format(calendar.getTime()));

            if (dateRef.child("note") != null) {
                noteRef = dateRef.child("note");

                noteRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    arrayList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Todo todo = snapshot.getValue(Todo.class);
                        arrayList.add(todo);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("CalendarActivity", String.valueOf(error.toException()));
                }
            });

                adapter = new CustomAdapter(arrayList, this);
                recyclerView.setAdapter(adapter);
        }

            if (dateRef.child("memo") != null) {
                //Firebase Database Memo
                memoRef = dateRef.child("memo");

                memoRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Memo = snapshot.getValue(String.class);
                        TextView tvMemo = findViewById(R.id.tvMemo);
                        tvMemo.setText(Memo);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("CalendarActivity", String.valueOf(error.toException()));
                    }
                });
            }

            if (dateRef.child("reminder") != null) {
                //Firebase Database Assignment(Reminder)
                assignRef = dateRef.child("reminder");
                int Count = 0;
                String Reference = Integer.toString(Count);

                while (assignRef.child(Reference) != null) {
                    reminderRef = assignRef.child(Reference);

                    reminderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Assign = snapshot.getValue(String.class);
                            TextView tvAssign = findViewById(R.id.tvAssign);
                            tvAssign.append("\n" + "•" + Assign);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("CalendarActivity", String.valueOf(error.toException()));
                        }
                    });
                }
            }
        }
    }

    public void tomorrowClick(View view) {
        //btCalendarRight OnClick Event
        //Add one day to calendar
        calendar.add(Calendar.DATE, +1);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        firebaseFormat = new SimpleDateFormat("yyyyMMdd");
        Date = dateFormat.format(calendar.getTime());
        btDate.setText(dateFormat.format(calendar.getTime()));

        //Firebase Database Refresh
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        calendarRef = databaseReference.child("calendar");
        uidRef = calendarRef.child(uid);

        if (uidRef.child(firebaseFormat.format(calendar.getTime())) != null) {

            if (uidRef.child(firebaseFormat.format(calendar.getTime())) != null) {
                dateRef = uidRef.child(firebaseFormat.format(calendar.getTime()));
                noteRef = uidRef.child(firebaseFormat.format(calendar.getTime())).child("note");

                noteRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        arrayList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Todo todo = snapshot.getValue(Todo.class);
                            arrayList.add(todo);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("CalendarActivity", String.valueOf(error.toException()));
                    }
                });

                adapter = new CustomAdapter(arrayList, this);
                recyclerView.setAdapter(adapter);
            }

            if (dateRef.child("memo") != null) {
                //Firebase Database Memo
                memoRef = dateRef.child("memo");

                memoRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Memo = snapshot.getValue(String.class);
                        TextView tvMemo = findViewById(R.id.tvMemo);
                        tvMemo.setText(Memo);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("CalendarActivity", String.valueOf(error.toException()));
                    }
                });
            }

            if (dateRef.child("reminder") != null) {
                //Firebase Database Assignment(Reminder)
                assignRef = dateRef.child("reminder");
                int Count = 0;
                String Reference = Integer.toString(Count);

                while (assignRef.child(Reference) != null) {
                    reminderRef = assignRef.child(Reference);

                    reminderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Assign = snapshot.getValue(String.class);
                            TextView tvAssign = findViewById(R.id.tvAssign);
                            tvAssign.append("\n" + "•" + Assign);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("CalendarActivity", String.valueOf(error.toException()));
                        }
                    });
                }
            }
        }
    }
}