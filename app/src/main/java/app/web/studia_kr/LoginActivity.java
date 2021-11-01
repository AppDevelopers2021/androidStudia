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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private EditText etEmail;
    private EditText etPassword;
    private String Email;
    private String Password;
    private GoogleApiClient googleApiClient;
    private static final int REQ_SIGN_GOOGLE = 100;
    private FirebaseAuth mFirebaseAuth;
    public String uid;
    public SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        mFirebaseAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("file", 0);
        String autoEmail = sharedPreferences.getString("email", "");
        String autoPassword = sharedPreferences.getString("password", "");
        String google = sharedPreferences.getString("google", "");

        if (autoEmail != null && autoPassword != null && google == "no") {
            mFirebaseAuth.signInWithEmailAndPassword(autoEmail, autoPassword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.i("LoginActivity(Auth)", "LoginActivity - Auto Login Started.");

                        uid = mFirebaseAuth.getUid();
                        Intent intent = new Intent(LoginActivity.this, CalendarActivity.class);
                        intent.putExtra("firebaseUID", uid);
                        startActivity(intent);

                        overridePendingTransition(0, 0);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (google == "yes") {
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(intent, REQ_SIGN_GOOGLE);
        }

        ImageButton btLogin =  (ImageButton)findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etEmail = findViewById(R.id.etEmail);
                etPassword = findViewById(R.id.etPassword);

                Email = etEmail.getText().toString();
                Password = etPassword.getText().toString();

                mFirebaseAuth = FirebaseAuth.getInstance();

                mFirebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("LoginActivity(Auth)", "LoginActivity - Default Login Started.");

                            uid = mFirebaseAuth.getCurrentUser().getUid();
                            Intent intent = new Intent(LoginActivity.this, CalendarActivity.class);
                            intent.putExtra("firebaseUID", uid);
                            sharedPreferences = getSharedPreferences("file", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.commit();
                            editor.putString("email", Email);
                            editor.putString("password", Password);
                            editor.putString("google", "no");
                            editor.commit();
                            startActivity(intent);

                            overridePendingTransition(0, 0);
                            finish();
                        }
                        else {
                            //Login Failure
                            Toast.makeText(getApplicationContext(), "이메일 또는 비밀번호가 옳지 않습니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        ImageButton btGoogle = (ImageButton)findViewById(R.id.btGoogle);
        btGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
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
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                resultLogin(account);
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

                            uid = mFirebaseAuth.getCurrentUser().getUid();
                            Intent intent = new Intent(LoginActivity.this, CalendarActivity.class);
                            intent.putExtra("firebaseUID", uid);
                            sharedPreferences = getSharedPreferences("file", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.commit();
                            editor.putString("email", account.getEmail());
                            editor.putString("password", "");
                            editor.putString("google", "yes");
                            editor.commit();
                            startActivity(intent);

                            overridePendingTransition(0, 0);
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