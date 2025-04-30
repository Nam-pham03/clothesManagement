package com.example.project_prm.Activity.User.Profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_prm.Activity.User.Shop.ProductListActivity;
import com.example.project_prm.MainActivity;
import com.example.project_prm.R;
import com.example.project_prm.Repository.UserRepository;
import com.example.project_prm.ViewModel.User.UserViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private Button btnSignIn, btnSignUp, btnLogin, btnLoginWithGoogle;
    private EditText etUsername, etPassword, etSignUpUsername, etEmail, etSignUpPassword;
    private CheckBox cbRememberMe;
    private TextView tvForgotPassword;
    private LinearLayout layoutSignIn, layoutSignUp;
    private ViewStub viewStubSignUp;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_USER_ID = "user_id";

    private ActivityResultLauncher<Intent> googleSignInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("365666487550-o6aioaki9br6t1lr24aeuro406iq67en.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        UserRepository repo = new UserRepository(this);
        repo.logAllUsers();

        if (checkAlreadyLoggedIn()) {
            return;
        }

        initializeComponents();

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        loadSavedPreferences();
        setupTabSwitching();

        setupButtonListeners();
        observeAuthResults();

        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        handleSignInResult(task);
                    }
                });
    }

    private boolean checkAlreadyLoggedIn() {
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        boolean isRemembered = sharedPreferences.getBoolean(KEY_REMEMBER, false);
        int userId = sharedPreferences.getInt(KEY_USER_ID, -1);

        if (isRemembered && userId != -1) {
            navigateToMainScreen(userId);
            return true;
        }
        return false;
    }

    private void initializeComponents() {
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginWithGoogle = findViewById(R.id.btnLoginWithGoogle);

        layoutSignIn = findViewById(R.id.layoutSignIn);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        viewStubSignUp = findViewById(R.id.viewStubSignUp);

        layoutSignIn.setVisibility(View.VISIBLE);

        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void loadSavedPreferences() {
        if (sharedPreferences.getBoolean(KEY_REMEMBER, false)) {
            etUsername.setText(sharedPreferences.getString(KEY_USERNAME, ""));
            etPassword.setText(sharedPreferences.getString(KEY_PASSWORD, ""));
            cbRememberMe.setChecked(true);
        }
    }

    private void setupTabSwitching() {
        btnSignIn.setOnClickListener(v -> showSignInLayout());
        btnSignUp.setOnClickListener(v -> showSignUpLayout());
    }

    private void showSignInLayout() {
        layoutSignIn.setVisibility(View.VISIBLE);
        if (layoutSignUp != null) {
            layoutSignUp.setVisibility(View.GONE);
        }
        btnSignIn.setEnabled(false);
        btnSignUp.setEnabled(true);
    }

    private void showSignUpLayout() {
        if (layoutSignUp == null) {
            try {
                viewStubSignUp.setLayoutResource(R.layout.registration);
                layoutSignUp = (LinearLayout) viewStubSignUp.inflate();

                etSignUpUsername = layoutSignUp.findViewById(R.id.etSignUpUsername);
                etEmail = layoutSignUp.findViewById(R.id.etEmail);
                etSignUpPassword = layoutSignUp.findViewById(R.id.etSignUpPassword);
                Button btnCreateAccount = layoutSignUp.findViewById(R.id.btnCreateAccount);
                Button btnRegisterWithGoogle = layoutSignUp.findViewById(R.id.btnRegisterWithGoogle);

                if (etSignUpUsername == null || etEmail == null || etSignUpPassword == null || btnCreateAccount == null) {
                    Toast.makeText(this, "Error: Missing components in SignUp layout", Toast.LENGTH_LONG).show();
                    return;
                }

                btnCreateAccount.setOnClickListener(v -> onRegisterButtonClicked());
                if (btnRegisterWithGoogle != null) {
                    btnRegisterWithGoogle.setOnClickListener(v -> signInWithGoogle());
                }
            } catch (Exception e) {
                Toast.makeText(this, "Failed to load SignUp layout: " + e.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }
        }

        layoutSignIn.setVisibility(View.GONE);
        layoutSignUp.setVisibility(View.VISIBLE);
        btnSignIn.setEnabled(true);
        btnSignUp.setEnabled(false);
    }

    private void setupButtonListeners() {
        btnLogin.setOnClickListener(v -> onLoginButtonClicked());
        btnLoginWithGoogle.setOnClickListener(v -> signInWithGoogle());
        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                firebaseAuthWithGoogle(account.getIdToken());
            } else {
                Toast.makeText(this, "Account is null", Toast.LENGTH_SHORT).show();
            }
        } catch (ApiException e) {
            Log.e("Register", "Google Sign-In failed: " + e.getMessage(), e);
            Toast.makeText(this, "Google Sign-In failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        Log.d("GoogleSignIn", "Authenticating with Firebase using Google token");

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
                        if (account != null) {
                            String username = account.getDisplayName();
                            String email = account.getEmail();
                            Log.d("GoogleSignIn", "Firebase Authentication successful: " + email);
                            userViewModel.googleLogin(username, email);
                        }
                    } else {
                        Log.e("GoogleSignIn", "Firebase Authentication failed: " + Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }


    private void observeAuthResults() {
        userViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult != null) {
                if (loginResult.isSuccess()) {
                    int userId = loginResult.getUser().getId();
                    if (cbRememberMe.isChecked()) {
                        saveLoginInfo(etUsername.getText().toString(), etPassword.getText().toString(), userId);
                    } else {
                        clearSavedLoginInfo();
                        saveUserIdOnly(userId);
                    }
                    navigateToMainScreen(userId);
                } else {
                    Toast.makeText(this, loginResult.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        userViewModel.getRegisterResult().observe(this, registerResult -> {
            if (registerResult != null) {
                if (registerResult.isSuccess()) {
                    Toast.makeText(this, "Account created successfully. Please login.", Toast.LENGTH_SHORT).show();
                    navigateToLoginScreen();
                } else {
                    Toast.makeText(this, registerResult.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        userViewModel.getGoogleLoginResult().observe(this, googleLoginResult -> {
            if (googleLoginResult != null) {
                if (googleLoginResult.isSuccess()) {
                    int userId = googleLoginResult.getUser().getId();
                    saveUserIdOnly(userId);
                    navigateToMainScreen(userId);
                } else {
                    Toast.makeText(this, googleLoginResult.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onLoginButtonClicked() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Username is required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return;
        }


        userViewModel.login(username, password);
    }

    private void onRegisterButtonClicked() {
        String username = etSignUpUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etSignUpPassword.getText().toString().trim();


        if (TextUtils.isEmpty(username)) {
            etSignUpUsername.setError("Username is required");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email address");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etSignUpPassword.setError("Password is required");
            return;
        }
        if (password.length() < 6) {
            etSignUpPassword.setError("Password must be at least 6 characters");
            return;
        }

        userViewModel.register(username, password, email);
    }

    private void navigateToLoginScreen() {
        layoutSignIn.setVisibility(View.VISIBLE);
        if (layoutSignUp != null) {
            layoutSignUp.setVisibility(View.GONE);
        }
        btnSignIn.setEnabled(false);
        btnSignUp.setEnabled(true);

        if (etSignUpUsername != null) etSignUpUsername.setText("");
        if (etEmail != null) etEmail.setText("");
        if (etSignUpPassword != null) etSignUpPassword.setText("");
    }

    private void navigateToMainScreen(int userId) {
        Intent intent = new Intent(this, ProductListActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
        finish();
    }

    private void saveLoginInfo(String username, String password, int userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.putBoolean(KEY_REMEMBER, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    private void saveUserIdOnly(int userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    private void clearSavedLoginInfo() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_PASSWORD);
        editor.putBoolean(KEY_REMEMBER, false);
        editor.apply();
    }
}