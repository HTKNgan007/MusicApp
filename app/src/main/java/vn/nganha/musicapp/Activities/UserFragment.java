package vn.nganha.musicapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import vn.nganha.musicapp.R;
import vn.nganha.musicapp.databinding.FragmentUserBinding;
import vn.nganha.musicapp.models.User;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Lấy thông tin người dùng từ Firebase
        loadUserInfo();

        // Xử lý nút lưu thông tin
        binding.btnSave.setOnClickListener(view -> saveUserInfo());

        // Xử lý nút đăng xuất
        binding.btnLogout.setOnClickListener(view -> logout());

        return binding.getRoot();
    }

    private void loadUserInfo() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            // Hiển thị email
            binding.etEmail.setText(user.getEmail());

            // Lấy thêm thông tin từ Firestore
            DocumentReference docRef = firestore.collection("users").document(user.getUid());
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("name");
                    String phone = documentSnapshot.getString("phone");
                    binding.etName.setText(name);
                    binding.etPhone.setText(phone);
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Không tải được thông tin", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void saveUserInfo() {
        String name = binding.etName.getText().toString();
        String phone = binding.etPhone.getText().toString();

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            DocumentReference docRef = firestore.collection("users").document(user.getUid());

            // Kiểm tra nếu tài liệu tồn tại, nếu không thì tạo mới
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    // Cập nhật nếu tài liệu đã tồn tại
                    docRef.update("name", name, "phone", phone)
                            .addOnSuccessListener(unused -> Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show());
                } else {
                    // Nếu tài liệu không tồn tại, tạo mới
                    docRef.set(new User(name, phone))
                            .addOnSuccessListener(unused -> Toast.makeText(getContext(), "Tạo thông tin mới thành công", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Tạo thông tin mới thất bại", Toast.LENGTH_SHORT).show());
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Lỗi khi kiểm tra tài liệu", Toast.LENGTH_SHORT).show();
            });
        }
    }


    private void logout() {
        if (MyExoplayer.getInstance() != null) {
            MyExoplayer.getInstance().release();
        }
        auth.signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
}