package ru.mirea.ovsyannikov.viewbinding;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.ovsyannikov.viewbinding.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.songTitle.setText("Зараза");
        binding.artistName.setText("Григорий Лепс");
        binding.currentTime.setText("0:00");
        binding.totalTime.setText("3:45");
        binding.seekBar.setMax(100);

        binding.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaying = !isPlaying;
                binding.playButton.setText(isPlaying ? "Pause" : "Play");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}