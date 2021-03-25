package com.rghapp.exoplayerworkshop;

import java.util.ArrayList;

public class VodVideoObject {

    public String title;
    public String imageUrl;
    public String streamUrl;
    public ArrayList<VodSubtitleObject> subtitles = new ArrayList<>();
    public VodSettings setting = new VodSettings();


    static public class VodSettings {
        public boolean isForcePlayFromFirst = false;
        public int startTime = -1;
        public boolean autoStart = true;
        public int subtitle_id = -1;
        public int quality_id = -1;
    }
}
