package com.gm.a80066158.lockscreentest;

/**
 * Created by 80066158 on 2017-04-24.
 */

public class NowplayingInfo {
    public static final int PLAYBACK_STATE_STOPED = 0;
    public static final int PLAYBACK_STATE_PLAYING = 1;
    public static final int PLAYBACK_STATE_PAUSED = 2;

    private static NowplayingInfo instance = null;

    private String songName = null;
    private String artistName = null;
    private String albumName = null;
    private String iconUrl = null;
    private int playState = PLAYBACK_STATE_STOPED;

    private NowplayingInfo() {

    }

    public synchronized static NowplayingInfo getInstance() {
        if (null == instance) {
            instance = new NowplayingInfo();
        }

        return instance;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getPlayState() {
        return playState;
    }

    public void setPlayState(int playState) {
        this.playState = playState;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }
}
