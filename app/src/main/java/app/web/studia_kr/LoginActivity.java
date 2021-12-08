package app.web.studia_kr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private String Email;
    private String Password;
    private GoogleSignInClient googleSignInClient;
    private static final int REQ_SIGN_GOOGLE = 100;
    private FirebaseAuth mFirebaseAuth;
    public SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        mFirebaseAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("preference", 0);
        if (sharedPreferences.getString("auto", "0").equals("1")) {
            if (sharedPreferences.getString("google", "0").equals("0")) {

                String Email = sharedPreferences.getString("email", "none");
                String Password = sharedPreferences.getString("password", "none");

                mFirebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("Login Success", "Successful Login.");

                            Intent intent = new Intent(LoginActivity.this, CalendarActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Log.w("Login Failure", task.getException());

                            Toast.makeText(getApplicationContext(), "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, REQ_SIGN_GOOGLE);
            }
        }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        Button btLogin =  (Button)findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email = etEmail.getText().toString();
                Password = etPassword.getText().toString();

                if (Email.length() >= 6 && Password.length() >= 6) {
                    mFirebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.i("Login Success", "Successful Login.");

                                sharedPreferences = getSharedPreferences("preference", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("auto", "1");
                                editor.putString("google", "0");
                                editor.putString("email", Email);
                                editor.putString("password", Password);
                                editor.apply();

                                Intent intent = new Intent(LoginActivity.this, CalendarActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Log.w("Login Failure", task.getException());

                                Toast.makeText(getApplicationContext(), "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(), "이메일 또는 비밀번호가 옳지 않습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button btGoogle = (Button)findViewById(R.id.btGoogle);
        btGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, REQ_SIGN_GOOGLE);
            }
        });
    }


    public void Register(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://studia.blue/signup/"));
        startActivity(intent);
    }

    public void Lost(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://studia.blue/iforgot/"));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == REQ_SIGN_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                GoogleSignInAccount account = task.getResult();
                resultLogin(account);
            }
        }
    }

    private void resultLogin(GoogleSignInAccount account) {
        mFirebaseAuth = FirebaseAuth.getInstance();

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Login Success
                            Log.i("LoginActivity(Auth)", "LoginActivity - Google Login Started.");

                            sharedPreferences = getSharedPreferences("preference", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("auto", "1");
                            editor.putString("google", "1");
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this, CalendarActivity.class);

                            overridePendingTransition(0, 0);
                            finish();
                        }
                        else {
                            //Login Failure
                            Log.w("Login Failure - Google", task.getException());
                            Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}