package app.web.studia_kr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private String Email;
    private String Password;
    private FirebaseAuth mFirebaseAuth;
    public String uid;
    public SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("file", 0);
        String autoEmail = sharedPreferences.getString("email", "");
        String autoPassword = sharedPreferences.getString("password", "");

        if (autoEmail != null && autoPassword != null) {
            mFirebaseAuth.signInWithEmailAndPassword(autoEmail, autoPassword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        uid = mFirebaseAuth.getUid();
                        Intent intent = new Intent(LoginActivity.this, CalendarActivity.class);
                        intent.putExtra("firebaseUID", uid);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        //Login Failure
                    }
                }
            });
        }
    }

    public void loginRequest(View view) {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        Email = etEmail.getText().toString();
        Password = etPassword.getText().toString();

        mFirebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    uid = mFirebaseAuth.getUid();
                    Intent intent = new Intent(LoginActivity.this, CalendarActivity.class);
                    intent.putExtra("firebaseUID", uid);
                    sharedPreferences = getSharedPreferences("file", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                    editor.putString("email", Email);
                    editor.putString("password", Password);
                    editor.commit();
                    startActivity(intent);
                    finish();
                }
                else {
                    //Login Failure
                    Toast.makeText(getApplicationContext(), "이메일 또는 비밀번호가 옳지 않습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}