package app.web.studia_kr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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