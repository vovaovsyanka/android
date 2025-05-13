package ru.mirea.ovsyannikov.mireaproject.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import ru.mirea.ovsyannikov.mireaproject.R;

public class AudioRecorderFragment extends Fragment {
    private Button recordBtn, playBtn;
    private MediaRecorder audioRecorder;
    private MediaPlayer audioPlayer;
    private String recordingPath;
    private boolean isRecording = false;
    private boolean isPlaying = false;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private boolean hasRecordPermission = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle state) {
        View root = inflater.inflate(R.layout.fragment_audio_recorder, container, false);

        hasRecordPermission = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;

        if (!hasRecordPermission) {
            requestPermissions(
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    PERMISSION_REQUEST_CODE);
        }

        recordBtn = root.findViewById(R.id.record_btn);
        playBtn = root.findViewById(R.id.play_btn);
        playBtn.setEnabled(false);

        recordingPath = requireActivity().getExternalCacheDir().getPath() + "/recording.3gp";

        recordBtn.setOnClickListener(v -> toggleRecording());
        playBtn.setOnClickListener(v -> togglePlayback());

        return root;
    }

    private void toggleRecording() {
        if (isRecording) {
            stopRecording();
            recordBtn.setText("Записать");
            playBtn.setEnabled(true);
        } else {
            startRecording();
            recordBtn.setText("Стоп");
            playBtn.setEnabled(false);
        }
        isRecording = !isRecording;
    }

    private void togglePlayback() {
        if (isPlaying) {
            stopPlayback();
            playBtn.setText("Воспроизвести");
            recordBtn.setEnabled(true);
        } else {
            startPlayback();
            playBtn.setText("Стоп");
            recordBtn.setEnabled(false);
        }
        isPlaying = !isPlaying;
    }

    private void startRecording() {
        audioRecorder = new MediaRecorder();
        audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        audioRecorder.setOutputFile(recordingPath);
        audioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            audioRecorder.prepare();
            audioRecorder.start();
            showMessage("Идет запись...");
        } catch (Exception e) {
            showMessage("Ошибка записи");
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (audioRecorder != null) {
            audioRecorder.stop();
            audioRecorder.release();
            audioRecorder = null;
            showMessage("Запись сохранена");
        }
    }

    private void startPlayback() {
        audioPlayer = new MediaPlayer();
        try {
            audioPlayer.setDataSource(recordingPath);
            audioPlayer.prepare();
            audioPlayer.start();
            showMessage("Воспроизведение...");

            audioPlayer.setOnCompletionListener(mp -> {
                stopPlayback();
                playBtn.setText("Воспроизвести");
                recordBtn.setEnabled(true);
                isPlaying = false;
            });
        } catch (Exception e) {
            showMessage("Ошибка воспроизведения");
            e.printStackTrace();
        }
    }

    private void stopPlayback() {
        if (audioPlayer != null) {
            audioPlayer.release();
            audioPlayer = null;
            showMessage("Воспроизведение остановлено");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isRecording) stopRecording();
        if (isPlaying) stopPlayback();
    }

    private void showMessage(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }
}