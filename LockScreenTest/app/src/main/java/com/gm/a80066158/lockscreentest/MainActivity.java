package com.gm.a80066158.lockscreentest;

import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gm.a80066158.lockscreen.LockScreen;
import com.gm.a80066158.lockscreen.LockScreenInfo;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Button requestAudioFocusButton = null;
    private TextView infoTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initContent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "<onResume> start");
        onRequestAudioFocusButtonClick();
    }

    private void initContent() {
        Log.i(TAG, "<initContent> start");

        requestAudioFocusButton = (Button) findViewById(R.id.requestAudioFocusBtn);
        infoTextView = (TextView) findViewById(R.id.info);

        requestAudioFocusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRequestAudioFocusButtonClick();
            }
        });
    }

    private void onRequestAudioFocusButtonClick() {
        Log.i(TAG, "<onRequestAudioFocusButtonClick> start");

        final NowplayingInfo nowplayingInfo = NowplayingInfo.getInstance();
        nowplayingInfo.setSongName("song name");
        nowplayingInfo.setArtistName("artist name");
        nowplayingInfo.setAlbumName("album name");
        nowplayingInfo.setPlayState(NowplayingInfo.PLAYBACK_STATE_PLAYING);

        final LockScreen lockScreen = LockScreen.getInstance(this, EventReceiver.class);
        lockScreen.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int i) {
                Log.i(TAG, "<onAudioFocusChange> i = " + i);

                switch (i) {
                    case AudioManager.AUDIOFOCUS_REQUEST_GRANTED: {
                        infoTextView.setText("REQUEST AUDIO FOCUS");

                        LockScreenInfo lockScreenInfo = new LockScreenInfo();
                        lockScreenInfo.setTitle(NowplayingInfo.getInstance().getSongName());
                        lockScreenInfo.setSubTitle(NowplayingInfo.getInstance().getArtistName() +
                            " - " + NowplayingInfo.getInstance().getAlbumName());
                        lockScreen.updateInfo(lockScreenInfo);
                        lockScreen.setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);
                        lockScreen.setTransportControlFlags(RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS
                                | RemoteControlClient.FLAG_KEY_MEDIA_NEXT
                                | RemoteControlClient.FLAG_KEY_MEDIA_PLAY
                                | RemoteControlClient.FLAG_KEY_MEDIA_PAUSE
                                | RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE
                                | RemoteControlClient.FLAG_KEY_MEDIA_STOP);
                        break;
                    }
                    default: {
                        infoTextView.setText("LOSE AUDIO FOCUS");
                        nowplayingInfo.setPlayState(NowplayingInfo.PLAYBACK_STATE_PAUSED);
                        lockScreen.setPlaybackState(RemoteControlClient.PLAYSTATE_PAUSED);
                    }
                }
            }
        });
    }
}
