package ru.mirea.ovsyannikov.audiorecord;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String APP_TAG = "AudioRecorderDemo";
    private static final int PERMISSION_REQUEST_CODE = 100;

    private Button btnRecord;
    private Button btnPlay;

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;

    private boolean recording = false;
    private boolean playing = false;

    private String audioFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRecord = findViewById(R.id.recordButton);
        btnPlay = findViewById(R.id.playButton);
        btnPlay.setEnabled(false);

        audioFilePath = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "audio_sample.3gp").getAbsolutePath();

        verifyPermissions();

        btnRecord.setOnClickListener(view -> toggleRecording());
        btnPlay.setOnClickListener(view -> togglePlayback());
    }

    private void verifyPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    PERMISSION_REQUEST_CODE);
        }
    }

    private void toggleRecording() {
        if (!recording) {
            beginRecording();
        } else {
            endRecording();
        }
        recording = !recording;
    }

    private void togglePlayback() {
        if (!playing) {
            beginPlayback();
        } else {
            endPlayback();
        }
        playing = !playing;
    }

    private void beginRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(audioFilePath);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
            btnRecord.setText("Stop Recording");
            btnPlay.setEnabled(false);
            Log.d(APP_TAG, "Recording started at: " + audioFilePath);
        } catch (IOException e) {
            Log.e(APP_TAG, "Failed to start recording: ", e);
            Toast.makeText(this, "Failed to start recording", Toast.LENGTH_SHORT).show();
        }
    }

    private void endRecording() {
        try {
            mediaRecorder.stop();
        } catch (RuntimeException e) {
            Log.w(APP_TAG, "Stop called without valid start", e);
        }
        mediaRecorder.release();
        mediaRecorder = null;
        Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show();
        btnRecord.setText("Start Recording");
        btnPlay.setEnabled(true);
        Log.d(APP_TAG, "Recording stopped");
    }

    private void beginPlayback() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioFilePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(this, "Playback started", Toast.LENGTH_SHORT).show();
            btnPlay.setText("Stop Playback");
            btnRecord.setEnabled(false);

            mediaPlayer.setOnCompletionListener(mp -> {
                endPlayback();
                btnPlay.setText("Play");
                btnRecord.setEnabled(true);
                playing = false;
                Log.d(APP_TAG, "Playback completed");
            });

            Log.d(APP_TAG, "Playing audio from: " + audioFilePath);
        } catch (IOException e) {
            Log.e(APP_TAG, "Failed to play audio: ", e);
            Toast.makeText(this, "Playback failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void endPlayback() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            Toast.makeText(this, "Playback stopped", Toast.LENGTH_SHORT).show();
            btnPlay.setText("Play");
            btnRecord.setEnabled(true);
            Log.d(APP_TAG, "Playback stopped");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Microphone permission is required", Toast.LENGTH_SHORT).show();
                btnRecord.setEnabled(false);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        cleanup();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cleanup();
    }

    private void cleanup() {
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
