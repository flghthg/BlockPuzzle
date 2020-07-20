package com.two.blockpuzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TutorialActivity extends Activity {

    CheckBox check;
    SharedPreferences pref, pref_sound;
    Button end_totu;
    TextView tuto_txt1, tuto_txt2, tuto_txt3, tuto_txt4,
            tuto_txt5;

    int TUTOPAGE = 1;
    ImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        // BGM & 효과음 저장 상태 가져오기
        pref_sound = getSharedPreferences("SOUND", MODE_PRIVATE);
        SoundManager.effectSound = pref_sound.getInt("sound_status", 0);
        BgmManager.bgmSound = pref_sound.getInt("bgm_status", 0);

        //튜토리얼 이미지
        background = findViewById(R.id.background);
        //튜토리얼 텍스트
        tuto_txt1 = findViewById(R.id.tuto_txt1);
        tuto_txt2 = findViewById(R.id.tuto_txt2);
        tuto_txt3 = findViewById(R.id.tuto_txt3);
        tuto_txt4 = findViewById(R.id.tuto_txt4);
        tuto_txt5 = findViewById(R.id.tuto_txt5);

        //튜토리얼 넘기고 끝내기 버튼
        end_totu = findViewById(R.id.end_totu);

        pref = getSharedPreferences("SHARE",MODE_PRIVATE);
        check = findViewById(R.id.check);
        pref.getBoolean("totu",false);

        if(TUTOPAGE == 1){
            background.setBackgroundResource(R.drawable.tutorial_1);
            tuto_txt2.setVisibility(View.GONE);
            tuto_txt3.setVisibility(View.GONE);
            tuto_txt4.setVisibility(View.GONE);
            tuto_txt5.setVisibility(View.GONE);
        }
        end_totu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page();

                if(end_totu.getText().equals("튜토리얼 끝내기")){
                    if (SoundManager.effectSound == SoundManager.EFFECT_ON){
                        StartActivity.soundManager.playSound(SoundManager.BTN_CLICK); // 버튼 클릭 효과음
                        StartActivity.soundManager.playSound(SoundManager.BTN_CLICK2); // 버튼 클릭 효과음
                    }

                    boolean myCheck = check.isChecked();
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putBoolean("tuto",myCheck);
                    edit.commit();
                    moveActivity();
                    //Toast.makeText(getApplicationContext(),pref.getBoolean("tuto",false)+"",Toast.LENGTH_SHORT).show();
                }
                if(TUTOPAGE ==5){
                    end_totu.setText("튜토리얼 끝내기");
                }
            }
        });
    }

    //1.메서드생성
    public void moveActivity(){
        Intent i = new Intent( TutorialActivity.this, SelectPageActivity.class );
        startActivity(i);
        finish();//메인에서 뒤로가기 누르면 튜토 안보이고 바로 종료되게 현재 튜토리얼은 그냥 종료
    }

    public void page(){
        TUTOPAGE += 1;
        switch (TUTOPAGE){
            case 1:
                break;
            case 2:
                background.setImageResource(R.drawable.tutorial_2);
                tuto_txt2.setVisibility(View.VISIBLE);
                tuto_txt1.setVisibility(View.GONE);
                tuto_txt3.setVisibility(View.GONE);
                tuto_txt4.setVisibility(View.GONE);
                tuto_txt5.setVisibility(View.GONE);
                break;
            case 3:
                background.setImageResource(R.drawable.tutorial_3);
                tuto_txt3.setVisibility(View.VISIBLE);
                tuto_txt1.setVisibility(View.GONE);
                tuto_txt2.setVisibility(View.GONE);
                tuto_txt4.setVisibility(View.GONE);
                tuto_txt5.setVisibility(View.GONE);
                break;
            case 4:
                background.setImageResource(R.drawable.tutorial_4);
                tuto_txt4.setVisibility(View.VISIBLE);
                tuto_txt1.setVisibility(View.GONE);
                tuto_txt3.setVisibility(View.GONE);
                tuto_txt2.setVisibility(View.GONE);
                tuto_txt5.setVisibility(View.GONE);
                break;
            case 5:
                background.setImageResource(R.drawable.tutorial_1);
                tuto_txt5.setVisibility(View.VISIBLE);
                tuto_txt1.setVisibility(View.GONE);
                tuto_txt2.setVisibility(View.GONE);
                tuto_txt3.setVisibility(View.GONE);
                tuto_txt4.setVisibility(View.GONE);
                break;
        }

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

}