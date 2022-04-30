package app.web.studia_kr;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class NoteManagerActivity extends AppCompatActivity {

    private String dateValue;
    private String subjectValue;
    private String contentValue;
    private Spinner subjectSpinner;
    private EditText contentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notemanager);

        dateValue = getIntent().getStringExtra("date");
        subjectValue = getIntent().getStringExtra("subject");
        contentValue = getIntent().getStringExtra("content");

        subjectSpinner = findViewById(R.id.snSubject);
        contentEditText = findViewById(R.id.etAssign);

        if (subjectValue.equals(R.string.Home)) subjectSpinner.setSelection(0);
        if (subjectValue.equals(R.string.Science)) subjectSpinner.setSelection(1);
        if (subjectValue.equals(R.string.Korean)) subjectSpinner.setSelection(2);
        if (subjectValue.equals(R.string.Artcraft)) subjectSpinner.setSelection(3);
        if (subjectValue.equals(R.string.Civics)) subjectSpinner.setSelection(4);
        if (subjectValue.equals(R.string.Reading)) subjectSpinner.setSelection(5);
        if (subjectValue.equals(R.string.Art)) subjectSpinner.setSelection(6);
        if (subjectValue.equals(R.string.HealthEdu)) subjectSpinner.setSelection(7);
        if (subjectValue.equals(R.string.SocialStudies)) subjectSpinner.setSelection(8);
        if (subjectValue.equals(R.string.Math)) subjectSpinner.setSelection(9);
        if (subjectValue.equals(R.string.English)) subjectSpinner.setSelection(10);
        if (subjectValue.equals(R.string.Music)) subjectSpinner.setSelection(11);
        if (subjectValue.equals(R.string.ComputerScience)) subjectSpinner.setSelection(12);
        if (subjectValue.equals(R.string.Career)) subjectSpinner.setSelection(13);
        if (subjectValue.equals(R.string.CreativeActivity)) subjectSpinner.setSelection(14);
        if (subjectValue.equals(R.string.PhysicalEducation)) subjectSpinner.setSelection(15);
        if (subjectValue.equals(R.string.Environments)) subjectSpinner.setSelection(16);
        if (subjectValue.equals(R.string.Autonomy)) subjectSpinner.setSelection(17);
        if (subjectValue.equals(R.string.Etc)) subjectSpinner.setSelection(18);

        contentEditText.setText(contentValue);
    }
}