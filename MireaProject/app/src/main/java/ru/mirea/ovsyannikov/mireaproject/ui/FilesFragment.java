package ru.mirea.ovsyannikov.mireaproject.ui;

import android.util.Base64;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import ru.mirea.ovsyannikov.mireaproject.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FilesFragment extends Fragment {

    private RecyclerView rvFiles;
    private TextView tvFileContent, tvFileContentLabel;
    private ScrollView svFileContentContainer;
    private Button btnEncryptDecrypt;
    private FloatingActionButton fabAddFile;
    private FileAdapter fileAdapter;
    private List<FileItem> fileItems = new ArrayList<>();
    private File selectedFile;
    private boolean isEncrypted = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_files, container, false);

        rvFiles = view.findViewById(R.id.rvFiles);
        tvFileContent = view.findViewById(R.id.tvFileContent);
        tvFileContentLabel = view.findViewById(R.id.tvFileContentLabel);
        svFileContentContainer = view.findViewById(R.id.svFileContentContainer);
        btnEncryptDecrypt = view.findViewById(R.id.btnEncryptDecrypt);
        fabAddFile = view.findViewById(R.id.fabAddFile);

        setupRecyclerView();
        loadFiles();

        fabAddFile.setOnClickListener(v -> showAddFileDialog());

        btnEncryptDecrypt.setOnClickListener(v -> {
            if (selectedFile != null) {
                if (isEncrypted) {
                    decryptFile(selectedFile);
                } else {
                    encryptFile(selectedFile);
                }
            }
        });

        return view;
    }

    private void setupRecyclerView() {
        fileAdapter = new FileAdapter(fileItems, file -> {
            selectedFile = file;
            isEncrypted = file.getName().endsWith(".enc");
            btnEncryptDecrypt.setText(isEncrypted ? "Расшифровать файл" : "Зашифровать файл");
            btnEncryptDecrypt.setVisibility(View.VISIBLE);
            tvFileContentLabel.setVisibility(View.VISIBLE);
            svFileContentContainer.setVisibility(View.VISIBLE);

            try {
                String content = readFile(file);
                tvFileContent.setText(content);
            } catch (IOException e) {
                Toast.makeText(getContext(), "Ошибка чтения файла", Toast.LENGTH_SHORT).show();
            }
        });

        rvFiles.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFiles.setAdapter(fileAdapter);
    }

    private void loadFiles() {
        fileItems.clear();
        File filesDir = requireContext().getFilesDir();
        File[] files = filesDir.listFiles();

        if (files != null) {
            for (File file : files) {
                fileItems.add(new FileItem(file.getName(), file));
            }
        }

        fileAdapter.notifyDataSetChanged();
    }

    private void showAddFileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Создать новый файл");

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_file, null);
        EditText etFileName = dialogView.findViewById(R.id.etFileName);
        EditText etFileContent = dialogView.findViewById(R.id.etFileContent);
        SwitchMaterial swEncrypt = dialogView.findViewById(R.id.swEncrypt);

        builder.setView(dialogView);
        builder.setPositiveButton("Создать", (dialog, which) -> {
            String fileName = etFileName.getText().toString();
            String content = etFileContent.getText().toString();
            boolean encrypt = swEncrypt.isChecked();

            if (!fileName.isEmpty() && !content.isEmpty()) {
                createFile(fileName, content, encrypt);
            } else {
                Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void createFile(String fileName, String content, boolean encrypt) {
        try {
            String finalFileName = encrypt ? fileName + ".enc" : fileName;
            File file = new File(requireContext().getFilesDir(), finalFileName);

            if (encrypt) {
                content = encryptContent(content);
            }

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes(StandardCharsets.UTF_8));
            fos.close();

            loadFiles();
            Toast.makeText(getContext(), "Файл создан", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getContext(), "Ошибка создания файла", Toast.LENGTH_SHORT).show();
        }
    }

    private void encryptFile(File file) {
        try {
            String content = readFile(file);
            String encryptedContent = encryptContent(content);

            File encryptedFile = new File(requireContext().getFilesDir(), file.getName() + ".enc");
            FileOutputStream fos = new FileOutputStream(encryptedFile);
            fos.write(encryptedContent.getBytes(StandardCharsets.UTF_8));
            fos.close();

            file.delete();
            loadFiles();
            selectFile(encryptedFile);
            Toast.makeText(getContext(), "Файл зашифрован", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getContext(), "Ошибка шифрования", Toast.LENGTH_SHORT).show();
        }
    }

    private void decryptFile(File file) {
        try {
            String encryptedContent = readFile(file);
            String decryptedContent = decryptContent(encryptedContent);

            String originalName = file.getName().replace(".enc", "");
            File decryptedFile = new File(requireContext().getFilesDir(), originalName);

            FileOutputStream fos = new FileOutputStream(decryptedFile);
            fos.write(decryptedContent.getBytes(StandardCharsets.UTF_8));
            fos.close();

            file.delete();
            loadFiles();
            selectFile(decryptedFile);
            Toast.makeText(getContext(), "Файл расшифрован", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getContext(), "Ошибка дешифрования", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectFile(File file) {
        selectedFile = file;
        isEncrypted = file.getName().endsWith(".enc");
        btnEncryptDecrypt.setText(isEncrypted ? "Расшифровать файл" : "Зашифровать файл");

        try {
            String content = readFile(file);
            tvFileContent.setText(content);
        } catch (IOException e) {
            Toast.makeText(getContext(), "Ошибка чтения файла", Toast.LENGTH_SHORT).show();
        }
    }

    private String readFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] contentBytes = new byte[(int) file.length()];
        fis.read(contentBytes);
        fis.close();
        return new String(contentBytes, StandardCharsets.UTF_8);
    }

    private String encryptContent(String content) {
        // Шифрование с помощью Base64 (Android-версия)
        byte[] data = content.getBytes(StandardCharsets.UTF_8);
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    private String decryptContent(String encryptedContent) {
        // Дешифровка Base64 (Android-версия)
        byte[] decodedBytes = Base64.decode(encryptedContent, Base64.DEFAULT);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}