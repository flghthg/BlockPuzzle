package com.two.blockpuzzle;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

public class SoundManager {

    private SoundPool soundPool;
    private HashMap<Integer, Integer> soundPoolMap;
    private AudioManager audioManager;
    private Context context;

    final static int EFFECT_ON = 0;
    final static int EFFECT_OFF = 1;
    static int effectSound;

    final static int BLOCK_DOWN = 0;
    final static int BLOCK_DOWN2 = 1;
    final static int LINE_CLEAR = 2;
    final static int LINE_CLEAR2 = 3;
    final static int ITEM_3ROWS_CLEAR = 4;
    final static int ITEM_3ROWS_CLEAR2 = 5;
    final static int ITEM_ALLCLEAR = 6;
    final static int ITEM_ALLCLEAR2 = 7;
    final static int GAME_OVER = 8;
    final static int BTN_CLICK = 9;
    final static int BTN_CLICK2 = 10;
    final static int BTN_BUY = 11;
    final static int BTN_BUY2 = 12;
    final static int ITEM_SPEED_DOWN = 13;
    final static int ITEM_SPEED_DOWN2 = 14;

    public SoundManager(Context context, SoundPool soundPool){
        this.context = context;
        this.soundPool = soundPool;
        soundPoolMap = new HashMap<Integer, Integer>();
        audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

    }

    // 효과음 추가
    public void addSound(){
        soundPoolMap.put(BLOCK_DOWN, soundPool.load(context, R.raw.block_down, 1));
        soundPoolMap.put(BLOCK_DOWN2, soundPool.load(context, R.raw.block_down, 2));
        soundPoolMap.put(LINE_CLEAR, soundPool.load(context, R.raw.line_clear, 3));
        soundPoolMap.put(LINE_CLEAR2, soundPool.load(context, R.raw.line_clear, 4));
        soundPoolMap.put(ITEM_3ROWS_CLEAR, soundPool.load(context, R.raw.item_3rows_clear, 5));
        soundPoolMap.put(ITEM_3ROWS_CLEAR2, soundPool.load(context, R.raw.item_3rows_clear, 6));
        soundPoolMap.put(ITEM_ALLCLEAR, soundPool.load(context, R.raw.item_allclear, 7));
        soundPoolMap.put(ITEM_ALLCLEAR2, soundPool.load(context, R.raw.item_allclear, 8));
        soundPoolMap.put(GAME_OVER, soundPool.load(context, R.raw.game_over, 9));
        soundPoolMap.put(BTN_CLICK, soundPool.load(context, R.raw.btn_click, 10));
        soundPoolMap.put(BTN_CLICK2, soundPool.load(context, R.raw.btn_click, 11));
        soundPoolMap.put(BTN_BUY, soundPool.load(context, R.raw.btn_buy, 12));
        soundPoolMap.put(BTN_BUY2, soundPool.load(context, R.raw.btn_buy, 13));
        soundPoolMap.put(ITEM_SPEED_DOWN, soundPool.load(context, R.raw.item_speed_down, 14));
        soundPoolMap.put(ITEM_SPEED_DOWN2, soundPool.load(context, R.raw.item_speed_down, 15));
    }

    // 효과음 재생
    public int playSound(int index){
        int streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        return soundPool.play(soundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1f);
    }

    // 효과음 볼륨 설정
    public void setVolume(){
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 5, AudioManager.FLAG_SHOW_UI);
    }

    // ---------------------------------------- 사용하지 않은 메서드 -------------------------------------------
    // 효과음 정지
    public void stopSound(int streamId){
        soundPool.stop(streamId);
    }

    // 효과음 일시정지
    public void pauseSound(int streamId){
        soundPool.pause(streamId);
    }

    // 효과음 다시 재생
    public void resumeSound(int streamId){
        soundPool.resume(streamId);
    }

    // 효과음 볼륨 조절 (-)
    public boolean volumeDown(){
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
        return true;
    }

    // 효과음 볼륨 조절 (+)
    public boolean volumeUp(){
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
        return true;
    }

    // 효과음 볼륨 음소거
    public boolean volumeMute(){
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, AudioManager.FLAG_SHOW_UI);
        return true;
    }

}
