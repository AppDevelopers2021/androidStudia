package app.web.studia_kr;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NoteManagerPopup extends AppCompatActivity {

    private Spinner subject;
    private EditText content;
    private View copy;
    private View share;
    private View delete;
    private String subjectString;
    private String contentString;
    private String dateString;
    private ArrayList subjectList;
    private ArrayList contentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView(R.layout.activity_note_manager_popup);

        subject = findViewById(R.id.snSubject);
        content = findViewById(R.id.etAssign);
        copy = findViewById(R.id.copyLayout);
        share = findViewById(R.id.shareLayout);
        delete = findViewById(R.id.deleteLayout);
        subjectString = getIntent().getStringExtra("subject");
        contentString = getIntent().getStringExtra("content");
        dateString = getIntent().getStringExtra("date");

        if (getIntent().getStringExtra("subject").equals("가정")) {
            subject.setSelection(0);
        } else if (getIntent().getStringExtra("subject").equals("과학")) {
            subject.setSelection(1);
        } else if (getIntent().getStringExtra("subject").equals("국어")) {
            subject.setSelection(2);
        } else if (getIntent().getStringExtra("subject").equals("기술")) {
            subject.setSelection(3);
        } else if (getIntent().getStringExtra("subject").equals("도덕")) {
            subject.setSelection(4);
        } else if (getIntent().getStringExtra("subject").equals("독서")) {
            subject.setSelection(5);
        } else if (getIntent().getStringExtra("subject").equals("미술")) {
            subject.setSelection(6);
        } else if (getIntent().getStringExtra("subject").equals("보건")) {
            subject.setSelection(7);
        } else if (getIntent().getStringExtra("subject").equals("사회")) {
            subject.setSelection(8);
        } else if (getIntent().getStringExtra("subject").equals("수학")) {
            subject.setSelection(9);
        } else if (getIntent().getStringExtra("subject").equals("영어")) {
            subject.setSelection(10);
        } else if (getIntent().getStringExtra("subject").equals("음악")) {
            subject.setSelection(11);
        } else if (getIntent().getStringExtra("subject").equals("정보")) {
            subject.setSelection(12);
        } else if (getIntent().getStringExtra("subject").equals("진로")) {
            subject.setSelection(13);
        } else if (getIntent().getStringExtra("subject").equals("창체")) {
            subject.setSelection(14);
        } else if (getIntent().getStringExtra("subject").equals("체육")) {
            subject.setSelection(15);
        } else if (getIntent().getStringExtra("subject").equals("환경")) {
            subject.setSelection(16);
        } else if (getIntent().getStringExtra("subject").equals("자율")) {
            subject.setSelection(17);
        } else if (getIntent().getStringExtra("subject").equals("기타")) {
            subject.setSelection(18);
        }

        content.setText(getIntent().getStringExtra("content"));

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied from Studia Android application", subjectString + ": " + contentString);
                clipboard.setPrimaryClip(clip);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, subjectString + ": " + contentString);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference noteRef = FirebaseDatabase.getInstance().getReference().child("calendar").child(FirebaseAuth.getInstance().getUid()).child(dateString).child("note");

                noteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (int i = 0; i<snapshot.getChildrenCount(); i++) {
                            noteRef.child(Integer.toString(i)).child("subject").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    subjectList.add(snapshot.getValue().toString());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.d("NoteManagerPopup", "Database error occurred.");
                                }
                            });

                            noteRef.child(Integer.toString(i)).child("content").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    contentList.add(snapshot.getValue().toString());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.d("NoteManagerPopup", "Database error occurred.");
                                }
                            });
                        }

                        subjectList.remove(subjectString);
                        contentList.remove(contentString);

                        for (int j = 0; j<subjectList.size(); j++) {
                            noteRef.child(Integer.toString(j)).child("subject").setValue(subjectList.get(j));
                            noteRef.child(Integer.toString(j)).child("content").setValue(contentList.get(j));
                        }

                        noteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                noteRef.child(Long.toString(snapshot.getChildrenCount() - 1)).removeValue();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d("NoteManagerPopup", "Database error occurred.");
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("NoteManagerPopup", "Database error occurred.");
                    }
                });
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }
}