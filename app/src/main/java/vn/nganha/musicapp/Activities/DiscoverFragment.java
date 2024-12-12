package vn.nganha.musicapp.Activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import vn.nganha.musicapp.R;
import vn.nganha.musicapp.adapter.CategoryAdapter;
import vn.nganha.musicapp.models.CategoryModel;

public class DiscoverFragment extends Fragment {
    private RecyclerView categoriesRecyclerView;
    private CategoryAdapter categoryAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Kết nối RecyclerView
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);

        // Thiết lập RecyclerView (layout manager, adapter, etc.)
        categoriesRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        // Gọi hàm để tải dữ liệu từ Firestore
        getCategories();
    }

    private void getCategories() {
        FirebaseFirestore.getInstance()
                .collection("category")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    // Chuyển dữ liệu Firestore thành danh sách CategoryModel
                    List<CategoryModel> categoryList = querySnapshot.toObjects(CategoryModel.class);

                    // Khởi tạo adapter và gán vào RecyclerView
                    categoryAdapter = new CategoryAdapter(categoryList);
                    categoriesRecyclerView.setAdapter(categoryAdapter);
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi khi không lấy được dữ liệu
                    e.printStackTrace();
                });
    }
}