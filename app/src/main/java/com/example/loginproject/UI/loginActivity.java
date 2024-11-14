package com.example.loginproject.UI;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginproject.R;
import com.example.loginproject.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class loginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Configurar Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Configurar click listeners
        binding.loginButton.setOnClickListener(v -> signInWithEmail());
        binding.googleSignInButton.setOnClickListener(v -> signInWithGoogle());
    }

    private void signInWithEmail() {
        String email = binding.emailEditText.getText().toString();
        String password = binding.passwordEditText.getText().toString();

        if (TextUtils.isEmpty(email)) {
            binding.emailEditText.setError("Email requerido");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            binding.passwordEditText.setError("Contraseña requerida");
            return;
        }

        // Mostrar progreso
        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    hideProgressDialog();
                    if (task.isSuccessful()) {
                        // Login exitoso
                        goToMainActivity();
                    } else {
                        // Si falla, mostrar mensaje
                        Toast.makeText(loginActivity.this,
                                "Autenticación fallida: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In fue exitoso, autenticar con Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In falló
                Toast.makeText(this, "Google sign in failed: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        showProgressDialog();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    hideProgressDialog();
                    if (task.isSuccessful()) {
                        // Sign in success
                        goToMainActivity();
                    } else {
                        // If sign in fails
                        Toast.makeText(loginActivity.this, "Authentication Failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(loginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void showProgressDialog() {
        // Mostrar un ProgressDialog o ProgressBar
    }

    private void hideProgressDialog() {
        // Ocultar el ProgressDialog o ProgressBar
    }
}