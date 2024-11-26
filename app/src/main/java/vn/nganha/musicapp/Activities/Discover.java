package vn.nganha.musicapp.Activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import vn.nganha.musicapp.adapter.CategoryAdapter;
import vn.nganha.musicapp.databinding.ActivityDiscoverBinding;
import vn.nganha.musicapp.models.CategoryModel;

public class Discover extends AppCompatActivity {

    private ActivityDiscoverBinding binding;
    private CategoryAdapter categoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityDiscoverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Fetch categories from Firestore
        getCategories();
    }

    private void getCategories() {
        FirebaseFirestore.getInstance()
            .collection("category")
            .get()
            .addOnSuccessListener(querySnapshot -> {
                // Convert Firestore data to list of CategoryModel
                List<CategoryModel> categoryList = querySnapshot.toObjects(CategoryModel.class);

                // Log để kiểm tra URL ảnh
                for (CategoryModel category : categoryList) {
                    Log.d("Category", "Cover URL: " + category.getCoverURL());
                }

                setupCategoryRecyclerView(categoryList);
            })
            .addOnFailureListener(e -> {
                // Handle failure here
                e.printStackTrace();
            });
    }

    private void setupCategoryRecyclerView(List<CategoryModel> categoryList) {
        // Khởi tạo adapter với dữ liệu
        categoryAdapter = new CategoryAdapter(categoryList);

        // Thiết lập RecyclerView
        binding.categoriesRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        binding.categoriesRecyclerView.setAdapter(categoryAdapter);
    }
}