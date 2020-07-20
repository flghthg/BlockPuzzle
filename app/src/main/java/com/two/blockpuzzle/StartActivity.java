package com.two.blockpuzzle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class StartActivity extends Activity {

    Button btn_start;

    MediaPlayer mediaPlayer;
    static BgmManager bgmManager;

    SoundPool soundPool;
    static SoundManager soundManager;

    SharedPreferences pref_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // BGM & 효과음 저장 상태 가져오기
        pref_sound = getSharedPreferences("SOUND", MODE_PRIVATE);
        SoundManager.effectSound = pref_sound.getInt("sound_status", 0);
        BgmManager.bgmSound = pref_sound.getInt("bgm_status", 0);

        btn_start = findViewById(R.id.btn_start);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SoundManager.effectSound == SoundManager.EFFECT_ON) {
                    soundManager.playSound(SoundManager.BTN_CLICK); // 버튼 클릭 효과음
                    soundManager.playSound(SoundManager.BTN_CLICK2); // 버튼 클릭 효과음
                }

                new LoginActivity(StartActivity.this);
            }//btn_start onclick
        });

        // BGM 객체 생성
        bgmManager = new BgmManager(this, mediaPlayer);

        // SoundPool객체 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            // 롤리팝 이상 버전의 경우
            soundPool = new SoundPool.Builder().build();

        }else{
            // 롤리팝 이하 버전의 경우
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        }

        // SoundManager객체 생성
        soundManager = new SoundManager(this, soundPool);

        // 효과음 추가
        soundManager.addSound();

    } // onCreate

    @Override
    protected void onPause() {
        super.onPause();
        bgmManager.bgmPause();
        BgmManager.isPlaying = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("bgmSound", ""+BgmManager.bgmSound);
        if (BgmManager.bgmSound == BgmManager.BGM_ON){
            bgmManager.bgmStart(R.raw.bgm_main);
            BgmManager.isPlaying = true;
        } else if(BgmManager.bgmSound == BgmManager.BGM_OFF){
            bgmManager.bgmStart(R.raw.bgm_main);
            bgmManager.bgmPause();
            BgmManager.isPlaying = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bgmManager.bgmStop();
        BgmManager.pausePosition = 0;
    }
}