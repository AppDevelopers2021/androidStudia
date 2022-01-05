package app.web.studia_kr;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity{

    private EditText etEmail;
    private EditText etPassword;
    private CheckBox chPolicy;
    private String Email;
    private String Password;
    private Boolean firstRun;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth mFirebaseAuth;

    // Deprecated 처리된 startActivityForResult를 대체
    public ActivityResultLauncher<Intent> signInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getIntent().hasExtra("showAgreeBtn")) {
            chPolicy = findViewById(R.id.cbPolicy);
            chPolicy.setVisibility(View.VISIBLE);
            firstRun = true;
        }
        else {
            firstRun = false;
        }

        // GoogleSignInOptions에서 requestIdToken은 google-services.json의 client>oauth_client>client_id를 하드코딩함
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        mFirebaseAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        Button btLogin =  (Button)findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email = etEmail.getText().toString();
                Password = etPassword.getText().toString();

                if (Email.length() >= 6 && Password.length() >= 6) {
                    if (!firstRun) {
                        mFirebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("LoginActivity", "Logged in successfully via email");

                                    FirebaseUser user = mFirebaseAuth.getCurrentUser();

                                    Intent intent = new Intent(LoginActivity.this, CalendarActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                                    if (errorCode == "ERROR_USER_NOT_FOUND") {
                                        Log.w("LoginActivity", task.getException());
                                        Toast.makeText(getApplicationContext(), "이메일 또는 비밀번호가 옳지 않습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Log.w("LoginActivity", task.getException());
                                        Toast.makeText(getApplicationContext(), "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }
                    else {
                        if (chPolicy.isChecked()) {
                            SharedPreferences settings = getSharedPreferences("PrefsFile", 0);
                            settings.edit().putBoolean("agreed", true).commit();

                            mFirebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("LoginActivity", "Logged in successfully via email");

                                        FirebaseUser user = mFirebaseAuth.getCurrentUser();

                                        Intent intent = new Intent(LoginActivity.this, CalendarActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                                        if (errorCode == "ERROR_USER_NOT_FOUND") {
                                            Log.w("LoginActivity", task.getException());
                                            Toast.makeText(getApplicationContext(), "이메일 또는 비밀번호가 옳지 않습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Log.w("LoginActivity", task.getException());
                                            Toast.makeText(getApplicationContext(), "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "스튜디아 이용약관 및 개인정보처리방침에 동의해야 합니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "이메일 또는 비밀번호가 옳지 않습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

        signInLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                int resultCode = result.getResultCode();
                if (resultCode == Activity.RESULT_OK) {
                    Intent data = result.getData();

                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        // 구글 로그인 성공
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        Log.d("LoginActivity", "firebaseAuthWithGoogle: " + account.getId());
                        resultLogin(account.getIdToken());
                    } catch (ApiException e) {
                        // 구글 로그인 실패
                        Toast.makeText(getApplicationContext(), "Google 로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        Log.w("LoginActivity", "Google sign in failed", e);
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Log.d("LoginActivity", "Google sign in Activity failed with: RESULT_CANCELED");
                } else {
                    Log.d("LoginActivity", "Google sign in Activity failed with: UNKNOWN");
                }

            }
        });

        Button btGoogle = (Button)findViewById(R.id.btGoogle);
        btGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!firstRun) {
                    Intent signInIntent = googleSignInClient.getSignInIntent();
                    signInLauncher.launch(signInIntent);
                }
                else {
                    if (chPolicy.isChecked()) {
                        SharedPreferences settings = getSharedPreferences("PrefsFile", 0);
                        settings.edit().putBoolean("agreed", true).commit();

                        Intent signInIntent = googleSignInClient.getSignInIntent();
                        signInLauncher.launch(signInIntent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "스튜디아 이용약관 및 개인정보처리방침에 동의해야 합니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void resultLogin(String idToken) {
        mFirebaseAuth = FirebaseAuth.getInstance();

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Log.d("LoginActivity", "Successful Google AuthCredential");

                            FirebaseUser user = mFirebaseAuth.getCurrentUser();

                            Intent intent = new Intent(LoginActivity.this, CalendarActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();
                        }
                        else {
                            // 로그인 실패
                            task.getException().printStackTrace();
                            Log.w("LoginActivity", task.getException());
                            Toast.makeText(getApplicationContext(), "Google 로그인에 실패했습니다.", Toast.LENGTH_LONG).show();
                        }
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
}