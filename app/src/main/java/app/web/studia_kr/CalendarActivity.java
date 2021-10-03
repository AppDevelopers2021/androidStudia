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

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Todo> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Button btDate;
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
        btDate = findViewById(R.id.btDateManager);
        btDate.setText(dateFormat.format(calendar.getTime()));

        //Firebase RecyclerView Declare
        recyclerView = findViewById(R.id.rvTodoLIst);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        //Firebase Database RecyclerView
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        DatabaseReference calendarRef = databaseReference.child("calendar");
        DatabaseReference uidRef = calendarRef.child("firebaseUID");
        DatabaseReference dateRef = uidRef.child(firebaseFormat.format(calendar.getTime()));
        DatabaseReference noteRef = dateRef.child("note");

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

        //Firebase Database Memo
        DatabaseReference memoRef = dateRef.child("memo");

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

        //Firebase Database Assignment(Reminder)
        DatabaseReference assignRef = dateRef.child("reminder");
        DatabaseReference reminderRef = assignRef.child("0");

        reminderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Assign = snapshot.getValue(String.class);
                TextView tvAssign = findViewById(R.id.tvAssign);
                tvAssign.setText(Assign);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CalendarActivity", String.valueOf(error.toException()));
            }
        });
    }

    public void scheduleSetup(View view) {
        //btTodoAdd OnClick Event
        //Calendar → Schedule Activity Change
        Intent intent = new Intent(CalendarActivity.this, ScheduleActivity.class);
        startActivity(intent);
    }

    public void yesterdayClick(View view) {
        //btCalendarLeft OnClick Event
        //Subtract one day from calendar
        calendar.add(Calendar.DATE, -1);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        firebaseFormat = new SimpleDateFormat("yyyyMMdd");
        btDate.setText(dateFormat.format(calendar.getTime()));
    }

    public void tomorrowClick(View view) {
        //btCalendarRight OnClick Event
        //Add one day to calendar
        calendar.add(Calendar.DATE, +1);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        firebaseFormat = new SimpleDateFormat("yyyyMMdd");
        btDate.setText(dateFormat.format(calendar.getTime()));
    }
}