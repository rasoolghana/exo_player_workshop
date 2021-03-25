package com.rghapp.exoplayerworkshop;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.exoplayer2.ui.PlayerView;
import com.rghapp.exoplayerworkshop.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding binding;
    MainViewModel viewModel;
    RGHVodPlayer rghVodPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        rghVodPlayer = new RGHVodPlayer(this){
            @Override
            public PlayerView getPlayerView() {
                return binding.exoPlayerView;
            }
        };

        viewModel.setVodPlayer(rghVodPlayer);

        initViews();
    }

    private void initViews(){
        binding.playDashButton.setOnClickListener(this);
        binding.playHlsButton.setOnClickListener(this);
        binding.playerForward.setOnClickListener(this);
        binding.playerRewind.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (rghVodPlayer == null)
            return;
        rghVodPlayer.startPlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (rghVodPlayer == null)
            return;
        rghVodPlayer.pausePlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rghVodPlayer == null)
            return;
        rghVodPlayer.destroyPlayer();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.play_dash_button){
            viewModel.playDashVideoWithSub();
        }else if (v.getId() == R.id.play_hls_button){
            viewModel.playHLSVideo();
        }else if (v.getId() == R.id.player_forward){
            viewModel.playerForward();
        }else if (v.getId() == R.id.player_rewind){
            viewModel.playerRewind();
        }
    }
}