package vn.nganha.musicapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

import vn.nganha.musicapp.R;
import vn.nganha.musicapp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sử dụng View Binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy email
                String email = binding.emailEdittext.getText().toString();
                // Lấy mật khẩu
                String password = binding.passwordEdittext.getText().toString();

                // Kiểm tra email hợp lệ
                if (!Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), email)) {
                    binding.emailEdittext.setError("Email không hợp lệ");
                    return;
                }
                // Đặt quy định cho pass
                if(password.length() < 6){
                    binding.passwordEdittext.setError("Độ dài mật khẩu trên 6 kí tự");
                    return;
                }

                loginWithFirebase(email, password);

            }
        });

        binding.gotoResetpassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        binding.gotoSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginWithFirebase(String email, String password) {
        // Hiển thị tiến trình
        setInProgress(true);

        // Gọi Firebase để tạo tài khoản
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        setInProgress(false);
                        // Thông báo khi tạo tài khoản thành công
                        Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        setInProgress(false);
                        // Thông báo khi tạo tài khoản thất bại
                        Toast.makeText(getApplicationContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            // Chuyển sang MainActivity nếu đã đăng nhập
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setInProgress(boolean inProgress) {
        if (inProgress) {
            binding.loginBtn.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.loginBtn.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        }
    }
}