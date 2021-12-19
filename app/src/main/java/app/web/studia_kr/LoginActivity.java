package app.web.studia_kr;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

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
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity{

    private EditText etEmail;
    private EditText etPassword;
    private String Email;
    private String Password;
    private GoogleSignInClient googleSignInClient;
    private final static int RC_SIGN_IN = 123;
    private FirebaseAuth mFirebaseAuth;
    public ActivityResultLauncher<Intent> signInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("654717768488-dmq2q3n9g27kgk1uv3ndo0im4f0bokbh.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        mFirebaseAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        signInLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RC_SIGN_IN) {
                    Intent data = result.getData();

                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        Log.d("LoginActivity", "firebaseAuthWithGoogle: " + account.getId());
                        resultLogin(account.getIdToken());
                    } catch (ApiException e) {
                        // Google Sign In failed, update UI appropriately
                        Log.w("LoginActivity", "Google sign in failed", e);
                    }
                }
            }
        });

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

                                FirebaseUser user = mFirebaseAuth.getCurrentUser();

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
                Intent signInIntent = googleSignInClient.getSignInIntent();
                signInLauncher.launch(signInIntent);

                //원래 있던 코드(혹시 몰라 남겼습니다)
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    //원래 있던 코드(혹시 몰라 남겼습니다)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("LoginActivity", "firebaseAuthWithGoogle: " + account.getId());
                resultLogin(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("LoginActivity", "Google sign in failed", e);
            }
        }
    }

    private void resultLogin(String idToken) {
        mFirebaseAuth = FirebaseAuth.getInstance();

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Login Success
                            Log.i("LoginActivity(Auth)", "LoginActivity - Google Login Started.");

                            FirebaseUser user = mFirebaseAuth.getCurrentUser();

                            Intent intent = new Intent(LoginActivity.this, CalendarActivity.class);
                            startActivity(intent);
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

    public void Register(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://studia.blue/signup/"));
        startActivity(intent);
    }

    public void Lost(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://studia.blue/iforgot/"));
        startActivity(intent);
    }
}