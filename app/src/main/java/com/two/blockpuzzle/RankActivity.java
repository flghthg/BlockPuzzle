package com.two.blockpuzzle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RankActivity extends Activity {

    TextView easyscore, normalscore, hardscore;
    Button btn_home;
    Intent i;
    SharedPreferences pref_gameinfo_easy, pref_gameinfo_normal, pref_gameinfo_hard, pref_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        // BGM & 효과음 저장 상태 가져오기
        pref_sound = getSharedPreferences("SOUND", MODE_PRIVATE);
        SoundManager.effectSound = pref_sound.getInt("sound_status", 0);
        BgmManager.bgmSound = pref_sound.getInt("bgm_status", 0);

        pref_gameinfo_easy = getSharedPreferences("easyscore", MODE_PRIVATE);
        pref_gameinfo_normal = getSharedPreferences("normalscore", MODE_PRIVATE);
        pref_gameinfo_hard = getSharedPreferences("hardscore", MODE_PRIVATE);

        int easy_score = pref_gameinfo_easy.getInt("easyscore",0);
        int normal_score = pref_gameinfo_normal.getInt("normalscore",0);
        int hard_score = pref_gameinfo_hard.getInt("hardscore",0);

        Log.i("SCORE", " : " + normal_score);

        easyscore = findViewById(R.id.easyscore);
        easyscore.setText("" + easy_score);

        normalscore = findViewById(R.id.normalscore);
        normalscore.setText("" + normal_score);

        hardscore = findViewById(R.id.hardscore);
        hardscore.setText("" + hard_score);

        btn_home = findViewById(R.id.btn_home);

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SoundManager.effectSound == SoundManager.EFFECT_ON){
                    StartActivity.soundManager.playSound(SoundManager.BTN_CLICK); // 버튼 클릭 효과음
                    StartActivity.soundManager.playSound(SoundManager.BTN_CLICK2); // 버튼 클릭 효과음
                }

                i = new Intent(RankActivity.this, SelectPageActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

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
        i = new Intent(RankActivity.this, SelectPageActivity.class);
        startActivity(i);
        finish();
    }
}