package vn.nganha.musicapp.Activities;

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
import vn.nganha.musicapp.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sử dụng View Binding
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy email
                String email = binding.emailEdittext.getText().toString();
                // Lấy mật khẩu
                String password = binding.passwordEdittext.getText().toString();
                // Lấy xác nhận mật khẩu
                String confirmPassword = binding.confirmPasswordEdittext.getText().toString();

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
                // Kiểm tra xác nhận mật khẩu
                if (!password.equals(confirmPassword)){
                    binding.confirmPasswordEdittext.setError("Mật khẩu không khớp");
                    return;
                }

                createAccountWithFirebase(email, password);
            }
        });

        binding.gotoLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void createAccountWithFirebase(String email, String password) {
        // Hiển thị tiến trình
        setInProgress(true);

        // Gọi Firebase để tạo tài khoản
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        setInProgress(false);
                        // Thông báo khi tạo tài khoản thành công
                        Toast.makeText(getApplicationContext(), "Tạo thành công tài khoản", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        setInProgress(false);
                        // Thông báo khi tạo tài khoản thất bại
                        Toast.makeText(getApplicationContext(), "Tạo tài khoản thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setInProgress(boolean inProgress) {
        if (inProgress) {
            binding.createAccountBtn.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.createAccountBtn.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        }
    }
}