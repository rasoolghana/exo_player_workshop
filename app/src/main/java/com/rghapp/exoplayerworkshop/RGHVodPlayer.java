package com.rghapp.exoplayerworkshop;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.MimeTypes;

import java.util.ArrayList;


public class RGHVodPlayer implements Player.EventListener {

    private int lastReportedPlaybackState;
    private final VodPlayerEventLogger playerEventLogger;
    private VodVideoObject currentVideoObject;
    DefaultTrackSelector trackSelector;
    SimpleExoPlayer vodPlayer;
    Context context;

    public RGHVodPlayer(Context context){
        this.context = context;
        trackSelector = new DefaultTrackSelector(this.context);
        trackSelector.setParameters(trackSelector.buildUponParameters());

        playerEventLogger = new VodPlayerEventLogger();
        lastReportedPlaybackState = ExoPlayer.STATE_IDLE;
    }

    public void playMedia(VodVideoObject videoObject,VodStreamType streamType){
        if (videoObject == null)
            return;
        this.currentVideoObject = videoObject;
        initializePlayer();

        String mimeType;
        if (streamType == VodStreamType.DASH)
            mimeType = MimeTypes.APPLICATION_MPD;
        else if (streamType == VodStreamType.SS)
            mimeType = MimeTypes.APPLICATION_SS;
        else {
            mimeType = MimeTypes.APPLICATION_M3U8;
        }

        ArrayList<MediaItem.Subtitle> subtitles = new ArrayList<>();

        for (VodSubtitleObject subtitleObject : currentVideoObject.subtitles) {
            MediaItem.Subtitle subtitle =
                    new MediaItem.Subtitle(
                            Uri.parse(subtitleObject.file_url),
                            MimeTypes.APPLICATION_SUBRIP,
                            subtitleObject.language,
                            currentVideoObject.setting.subtitle_id == subtitleObject.id ? C.SELECTION_FLAG_DEFAULT : -1);
            subtitles.add(subtitle);
        }

        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(currentVideoObject.streamUrl)
                .setMimeType(mimeType)
                .setSubtitles(subtitles)
                .build();
        if (videoObject.setting.isForcePlayFromFirst){
            vodPlayer.seekTo(0);
        }else if (videoObject.setting.startTime >= 0){
            vodPlayer.seekTo(videoObject.setting.startTime * 1000);
        }

        getPlayerView().setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        getPlayerView().setPlayer(vodPlayer);

        vodPlayer.setMediaItem(mediaItem);
        vodPlayer.prepare();
    }

    public PlayerView getPlayerView(){
        return new PlayerView(context);
    }

    public void pausePlayer(){
        if (vodPlayer != null){
            vodPlayer.pause();
        }
    }

    public void startPlayer(){
        if (vodPlayer != null){
            vodPlayer.play();
        }
    }

    public void destroyPlayer(){
        if (vodPlayer != null){
            vodPlayer.release();
        }
    }

    public void playerForward(long duration){
        if (vodPlayer != null){
            vodPlayer.seekTo(vodPlayer.getCurrentPosition() + duration);
        }
    }

    public void playerRewind(long duration){
        if (vodPlayer != null){
            vodPlayer.seekTo(vodPlayer.getCurrentPosition() - duration);
        }
    }

    private void initializePlayer(){
        if (vodPlayer != null)
            return;
        vodPlayer = new SimpleExoPlayer.Builder(context)
                .setTrackSelector(trackSelector)
                .build();
        vodPlayer.setPlayWhenReady(currentVideoObject.setting.autoStart);
        vodPlayer.addListener(this);
    }


    @Override
    public void onIsLoadingChanged(boolean isLoading) {
        playerEventLogger.onIsLoadingChanged(isLoading);
    }

    @Override
    public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {
        playerEventLogger.onPlayWhenReadyChanged(playWhenReady,reason);
    }

    @Override
    public void onPlaybackStateChanged(int state) {
        playerEventLogger.onPlaybackStateChanged(state);
        lastReportedPlaybackState = state;
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        playerEventLogger.onPlaybackParametersChanged(playbackParameters);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, int reason) {
        playerEventLogger.onTimelineChanged(timeline,reason);
    }
}
