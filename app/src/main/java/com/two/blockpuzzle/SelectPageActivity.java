package com.two.blockpuzzle;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class SelectPageActivity extends Activity {

    Button btn_start, btn_tuto, btn_rank, btn_shop, btn_finish, btn_ok;
    ImageView icon_sound, img_sound_on, img_sound_off, img_bgm_on, img_bgm_off;
    Intent i;
    SharedPreferences pref, pref_sound;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_page);

        pref = getSharedPreferences("SHARE", MODE_PRIVATE);
        // BGM & 효과음 저장 상태 가져오기
        pref_sound = getSharedPreferences("SOUND", MODE_PRIVATE);
        SoundManager.effectSound = pref_sound.getInt("sound_status", 0);
        BgmManager.bgmSound = pref_sound.getInt("bgm_status", 0);

        btn_start = findViewById(R.id.btn_start);
        btn_tuto = findViewById(R.id.btn_tuto);
        btn_rank = findViewById(R.id.btn_rank);
        btn_shop = findViewById(R.id.btn_shop);
        btn_finish = findViewById(R.id.btn_finish);
        icon_sound = findViewById(R.id.icon_sound);

        btn_start.setOnClickListener( click );
        btn_tuto.setOnClickListener( click );
        btn_rank.setOnClickListener( click );
        btn_shop.setOnClickListener( click );
        btn_finish.setOnClickListener( click );
        icon_sound.setOnClickListener(icon_click);
    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (SoundManager.effectSound == SoundManager.EFFECT_ON){
                StartActivity.soundManager.playSound(SoundManager.BTN_CLICK); // 버튼 클릭 효과음
                StartActivity.soundManager.playSound(SoundManager.BTN_CLICK2); // 버튼 클릭 효과음
            }

            switch(view.getId()){
                case R.id.btn_start:
                    if(pref.getBoolean("tuto", false)) {
                        i = new Intent(SelectPageActivity.this,SelectLevel.class);
                        startActivity(i);
                        finish();
                    } else {
                        i = new Intent(SelectPageActivity.this,TutorialActivity.class);
                        startActivity(i);
                        finish();
                    }
                    break;
                case R.id.btn_tuto:
                    i = new Intent(SelectPageActivity.this,TutorialActivity.class);
                    startActivity(i);
                    break;
                case R.id.btn_rank:
                    i = new Intent(SelectPageActivity.this,RankActivity.class);
                    startActivity(i);
                    finish();
                    break;
                case R.id.btn_shop:
                    i = new Intent(SelectPageActivity.this, ShopActivity.class);
                    startActivity(i);
                    finish();
                    break;
                case R.id.btn_finish:
                    moveTaskToBack(true);
                    finishAndRemoveTask();
                    android.os.Process.killProcess(android.os.Process.myPid());
                    break;
            }
        }
    };

    View.OnClickListener icon_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (SoundManager.effectSound == SoundManager.EFFECT_ON){
                StartActivity.soundManager.playSound(SoundManager.BTN_CLICK); // 버튼 클릭 효과음
                StartActivity.soundManager.playSound(SoundManager.BTN_CLICK2); // 버튼 클릭 효과음
            }

            if (SoundManager.effectSound == SoundManager.EFFECT_ON){
                StartActivity.soundManager.playSound(SoundManager.BTN_CLICK); // 버튼 클릭 효과음
                StartActivity.soundManager.playSound(SoundManager.BTN_CLICK2); // 버튼 클릭 효과음
            }

            dialog = new Dialog(SelectPageActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_sound);

            btn_ok = dialog.findViewById(R.id.btn_ok);

            img_sound_on = dialog.findViewById(R.id.img_sound_on);
            img_sound_off = dialog.findViewById(R.id.img_sound_off);
            img_bgm_on = dialog.findViewById(R.id.img_bgm_on);
            img_bgm_off = dialog.findViewById(R.id.img_bgm_off);

            // 마지막 저장상태에 따른 이미지
            if (BgmManager.bgmSound == BgmManager.BGM_ON){
                img_bgm_on.setVisibility(View.VISIBLE);
                img_bgm_off.setVisibility(View.INVISIBLE);
            }else{
                img_bgm_off.setVisibility(View.VISIBLE);
                img_bgm_on.setVisibility(View.INVISIBLE);
            }

            if (SoundManager.effectSound == SoundManager.EFFECT_ON){
                img_sound_on.setVisibility(View.VISIBLE);
                img_sound_off.setVisibility(View.INVISIBLE);
            }else{
                img_sound_off.setVisibility(View.VISIBLE);
                img_sound_on.setVisibility(View.INVISIBLE);
            }

            // 효과음 음소거
            img_sound_on.setOnClickListener(soundClick);
            img_sound_off.setOnClickListener(soundClick);

            // BGM 음소거
            img_bgm_on.setOnClickListener(bgmClick);
            img_bgm_off.setOnClickListener(bgmClick);

            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    };

    // 효과음 아이콘 클릭시
    View.OnClickListener soundClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            SharedPreferences.Editor edit = pref_sound.edit();

            switch (view.getId()){
                case R.id.img_sound_on:

                    if( SoundManager.effectSound == SoundManager.EFFECT_ON ) {
                        SoundManager.effectSound = SoundManager.EFFECT_OFF;
                    }

                    img_sound_on.setVisibility(View.INVISIBLE);
                    img_sound_off.setVisibility(View.VISIBLE);
                    break;

                case R.id.img_sound_off:

                    if (SoundManager.effectSound == SoundManager.EFFECT_OFF){
                        SoundManager.effectSound = SoundManager.EFFECT_ON;
                    }

                    StartActivity.soundManager.setVolume();
                    img_sound_on.setVisibility(View.VISIBLE);
                    img_sound_off.setVisibility(View.INVISIBLE);
                    break;
            }
            edit.putInt("sound_status", SoundManager.effectSound);
            edit.commit();
        }
    };

    // BGM 아이콘 클릭시
    View.OnClickListener bgmClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            SharedPreferences.Editor edit = pref_sound.edit();

            switch (view.getId()){

                case R.id.img_bgm_on:
                    BgmManager.bgmSound = BgmManager.BGM_OFF;
                    StartActivity.bgmManager.bgmPause();
                    BgmManager.isPlaying = false;

                    img_bgm_on.setVisibility(View.INVISIBLE);
                    img_bgm_off.setVisibility(View.VISIBLE);
                    break;

                case R.id.img_bgm_off:
                    BgmManager.bgmSound = BgmManager.BGM_ON;
                    StartActivity.bgmManager.bgmStart(R.raw.bgm_main);
                    BgmManager.isPlaying = true;

                    img_bgm_on.setVisibility(View.VISIBLE);
                    img_bgm_off.setVisibility(View.INVISIBLE);
                    break;

            }
            edit.putInt("bgm_status", BgmManager.bgmSound);
            edit.commit();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        StartActivity.bgmManager.bgmPause();
        BgmManager.isPlaying = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("bgmSound", ""+BgmManager.bgmSound);
        if (BgmManager.bgmSound == BgmManager.BGM_ON){
            StartActivity.bgmManager.bgmStart(R.raw.bgm_main);
            BgmManager.isPlaying = true;
        } else if(BgmManager.bgmSound == BgmManager.BGM_OFF){
            StartActivity.bgmManager.bgmStart(R.raw.bgm_main);
            StartActivity.bgmManager.bgmPause();
            BgmManager.isPlaying = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BgmManager.pausePosition = 0;
    }

}