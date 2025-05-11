package com.example.mireaproject.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    private final List<FileItem> fileItems;
    private final OnFileClickListener listener;

    public FileAdapter(List<FileItem> fileItems, OnFileClickListener listener) {
        this.fileItems = fileItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        FileItem fileItem = fileItems.get(position);
        holder.tvFileName.setText(fileItem.getName());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFileClick(fileItem.getFile());
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileItems.size();
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {
        TextView tvFileName;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFileName = itemView.findViewById(android.R.id.text1);
        }
    }

    public interface OnFileClickListener {
        void onFileClick(File file);
    }
}
