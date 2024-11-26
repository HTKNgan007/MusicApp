package vn.nganha.musicapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import vn.nganha.musicapp.R;
import vn.nganha.musicapp.databinding.CategoryItemRecyclerBinding;
import vn.nganha.musicapp.models.CategoryModel;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private final List<CategoryModel> categoryList;

    public CategoryAdapter(List<CategoryModel> categoryList) {
        this.categoryList = categoryList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final CategoryItemRecyclerBinding binding;

        // Constructor
        public MyViewHolder(CategoryItemRecyclerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        // Bind data to views
        public void bindData(CategoryModel category) {
            binding.nameTextView.setText(category.getName());
            Glide.with(binding.coverImageView.getContext())
                    .load(category.getCoverURL())
                    .apply(new RequestOptions().placeholder(R.drawable.avatar_song) // placeholder image
                            .error(R.drawable.bg_app)) // error image
                    .into(binding.coverImageView);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryItemRecyclerBinding binding = CategoryItemRecyclerBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindData(categoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
