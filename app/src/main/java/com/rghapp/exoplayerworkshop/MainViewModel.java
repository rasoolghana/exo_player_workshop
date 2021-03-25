package com.rghapp.exoplayerworkshop;

import androidx.lifecycle.ViewModel;

/**
 * Created by Rasool Ghana on 3/25/21.
 * Email : Rasool.ghana@gmail.com
 */
public class MainViewModel extends ViewModel {

    private RGHVodPlayer vodPlayer;

    public void setVodPlayer(RGHVodPlayer vodPlayer) {
        this.vodPlayer = vodPlayer;
    }

    public void playDashVideoWithSub(){
        VodVideoObject videoObject = new VodVideoObject();
        VodSubtitleObject vodSubtitleObject = new VodSubtitleObject("en","https://mkvtoolnix.download/samples/vsshort-en.srt");
        videoObject.streamUrl = "https://5b44cf20b0388.streamlock.net:8443/vod/smil:bbb.smil/manifest.mpd";
        videoObject.subtitles.add(vodSubtitleObject);
        vodPlayer.playMedia(videoObject, VodStreamType.DASH);
    }

    public void playHLSVideo(){
        VodVideoObject videoObject = new VodVideoObject();
        videoObject.streamUrl = "https://5b44cf20b0388.streamlock.net:8443/vod/smil:bbb.smil/playlist.m3u8";
        vodPlayer.playMedia(videoObject, VodStreamType.HLS);
    }

    public void playerForward(){
        vodPlayer.playerForward(5000);
    }

    public void playerRewind(){
        vodPlayer.playerRewind(5000);
    }

}
