package com.two.blockpuzzle;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.View.VISIBLE;

public class InventoryActivity extends Activity {

    FrameLayout frame;
    Button theme_1,item_1,character_1, btn_1, btn_2, btn_3, btn_4,
            inven_item1, inven_item2, inven_item3, inven_item4,
            back_shop, inven_dia_btn1, inven_dia_btn2;
    View view;
    SharedPreferences pref, pref_sound;
    Drawable a1,a2,a3,a4;
    int invenTh = 0;
    int invenCh = 0;
    Dialog dialog;
    TextView inven_dia_txt1;

    MediaPlayer mediaPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // BGM & 효과음 저장 상태 가져오기
        pref_sound = getSharedPreferences("SOUND", MODE_PRIVATE);
        SoundManager.effectSound = pref_sound.getInt("sound_status", 0);
        BgmManager.bgmSound = pref_sound.getInt("bgm_status", 0);

        mediaPlayer2 = MediaPlayer.create(this, R.raw.bgm_shop);
        mediaPlayer2.setLooping(true);

        pref = getSharedPreferences("SHARE", MODE_PRIVATE);
        invenTh = pref.getInt("invenTh",invenTh);
        invenCh = pref.getInt("invenCh",invenCh);
        frame =  findViewById(R.id.frame);

        LayoutInflater linf = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        linf.inflate(R.layout.inven_frame, frame);

        theme_1 = findViewById(R.id.theme_1);
        item_1 =findViewById(R.id.item_1);
        character_1=findViewById(R.id.character_1);

        //다이얼로그 등록
        dialog = new Dialog(InventoryActivity.this,R.style.dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.invendialog);

        //인벤토리 다이얼로그 등록

        inven_dia_btn1 = dialog.findViewById(R.id.inven_dia_btn1);
        inven_dia_btn2 = dialog.findViewById(R.id.inven_dia_btn2);
        inven_dia_txt1 = dialog.findViewById(R.id.inven_dia_txt1);

        //-------------frame--------------------------------
        inven_item1 = frame.findViewById(R.id.inven_item1);
        inven_item2 = frame.findViewById(R.id.inven_item2);
        inven_item3 = frame.findViewById(R.id.inven_item3);
        inven_item4 = frame.findViewById(R.id.inven_item4);

        //----------처음 상점 세팅하는 부분------------
        btn_1= frame.findViewById(R.id.btn_1);
        if(pref.getBoolean("theme_1",true)==true) {
            btn_1.setBackgroundColor(Color.parseColor("#C2F5FB"));
            a1 = ((Button)findViewById(R.id.btn_1)).getBackground();
            a1.setAlpha(255);
            btn_1.setEnabled(true);
        }else{
            btn_1.setBackgroundColor(Color.parseColor("#C2F5FB"));
            a1 = ((Button)findViewById(R.id.btn_1)).getBackground();
            a1.setAlpha(150);
            btn_1.setEnabled(false);
        }
        btn_2= frame.findViewById(R.id.btn_2);
        if(pref.getBoolean("theme_2",true)==true) {
            btn_2.setBackgroundColor(Color.parseColor("#DFB371"));
            a2 = ((Button)findViewById(R.id.btn_2)).getBackground();
            a2.setAlpha(255);
            btn_2.setEnabled(true);
        }else{
            btn_2.setBackgroundColor(Color.parseColor("#DFB371"));
            a2 = ((Button)findViewById(R.id.btn_2)).getBackground();
            a2.setAlpha(90);
            btn_2.setEnabled(false);
        }
        btn_3= frame.findViewById(R.id.btn_3);
        if(pref.getBoolean("theme_3",true)==true) {
            btn_3.setBackgroundColor(Color.parseColor("#CD95D6"));
            a3 = ((Button)findViewById(R.id.btn_3)).getBackground();
            a3.setAlpha(255);
            btn_3.setEnabled(true);
        }else{
            btn_3.setBackgroundColor(Color.parseColor("#CD95D6"));
            a3 = ((Button)findViewById(R.id.btn_3)).getBackground();
            a3.setAlpha(90);
            btn_3.setEnabled(false);
        }
        btn_4= frame.findViewById(R.id.btn_4);
        if(pref.getBoolean("theme_4",true)==true) {
            btn_4.setBackgroundColor(Color.parseColor("#F16C99"));
            a4 = ((Button)findViewById(R.id.btn_4)).getBackground();
            a4.setAlpha(255);
            btn_4.setEnabled(true);
        }else{
            btn_4.setBackgroundColor(Color.parseColor("#F16C99"));
            a4 = ((Button)findViewById(R.id.btn_4)).getBackground();
            a4.setAlpha(90);
            btn_4.setEnabled(false);
        }
        back_shop = findViewById(R.id.back_shop);


        theme_1.setOnClickListener(click);
        item_1.setOnClickListener(click);
        character_1.setOnClickListener(click);

        btn_1.setOnClickListener(btn_click);
        btn_2.setOnClickListener(btn_click);
        btn_3.setOnClickListener(btn_click);
        btn_4.setOnClickListener(btn_click);

        back_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SoundManager.effectSound == SoundManager.EFFECT_ON){
                    StartActivity.soundManager.playSound(SoundManager.BTN_CLICK); // 버튼 클릭 효과음
                    StartActivity.soundManager.playSound(SoundManager.BTN_CLICK2); // 버튼 클릭 효과음
                }

                Intent i = new Intent(InventoryActivity.this,ShopActivity.class);
                startActivity(i);
                finish();
            }
        });

        if(pref.getInt("invenTh",invenTh)==1){
            btn_1.setText("선택완료");
        }if(pref.getInt("invenTh",invenTh)==2){
            btn_2.setText("선택완료");
        }
        if(pref.getInt("invenTh",invenTh)==3){
            btn_3.setText("선택완료");
        }if(pref.getInt("invenTh",invenTh)==4){
            btn_4.setText("선택완료");
        }

    }

    View.OnClickListener click = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (SoundManager.effectSound == SoundManager.EFFECT_ON){
                StartActivity.soundManager.playSound(SoundManager.BTN_CLICK); // 버튼 클릭 효과음
                StartActivity.soundManager.playSound(SoundManager.BTN_CLICK2); // 버튼 클릭 효과음
            }

            switch (view.getId()){

                case R.id.theme_1:

                    inven_item1.setVisibility(View.GONE);
                    inven_item2.setVisibility(View.GONE);
                    inven_item3.setVisibility(View.GONE);
                    inven_item4.setVisibility(View.GONE);

                    inven_dia_txt1.setText("테마");

                    if(invenTh==1){
                        btn_1.setText("선택완료");
                        btn_2.setText(" ");
                        btn_3.setText(" ");
                        btn_4.setText(" ");
                    }if(invenTh==2){
                    btn_2.setText("선택완료");
                    btn_1.setText(" ");
                    btn_3.setText(" ");
                    btn_4.setText(" ");
                }
                    if(invenTh==3){
                        btn_3.setText("선택완료");
                        btn_2.setText(" ");
                        btn_1.setText(" ");
                        btn_4.setText(" ");
                    }if(invenTh==4){
                    btn_4.setText("선택완료");
                    btn_2.setText(" ");
                    btn_3.setText(" ");
                    btn_1.setText(" ");
                }

                    if(pref.getBoolean("theme_1",true)==true) {

                        btn_1.setBackgroundColor(Color.parseColor("#C2F5FB"));
                        a1 = ((Button)findViewById(R.id.btn_1)).getBackground();
                        a1.setAlpha(255);
                        btn_1.setEnabled(true);
                    }else{
                        btn_1.setBackgroundColor(Color.parseColor("#C2F5FB"));
                        a1 = ((Button)findViewById(R.id.btn_1)).getBackground();
                        a1.setAlpha(150);
                        btn_1.setEnabled(false);
                    }

                    if(pref.getBoolean("theme_2",true)==true) {
                        btn_2.setBackgroundColor(Color.parseColor("#DFB371"));
                        a2 = ((Button)findViewById(R.id.btn_2)).getBackground();
                        a2.setAlpha(255);
                        btn_2.setEnabled(true);
                    }else{
                        btn_2.setBackgroundColor(Color.parseColor("#DFB371"));
                        a2 = ((Button)findViewById(R.id.btn_2)).getBackground();
                        a2.setAlpha(90);
                        btn_2.setEnabled(false);
                    }

                    if(pref.getBoolean("theme_3",true)==true) {
                        btn_3.setBackgroundColor(Color.parseColor("#CD95D6"));
                        a3 = ((Button)findViewById(R.id.btn_3)).getBackground();
                        a3.setAlpha(255);
                        btn_3.setEnabled(true);
                    }else{
                        btn_3.setBackgroundColor(Color.parseColor("#CD95D6"));
                        a3 = ((Button)findViewById(R.id.btn_3)).getBackground();
                        a3.setAlpha(90);
                        btn_3.setEnabled(false);
                    }

                    if(pref.getBoolean("theme_4",true)==true) {
                        btn_4.setBackgroundColor(Color.parseColor("#F16C99"));
                        a4 = ((Button)findViewById(R.id.btn_4)).getBackground();
                        a4.setAlpha(255);
                        btn_4.setEnabled(true);
                    }else{
                        btn_4.setBackgroundColor(Color.parseColor("#F16C99"));
                        a4 = ((Button)findViewById(R.id.btn_4)).getBackground();
                        a4.setAlpha(90);
                        btn_4.setEnabled(false);
                    }
                    break;

                case R.id.character_1:

                    inven_dia_txt1.setText("캐릭터");


                    btn_1.setBackgroundResource(R.drawable.wk);
                    /*
                      if(pref.getInt("invenCh",invenCh)==1){
                            btn_1.setBackgroundResource(R.drawable.wk_select);
                        }
                     */
                    btn_2.setBackgroundResource(R.drawable.mr);
                    /*if(pref.getInt("invenCh",invenCh)==2){
                        btn_2.setBackgroundResource(R.drawable.mr_select);
                    }*/
                    btn_3.setBackgroundResource(R.drawable.sj);
                    /*if(pref.getInt("invenCh",invenCh)==3){
                        btn_3.setBackgroundResource(R.drawable.sj_select);
                    }*/
                    btn_4.setBackgroundResource(R.drawable.sy);
                    /*if(pref.getInt("invenCh",invenCh)==4){
                        btn_4.setBackgroundResource(R.drawable.sy_select);
                    }*/


                    inven_item1.setVisibility(View.GONE);
                    inven_item2.setVisibility(View.GONE);
                    inven_item3.setVisibility(View.GONE);
                    inven_item4.setVisibility(View.GONE);

                    btn_1.setText(" ");
                    btn_2.setText(" ");
                    btn_3.setText(" ");
                    btn_4.setText(" ");

                    if(pref.getBoolean("character_1",true)==true) {

                        a1 = ((Button)findViewById(R.id.btn_1)).getBackground();
                        a1.setAlpha(255);
                        btn_1.setEnabled(true);
                        if(invenCh==1){
                            btn_1.setBackgroundResource(R.drawable.wk_select);
                        }
                    }else if(pref.getBoolean("character_1",true)==false){
                        a1 = ((Button)findViewById(R.id.btn_1)).getBackground();
                        a1.setAlpha(150);
                        btn_1.setText(" ");
                        btn_1.setEnabled(false);
                    }

                    if(pref.getBoolean("character_2",true)==true) {

                        a2 = ((Button)findViewById(R.id.btn_2)).getBackground();
                        a2.setAlpha(255);
                        btn_2.setEnabled(true);
                        if(invenCh==2){
                            btn_2.setBackgroundResource(R.drawable.mr_select);
                        }
                    }else if(pref.getBoolean("character_2",true)==false){
                        a2 = ((Button)findViewById(R.id.btn_2)).getBackground();
                        a2.setAlpha(150);
                        btn_2.setText(" ");
                        btn_2.setEnabled(false);
                    }

                    if(pref.getBoolean("character_3",true)==true) {

                        a3 = ((Button)findViewById(R.id.btn_3)).getBackground();
                        a3.setAlpha(255);
                        btn_3.setEnabled(true);
                        if(invenCh==3){
                            btn_3.setBackgroundResource(R.drawable.sj_select);
                        }
                    }else if(pref.getBoolean("character_3",true)==false){
                        a3 = ((Button)findViewById(R.id.btn_3)).getBackground();
                        a3.setAlpha(150);
                        btn_3.setText(" ");
                        btn_3.setEnabled(false);
                    }

                    if(pref.getBoolean("character_4",true)==true) {

                        a4 = ((Button)findViewById(R.id.btn_4)).getBackground();
                        a4.setAlpha(255);
                        btn_4.setEnabled(true);
                        if(invenCh==4){
                            btn_4.setBackgroundResource(R.drawable.sy_select);
                        }
                    }else if(pref.getBoolean("character_4",true)==false){
                        a4 = ((Button)findViewById(R.id.btn_4)).getBackground();
                        a4.setAlpha(150);
                        btn_4.setText(" ");
                        btn_4.setEnabled(false);
                    }


                    break;
                case R.id.item_1:
                    btn_1.setBackgroundResource(R.drawable.bm);
                    btn_2.setBackgroundResource(R.drawable.sd);
                    btn_3.setBackgroundResource(R.drawable.ad);
                    btn_4.setBackgroundResource(R.drawable.th);

                    btn_1.setText(" ");
                    btn_2.setText(" ");
                    btn_3.setText(" ");
                    btn_4.setText(" ");

                    inven_item1.setVisibility(VISIBLE);
                    inven_item2.setVisibility(VISIBLE);
                    inven_item3.setVisibility(VISIBLE);
                    inven_item4.setVisibility(VISIBLE);

                    inven_item1.setText("" + pref.getInt("bomb",0) + "개");
                    inven_item2.setText("" + pref.getInt("speed",0) + "개");
                    inven_item3.setText("" + pref.getInt("all",0) + "개");
                    inven_item4.setText("" + pref.getInt("thou",0) + "개");
                default:
            }
        }
    };

    View.OnClickListener btn_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (SoundManager.effectSound == SoundManager.EFFECT_ON){
                StartActivity.soundManager.playSound(SoundManager.BTN_CLICK); // 버튼 클릭 효과음
                StartActivity.soundManager.playSound(SoundManager.BTN_CLICK2); // 버튼 클릭 효과음
            }

            switch (view.getId()){
                case R.id.btn_1:
                    if(inven_dia_txt1.getText().equals("테마")){

                        dialog.show();
                        inven_dia_btn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                invenTh=1;
                                btn_1.setText("선택완료");
                                btn_2.setText("");
                                btn_3.setText("");
                                btn_4.setText("");
                                dialog.dismiss();
                            }
                        });
                        inven_dia_btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }else{
                        dialog.show();
                        inven_dia_btn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                invenCh = 1;
                                btn_1.setBackgroundResource(R.drawable.wk_select);
                                btn_2.setBackgroundResource(R.drawable.mr);
                                btn_3.setBackgroundResource(R.drawable.sj);
                                btn_4.setBackgroundResource(R.drawable.sy);

                                dialog.dismiss();
                            }
                        });
                        inven_dia_btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                    }

                    //Toast.makeText(getApplicationContext(),invenCh+"",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_2:
                    if(inven_dia_txt1.getText().equals("테마")) {
                        dialog.show();
                        inven_dia_btn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                invenTh = 2;
                                btn_1.setText("");
                                btn_2.setText("선택완료");
                                btn_3.setText("");
                                btn_4.setText("");
                                dialog.dismiss();
                            }
                        });
                        inven_dia_btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }else{
                        dialog.show();
                        inven_dia_btn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                invenCh=2;
                                btn_1.setBackgroundResource(R.drawable.wk);
                                btn_2.setBackgroundResource(R.drawable.mr_select);
                                btn_3.setBackgroundResource(R.drawable.sj);
                                btn_4.setBackgroundResource(R.drawable.sy);

                                dialog.dismiss();
                            }
                        });
                        inven_dia_btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                    }
                    break;
                case R.id.btn_3:
                    if(inven_dia_txt1.getText().equals("테마")) {
                        dialog.show();
                        inven_dia_btn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                invenTh=3;
                                btn_1.setText("");
                                btn_2.setText("");
                                btn_3.setText("선택완료");
                                btn_4.setText("");
                                dialog.dismiss();
                            }
                        });
                        inven_dia_btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }else {
                        dialog.show();
                        inven_dia_btn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                invenCh = 3;
                                btn_1.setBackgroundResource(R.drawable.wk);
                                btn_2.setBackgroundResource(R.drawable.mr);
                                btn_3.setBackgroundResource(R.drawable.sj_select);
                                btn_4.setBackgroundResource(R.drawable.sy);

                                dialog.dismiss();
                            }
                        });
                        inven_dia_btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }

                    break;
                case R.id.btn_4:
                    if(inven_dia_txt1.getText().equals("테마")) {
                        dialog.show();
                        inven_dia_btn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                invenTh = 4;
                                btn_1.setText("");
                                btn_2.setText("");
                                btn_3.setText("");
                                btn_4.setText("선택완료");
                                dialog.dismiss();
                            }
                        });
                        inven_dia_btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }else {
                        dialog.show();
                        inven_dia_btn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                invenCh = 4;
                                btn_1.setBackgroundResource(R.drawable.wk);
                                btn_2.setBackgroundResource(R.drawable.mr);
                                btn_3.setBackgroundResource(R.drawable.sj);
                                btn_4.setBackgroundResource(R.drawable.sy_select);

                                dialog.dismiss();
                            }
                        });
                        inven_dia_btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }

                    break;
            }

        }
    };

    @Override
    protected void onPause() {

        mediaPlayer2.pause();
        BgmManager.isPlaying = false;

        super.onPause();//이거 지우면 100%에러 / 저얼대 지우면 안됨
        //프로그램이 일시정지될 때 현재 n값을 저장
        //onDestroy는 가끔 호출이 안되므로 onPause나 onStop쪽에 안전하게 저장
        //n을 SHARE라고 하는 임시저장소에 저장
        pref = getSharedPreferences("SHARE", MODE_PRIVATE);//그냥 모드는 다 PRIVATE쓴다 생각하면 된다
        SharedPreferences.Editor edit = pref.edit();//저장을 위한 에디터의 생성
        edit.putInt("invenTh",invenTh);
        edit.putInt("invenCh",invenCh);
        //저장할 타입 제한되있음. / Char, Byte같은건 저장못함
        edit.commit();//n값을 SHARE에 물리적으로 저장하려면 반드시 commit()을 해야 한다.
        //저장된거 다시 불러오는 건 onCreate쪽에 해야 제일 효과적(홈버튼 네모버튼은 어차피 값이 안바뀌니까)
        //다른 activity에서 "my_n"같이 같은 키값써도 전달된다. 즉, 프로젝트 전체에서 키값이 중복되면 안된다.
        //Intent나 Bundle에 값 안담아도 다른액티비티에 값 넘길 수 있다는 장점있다.
        //onCreate기
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("bgmSound", ""+BgmManager.bgmSound);
        if (BgmManager.bgmSound == BgmManager.BGM_ON){
            mediaPlayer2.start();
            BgmManager.isPlaying = true;
        } else if(BgmManager.bgmSound == BgmManager.BGM_OFF){
            mediaPlayer2.pause();
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
        Intent i = new Intent(InventoryActivity.this, ShopActivity.class);
        startActivity(i);
        finish();
    }
}