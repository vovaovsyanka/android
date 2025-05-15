package ru.mirea.ovsyannikov.firebaseauth;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.mirea.ovsyannikov.firebaseauth.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "FirebaseAuth";
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.createAccountButton.setOnClickListener(v -> createAccount(
                binding.emailField.getText().toString(),
                binding.passwordField.getText().toString()
        ));

        binding.signInButton.setOnClickListener(v -> signIn(
                binding.emailField.getText().toString(),
                binding.passwordField.getText().toString()
        ));

        binding.signOutButton.setOnClickListener(v -> signOut());
        binding.verifyEmailButton.setOnClickListener(v -> sendEmailVerification());
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void createAccount(String email, String password) {
        if (!validateForm()) return;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Регистрация успешна");
                        updateUI(mAuth.getCurrentUser());
                    } else {
                        Toast.makeText(this, "Ошибка: " + task.getException(),
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void signIn(String email, String password) {
        if (!validateForm()) return;

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Вход выполнен");
                        updateUI(mAuth.getCurrentUser());
                    } else {
                        Toast.makeText(this, "Ошибка входа",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
        Toast.makeText(this, "Вы вышли", Toast.LENGTH_SHORT).show();
    }

    private void sendEmailVerification() {
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this,
                                "Письмо отправлено на " + user.getEmail(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validateForm() {
        boolean isValid = true;

        if (TextUtils.isEmpty(binding.emailField.getText())) {
            binding.emailLayout.setError("Обязательное поле");
            isValid = false;
        } else {
            binding.emailLayout.setError(null);
        }

        if (TextUtils.isEmpty(binding.passwordField.getText())) {
            binding.passwordLayout.setError("Обязательное поле");
            isValid = false;
        } else {
            binding.passwordLayout.setError(null);
        }

        return isValid;
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            binding.statusTextView.setText(
                    String.format("Email: %s\nВерифицирован: %s",
                            user.getEmail(),
                            user.isEmailVerified() ? "Да" : "Нет")
            );

            binding.detailTextView.setText(
                    String.format("UID: %s", user.getUid())
            );

            binding.emailPasswordButtons.setVisibility(View.GONE);
            binding.signedInButtons.setVisibility(View.VISIBLE);
            binding.verifyEmailButton.setEnabled(!user.isEmailVerified());

        } else {
            binding.statusTextView.setText("Вы не авторизованы");
            binding.detailTextView.setText("");
            binding.emailPasswordButtons.setVisibility(View.VISIBLE);
            binding.signedInButtons.setVisibility(View.GONE);

            binding.emailField.setText("");
            binding.passwordField.setText("");
        }
    }
}