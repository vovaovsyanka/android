package com.example.mireaproject.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.mireaproject.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PhotoCollageFragment extends Fragment {

    private static final int MAX_IMAGES = 4;
    private ImageView[] collageSlots = new ImageView[MAX_IMAGES];
    private Uri[] capturedImageUris = new Uri[MAX_IMAGES];
    private int currentSlotIndex = 0;

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<String> permissionRequester;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupPermissionHandler();
        setupCameraResultHandler();
    }

    private void setupPermissionHandler() {
        permissionRequester = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        launchCamera();
                    } else {
                        displayMessage("Camera permission is required");
                    }
                });
    }

    private void setupCameraResultHandler() {
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        handleNewPhoto();
                    }
                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle state) {
        View view = inflater.inflate(R.layout.fragment_photo_collage, container, false);
        setupUIComponents(view);
        return view;
    }

    private void setupUIComponents(View root) {
        collageSlots[0] = root.findViewById(R.id.slot1);
        collageSlots[1] = root.findViewById(R.id.slot2);
        collageSlots[2] = root.findViewById(R.id.slot3);
        collageSlots[3] = root.findViewById(R.id.slot4);

        Button captureBtn = root.findViewById(R.id.capture_btn);
        captureBtn.setOnClickListener(v -> verifyPermissions());
    }

    private void verifyPermissions() {
        if (currentSlotIndex >= MAX_IMAGES) {
            displayMessage("Collage is complete!");
            return;
        }

        if (hasCameraPermission()) {
            launchCamera();
        } else {
            requestCameraPermission();
        }
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        permissionRequester.launch(Manifest.permission.CAMERA);
    }

    private void launchCamera() {
        try {
            File photoFile = generateImageFile();
            Uri fileUri = createUriForFile(photoFile);
            capturedImageUris[currentSlotIndex] = fileUri;

            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            captureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Intent chooser = Intent.createChooser(captureIntent, "Select camera app");
            if (chooser.resolveActivity(requireActivity().getPackageManager()) != null) {
                cameraLauncher.launch(chooser);
            } else {
                displayMessage("No camera apps available");
            }
        } catch (IOException | SecurityException e) {
            e.printStackTrace();
            displayMessage("Failed to create image file");
        }
    }

    private File generateImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date());
        String prefix = "COLLAGE_" + timestamp + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(prefix, ".jpg", storageDir);
    }

    private Uri createUriForFile(File file) {
        return FileProvider.getUriForFile(
                requireContext(),
                requireContext().getPackageName() + ".fileprovider",
                file
        );
    }

    private void handleNewPhoto() {
        if (capturedImageUris[currentSlotIndex] != null) {
            collageSlots[currentSlotIndex].setImageURI(capturedImageUris[currentSlotIndex]);
            currentSlotIndex++;

            if (currentSlotIndex >= MAX_IMAGES) {
                displayMessage("Collage completed!");
            }
        }
    }

    private void displayMessage(String text) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show();
    }
}