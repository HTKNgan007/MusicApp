package vn.nganha.musicapp.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import vn.nganha.musicapp.R;
import vn.nganha.musicapp.adapter.CategoryAdapter;
import vn.nganha.musicapp.adapter.SectionSongListAdapter;
import vn.nganha.musicapp.models.CategoryModel;

public class DiscoverFragment extends Fragment {
    private RecyclerView categoriesRecyclerView;
    private CategoryAdapter categoryAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        // Hiển thị thêm Section
        setupSection("section_1",
                view.findViewById(R.id.section_1_main_layout),
                view.findViewById(R.id.section_1_title),
                view.findViewById(R.id.section_1_recycler_view));
        setupSection("section_2",
                view.findViewById(R.id.section_2_main_layout),
                view.findViewById(R.id.section_2_title),
                view.findViewById(R.id.section_2_recycler_view));
        setupSection("Section_3",
                view.findViewById(R.id.section_3_main_layout),
                view.findViewById(R.id.section_3_title),
                view.findViewById(R.id.section_3_recycler_view));
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

    private void setupSection(String id, RelativeLayout mainLayout, TextView titleView, RecyclerView recyclerView) {

        // Thiết lập layout manager cho RecyclerView
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        // Lấy dữ liệu từ Firestore
        FirebaseFirestore.getInstance()
                .collection("sections") // Truy cập collection "sections" ở cloud firestore
                .document(id) // Truy cập document với id đưa vào
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    // Chuyển đổi dữ liệu Firestore thành đối tượng CategoryModel
                    CategoryModel section = documentSnapshot.toObject(CategoryModel.class);
                    if (section != null) {
                        // Thiết lập tiêu đề (name) cho section
                        titleView.setText(section.getName()); // Gán tên section vào TextView

                        // Khởi tạo adapter cho Section
                        SectionSongListAdapter sectionAdapter = new SectionSongListAdapter(section.getSongs());

                        // Gán adapter vào RecyclerView
                        recyclerView.setAdapter(sectionAdapter);

                        // set trạng thái hiển thị cho section nếu có nội dung
                        mainLayout.setVisibility(View.VISIBLE);
                        // Thiết lập sự kiện khi click vào section
                        mainLayout.setOnClickListener(v -> {
                            // Chuyển dữ liệu category sang Activity tiếp theo
                            SongsListActivity.category = section;
                            startActivity(new Intent(getContext(), SongsListActivity.class));
                        });
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi khi không lấy được dữ liệu
                    e.printStackTrace();
                });
    }

}