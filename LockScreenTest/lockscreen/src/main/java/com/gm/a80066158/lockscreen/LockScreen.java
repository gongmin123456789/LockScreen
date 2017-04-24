package com.gm.a80066158.lockscreen;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.RemoteControlClient;
import android.util.Log;

/**
 * Created by 80066158 on 2017-04-20.
 */

public class LockScreen {
    private static final String TAG = "LockScreen";

    private Context context = null;
    private static LockScreen instance = null;
    private AudioManager audioManager = null;
    private boolean isRequestedAudioFocus = false;
    private Class<?> eventReceiverClass = null;
    private RemoteControlClient remoteControlClient = null;

    private LockScreen(Context context, Class<?> eventReceiverClass) {
        this.context = context;
        this.eventReceiverClass = eventReceiverClass;
    }

    public synchronized static LockScreen getInstance(Context context, Class<?> eventReceiverClass) {
        if (null == instance) {
            instance = new LockScreen(context, eventReceiverClass);
        }

        return instance;
    }

    public int requestAudioFocus(final AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener) {
        Log.i(TAG, "<requestAudioFocus> isRequestedAudioFocus = " + isRequestedAudioFocus);
        if (isRequestedAudioFocus) {
            return AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        }

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int i) {
                Log.i(TAG, "<requestAudioFocus:onAudioFocusChange> i = " + i);
                if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == i) {
                    isRequestedAudioFocus = true;
                    initLockScreen();
                } else {
                    isRequestedAudioFocus = false;
                }

                if (null != onAudioFocusChangeListener) {
                    onAudioFocusChangeListener.onAudioFocusChange(i);
                }
            }
        }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        Log.i(TAG, "<requestAudioFocus> result = " + result);
        if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == result) {
            isRequestedAudioFocus = true;
            initLockScreen();
        } else {
            isRequestedAudioFocus = false;
        }

        if (null != onAudioFocusChangeListener) {
            onAudioFocusChangeListener.onAudioFocusChange(result);
        }

        return result;
    }

    public boolean isRequestedAudioFocus() {
        return isRequestedAudioFocus;
    }

    public void updateInfo(LockScreenInfo info) {
        Log.i(TAG, "<updateInfo> " + info.getTitle() + ", " +
            info.getSubTitle() + ", " + info.getIconUrl());

        RemoteControlClient.MetadataEditor md = LockScreen.this.remoteControlClient.editMetadata(false);
        md.putString(MediaMetadataRetriever.METADATA_KEY_TITLE, info.getTitle());
        md.putString(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST, info.getSubTitle());
        Bitmap defaultIconBitmap = new BitmapFactory().decodeResource(context.getResources(), R.drawable.lock_screen_default_icon);
        md.putBitmap(RemoteControlClient.MetadataEditor.BITMAP_KEY_ARTWORK, defaultIconBitmap);
        md.apply();
    }

    /**
     *
     * @param playState
     *      such as: RemoteControlClient.PLAYSTATE_PLAYING
     */
    public void setPlaybackState(int playState) {
        Log.i(TAG, "<setPlaybackState> playState = " + playState);

        remoteControlClient.setPlaybackState(playState);
    }

    /**
     *
     * @param flags
     *      such as:
     *      RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS
                | RemoteControlClient.FLAG_KEY_MEDIA_NEXT
                | RemoteControlClient.FLAG_KEY_MEDIA_PLAY
                | RemoteControlClient.FLAG_KEY_MEDIA_PAUSE
                | RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE
                | RemoteControlClient.FLAG_KEY_MEDIA_STOP
     */
    public void setTransportControlFlags(int flags) {
        Log.i(TAG, "<setTransportControlFlags> flags = " + flags);
        remoteControlClient.setTransportControlFlags(flags);
    }

    private void initLockScreen() {
        Log.i(TAG, "<initLockScreen> start");

        ComponentName eventReceiver = new ComponentName(context, eventReceiverClass);
        audioManager.registerMediaButtonEventReceiver(eventReceiver);
        // build the PendingIntent for the remote control client
        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        mediaButtonIntent.setComponent(eventReceiver);
        PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, mediaButtonIntent, 0);
        // create and register the remote control client
        remoteControlClient = new RemoteControlClient(mediaPendingIntent);
        audioManager.registerRemoteControlClient(remoteControlClient);
    }
}
