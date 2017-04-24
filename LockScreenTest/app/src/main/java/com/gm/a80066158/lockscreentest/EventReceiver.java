package com.gm.a80066158.lockscreentest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RemoteControlClient;
import android.util.EventLog;
import android.util.Log;
import android.view.KeyEvent;

import com.gm.a80066158.lockscreen.LockScreen;

/**
 * Created by 80066158 on 2017-04-24.
 */

public class EventReceiver extends BroadcastReceiver {
    private static final String TAG = "EventReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "<onReceive> " + intent.getAction());

        String action = intent.getAction();
        KeyEvent keyEvent = null;

        if (action.equals("android.intent.action.MEDIA_BUTTON")) {
            keyEvent = (KeyEvent) intent.getExtras().get("android.intent.extra.KEY_EVENT");
            if (KeyEvent.ACTION_UP == keyEvent.getAction()) {
                Log.i(TAG, "<onReceive> " + keyEvent.getKeyCode());
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    case KeyEvent.KEYCODE_MEDIA_PLAY:
                    case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE: {
                        LockScreen lockScreen = LockScreen.getInstance(null, EventReceiver.class);

                        if (NowplayingInfo.PLAYBACK_STATE_PAUSED == NowplayingInfo.getInstance().getPlayState()) {
                            NowplayingInfo.getInstance().setPlayState(NowplayingInfo.PLAYBACK_STATE_PLAYING);
                            lockScreen.setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);
                        } else if (NowplayingInfo.PLAYBACK_STATE_PLAYING == NowplayingInfo.getInstance().getPlayState()) {
                            NowplayingInfo.getInstance().setPlayState(NowplayingInfo.PLAYBACK_STATE_PAUSED);
                            lockScreen.setPlaybackState(RemoteControlClient.PLAYSTATE_PAUSED);
                        }

                        break;
                    }
                    case KeyEvent.KEYCODE_MEDIA_NEXT: {
                        break;
                    }
                    case KeyEvent.KEYCODE_MEDIA_PREVIOUS: {
                        break;
                    }
                }
            }
        }
    }
}
