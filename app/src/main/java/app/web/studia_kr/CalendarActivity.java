package app.web.studia_kr;

import android.app.ActivityOptions;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
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

public class CalendarActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<Todo> arrayList;
    private Button btDate;
    private String uid;
    private String firebaseDate;
    private String showDate;
    private String Memo;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference dateRef;
    private DatabaseReference noteRef;
    private DatabaseReference memoRef;
    private DatabaseReference reminderRef;
    private DatabaseReference uidRef;
    private Calendar calendar;
    private DateFormat dateFormat;
    private DateFormat firebaseFormat;
    private DatePickerDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        getWindow().setEnterTransition(null);
        getWindow().getSharedElementEnterTransition().setDuration(200);

        //ScheduleActivity 등에서 다시 CalendarActivity로 복귀했을 때 원래 날짜 복원
        if (getIntent().hasExtra("dbDate")) {
            firebaseDate = getIntent().getStringExtra("dbDate");
            String[] arrayDate = firebaseDate.split("");

            int year = Integer.parseInt(arrayDate[0] + arrayDate[1] + arrayDate[2] + arrayDate[3]);
            int month = Integer.parseInt(arrayDate[4] + arrayDate[5]);
            int date = Integer.parseInt(arrayDate[6] + arrayDate[7]);

            calendar = Calendar.getInstance();

            //그냥 int month 자체를 사용했을 때, 제대로 로드되지 않는 오류가 있음
            //따라서, month 대신 month - 1 사용
            calendar.set(year, month - 1, date);

            dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            showDate = dateFormat.format(calendar.getTime());
            firebaseFormat = new SimpleDateFormat("yyyyMMdd");
            firebaseDate = firebaseFormat.format(calendar.getTime());
        }
        else {
            //Calendar 현 시간 설정
            calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            showDate = dateFormat.format(calendar.getTime());
            firebaseFormat = new SimpleDateFormat("yyyyMMdd");
            firebaseDate = firebaseFormat.format(calendar.getTime());
        }
        btDate = findViewById(R.id.btDate);
        btDate.setText(showDate);

        //Firebase RecyclerView Declare
        recyclerView = findViewById(R.id.rvTodo);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        //Firebase UID Data Get
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();

        CalendarLoad(uid, firebaseDate, showDate);

        Button btDate = findViewById(R.id.btDate);
        btDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogOpen(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            }
        });

        ImageButton btPrevious = findViewById(R.id.btPrevious);
        btPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DATE, -1);
                firebaseDate = firebaseFormat.format(calendar.getTime());
                showDate = dateFormat.format(calendar.getTime());

                arrayClear();

                //Memo, Assign Init
                TextView assign = findViewById(R.id.tvShowAssign);
                TextView memo = findViewById(R.id.tvShowMemo);
                memo.setText("");
                assign.setText("");

                View linearLayout = findViewById(R.id.SwipeLayout);

                TranslateAnimation anim1 = new TranslateAnimation(0, linearLayout.getWidth() /2,0, 0);
                anim1.setDuration(0200);
                anim1.setFillAfter(true);
                linearLayout.startAnimation(anim1);

                CalendarLoad(uid, firebaseDate, showDate);

                TranslateAnimation anim2 = new TranslateAnimation(0 - linearLayout.getWidth() /2, 0, 0, 0);
                anim2.setDuration(0200);
                anim2.setFillAfter(true);
                linearLayout.startAnimation(anim2);
            }
        });

        ImageButton btNext = findViewById(R.id.btNext);
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DATE, +1);
                firebaseDate = firebaseFormat.format(calendar.getTime());
                showDate = dateFormat.format(calendar.getTime());

                arrayClear();

                //Memo, Assign Init
                TextView assign = findViewById(R.id.tvShowAssign);
                TextView memo = findViewById(R.id.tvShowMemo);
                memo.setText("");
                assign.setText("");

                View linearLayout = findViewById(R.id.SwipeLayout);

                TranslateAnimation anim1 = new TranslateAnimation(0, 0 - linearLayout.getWidth() /2,0, 0);
                anim1.setDuration(0200);
                anim1.setFillAfter(true);
                linearLayout.startAnimation(anim1);

                CalendarLoad(uid, firebaseDate, showDate);

                TranslateAnimation anim2 = new TranslateAnimation(linearLayout.getWidth() /2, 0, 0, 0);
                anim2.setDuration(0200);
                anim2.setFillAfter(true);
                linearLayout.startAnimation(anim2);
            }
        });

        ImageButton btSchedule = findViewById(R.id.btAddSchedule);
        btSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, ScheduleActivity.class);
                intent.putExtra("date", showDate);
                intent.putExtra("dbDate", firebaseDate);
                getWindow().setExitTransition(null);

                // Custom transition effect
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(CalendarActivity.this, btDate, "date");
                startActivity(intent, options.toBundle());
            }
        });

        ImageButton btEditMemo = findViewById(R.id.btEditMemo);
        btEditMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, MemoEditActivity.class);
                intent.putExtra("date", showDate);
                intent.putExtra("dbDate", firebaseDate);
                getWindow().setExitTransition(null);

                // Custom transition effect
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(CalendarActivity.this, btDate, "date");
                startActivity(intent, options.toBundle());
            }
        });

        ImageButton btEditAssign = findViewById(R.id.btEditAssign);
        btEditAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, MemoEditActivity.class);
                intent.putExtra("date", showDate);
                intent.putExtra("dbDate", firebaseDate);
                getWindow().setExitTransition(null);

                // Custom transition effect
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(CalendarActivity.this, btDate, "date");
                startActivity(intent, options.toBundle());
            }
        });

        ImageButton btProfile = findViewById(R.id.btProfile);
        btProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, PopupActivity.class);
                intent.putExtra("date", showDate);
                intent.putExtra("dbDate", firebaseDate);
                startActivityForResult(intent, 1);
            }
        });
    }

    public void dialogOpen(int year, int month, int date) {
        dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i, i1, i2);

                firebaseDate = firebaseFormat.format(calendar.getTime());
                showDate = dateFormat.format(calendar.getTime());

                arrayClear();

                //Memo, Assign Init
                TextView assign = findViewById(R.id.tvShowAssign);
                TextView memo = findViewById(R.id.tvShowMemo);
                memo.setText("");
                assign.setText("");

                CalendarLoad(uid, firebaseDate, showDate);
            }
        }, year, month, date);

        dialog.show();
    }

    public void CalendarLoad(String uid, String firebaseDate, String showDate) {
        Log.d("CalendarActivity", "void CalendarLoad started");

        TextView assign = findViewById(R.id.tvShowAssign);
        TextView memo = findViewById(R.id.tvShowMemo);

        memo.setText("");
        assign.setText("");

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

                                    reminderRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int i = 1;
                                            TextView tvAssign = findViewById(R.id.tvShowAssign);
                                            tvAssign.setText("");

                                            for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                                                String reminder = datasnapshot.getValue().toString();

                                                if (i != snapshot.getChildrenCount()) {
                                                     tvAssign.append("·" + reminder + "\n");
                                                     i++;
                                                }
                                                else {
                                                    tvAssign.append("·" + reminder);
                                                }
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

                        TextView assign = findViewById(R.id.tvShowAssign);
                        TextView memo = findViewById(R.id.tvShowMemo);

                        if (!arrayList.isEmpty()) {
                            arrayList.clear();
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);

                            memo.setText("");
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

        Log.d("CalendarActivity", "void CalendarLoad finished");
    }

    public void arrayClear() {
        int arraySize = arrayList.size();
        arrayList.clear();

        for (int i = 0; i<arraySize; i++) {
            adapter = new CustomAdapter(arrayList, CalendarActivity.this);
            adapter.notifyItemRemoved(i);
            recyclerView.setAdapter(adapter);
        }
    }
}