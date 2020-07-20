package com.two.blockpuzzle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

public class SelectLevel extends Activity {

    Button easy_btn, normal_btn, hard_btn;
    SweetAlertDialog sweetAlertDialog;
    Intent i;
    SharedPreferences pref_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_level);

        // BGM & 효과음 저장 상태 가져오기
        pref_sound = getSharedPreferences("SOUND", MODE_PRIVATE);
        SoundManager.effectSound = pref_sound.getInt("sound_status", 0);
        BgmManager.bgmSound = pref_sound.getInt("bgm_status", 0);

        easy_btn = findViewById(R.id.easy_btn);
        normal_btn = findViewById(R.id.normal_btn);
        hard_btn = findViewById(R.id.hard_btn);

        easy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SoundManager.effectSound == SoundManager.EFFECT_ON){
                    StartActivity.soundManager.playSound(SoundManager.BTN_CLICK); // 버튼 클릭 효과음
                    StartActivity.soundManager.playSound(SoundManager.BTN_CLICK2); // 버튼 클릭 효과음
                }

                PublicDefine.gameLevel = "easy";
                newSweetAlertDialog();
            }
        });

        normal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SoundManager.effectSound == SoundManager.EFFECT_ON){
                    StartActivity.soundManager.playSound(SoundManager.BTN_CLICK); // 버튼 클릭 효과음
                    StartActivity.soundManager.playSound(SoundManager.BTN_CLICK2); // 버튼 클릭 효과음
                }

                PublicDefine.gameLevel = "normal";
                newSweetAlertDialog();
            }
        });

        hard_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SoundManager.effectSound == SoundManager.EFFECT_ON){
                    StartActivity.soundManager.playSound(SoundManager.BTN_CLICK); // 버튼 클릭 효과음
                    StartActivity.soundManager.playSound(SoundManager.BTN_CLICK2); // 버튼 클릭 효과음
                }

                PublicDefine.gameLevel = "hard";
                newSweetAlertDialog();
            }
        });
    }//onCreate

    public void newSweetAlertDialog() {
        sweetAlertDialog = new SweetAlertDialog(SelectLevel.this, SweetAlertDialog.SUCCESS_TYPE);

        if(PublicDefine.gameLevel.equals("easy")) {
            sweetAlertDialog.setTitleText("Easy Mode \n ★★☆☆☆");
            sweetAlertDialog.setContentText("You selected Easy Mode.\nIt's Block Puzzle Time!!");
        } else if(PublicDefine.gameLevel.equals("normal")) {
            sweetAlertDialog.setTitleText("Normal Mode \n ★★★☆☆");
            sweetAlertDialog.setContentText("You selected Normal Mode.\nAre you ready?\nIt's Block Puzzle Time!!");
        } else if(PublicDefine.gameLevel.equals("hard")) {
            sweetAlertDialog.setTitleText("Hard Mode \n ★★★★★");
            sweetAlertDialog.setContentText("You selected Hard Mode. 0_0\nIt will be very hard!!\n It's Block Puzzle Time!!");
        }

        sweetAlertDialog.setConfirmText("Start");
        sweetAlertDialog.setCancelText("Cancel");

        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sweetAlertDialog.dismissWithAnimation();
                Intent i = new Intent(SelectLevel.this, BlockPuzzleActivity.class);
                startActivity(i);
                sweetAlertDialog.dismissWithAnimation();

            }
        });
        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
            }
        });
        sweetAlertDialog.show();
    }//newSweetAlertDialog()

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        i = new Intent(SelectLevel.this, SelectPageActivity.class);
        startActivity(i);
        finish();
    }
}