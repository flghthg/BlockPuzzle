package com.two.blockpuzzle;


import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;


public class BgmManager {

    MediaPlayer mediaPlayer;
    Context context;
    static int pausePosition;
    static boolean isPlaying;

    final static int BGM_ON = 0;
    final static int BGM_OFF = 1;
    static int bgmSound;

    public BgmManager(Context context, MediaPlayer mediaPlayer) {
        this.context = context;
        this.mediaPlayer = mediaPlayer;
       // Log.i("Player", "" + this.mediaPlayer);
    }

    public void bgmStart(int bgm){

        Log.i("test", "is : " + isPlaying);

        mediaPlayer = MediaPlayer.create(context, bgm);
        mediaPlayer.setLooping(true);

        if (isPlaying == false){
            mediaPlayer.seekTo(pausePosition);
        }
        mediaPlayer.start();

    }

    public void bgmStop(){
        Log.i("test", "Manager : bgmStop");
        mediaPlayer.stop();
    }

    public void bgmPause(){
        Log.i("test", "Manager : bgmPause");
        mediaPlayer.pause();
        pausePosition = mediaPlayer.getCurrentPosition(); // 현재 재생 위치

    }

}








