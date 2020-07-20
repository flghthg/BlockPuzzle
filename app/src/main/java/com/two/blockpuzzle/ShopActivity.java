package com.two.blockpuzzle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ShopActivity extends Activity {

    Button ch_1,ch_2,ch_3,ch_4,
            th_1,th_2,th_3,th_4,
            it_1,it_2,it_3,it_4,
            btn_dia1,btn_dia2,
            btn_main, inven;

    TextView tv_1,tv_2,tv_3,tv_4,
            tvi_1,tvi_2,tvi_3,tvi_4,
            tvc_1,tvc_2,tvc_3,tvc_4;

    TextView txt_goods, txt_coin, txt_name;
    Dialog dialog;
    SharedPreferences pref, pref_sound;

    int my_coin = 0;
    int bomb = 0;
    int speed = 0;
    int all = 0;
    int thou = 0;

    boolean theme_1 = false;
    boolean theme_2 = false;
    boolean theme_3 = false;
    boolean theme_4 = false;

    boolean character_1 = false;
    boolean character_2 = false;
    boolean character_3 = false;
    boolean character_4 = false;

    Drawable a1,a2,a3,a4,a5,a6,a7,a8;

    Intent intent;

    MediaPlayer mediaPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        // BGM & 효과음 저장 상태 가져오기
        pref_sound = getSharedPreferences("SOUND", MODE_PRIVATE);
        SoundManager.effectSound = pref_sound.getInt("sound_status", 0);
        BgmManager.bgmSound = pref_sound.getInt("bgm_status", 0);

        mediaPlayer2 = MediaPlayer.create(this, R.raw.bgm_shop);
        mediaPlayer2.setLooping(true);

        //쉐어드프리퍼런스 선언
        pref = getSharedPreferences("SHARE", MODE_PRIVATE);
        my_coin = pref.getInt("my_coin", 12000);
        bomb = pref.getInt("bomb", bomb);
        speed = pref.getInt("speed", speed);
        all = pref.getInt("all", all);
        thou = pref.getInt("thou", thou);
        theme_1 = pref.getBoolean("theme_1", theme_1);
        theme_2 = pref.getBoolean("theme_2", theme_2);
        theme_3 = pref.getBoolean("theme_3", theme_3);
        theme_4 = pref.getBoolean("theme_4", theme_4);
        character_1 = pref.getBoolean("character_1", character_1);
        character_2 = pref.getBoolean("character_2", character_2);
        character_3 = pref.getBoolean("character_3", character_3);
        character_4 = pref.getBoolean("character_4", character_4);

        //다이얼로그 선언
        dialog = new Dialog(ShopActivity.this,R.style.dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.shopdialog);

        btn_dia1 = dialog.findViewById(R.id.btn_dia1);
        btn_dia2 = dialog.findViewById(R.id.btn_dia2);
        txt_goods = dialog.findViewById(R.id.txt_goods);
        txt_coin = findViewById(R.id.txt_coin);
        txt_name = dialog.findViewById(R.id.txt_name);
        inven = findViewById(R.id.inven);

        inven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SoundManager.effectSound == SoundManager.EFFECT_ON){
                    StartActivity.soundManager.playSound(SoundManager.BTN_CLICK); // 버튼 클릭 효과음
                    StartActivity.soundManager.playSound(SoundManager.BTN_CLICK2); // 버튼 클릭 효과음
                }

                /*Toast.makeText(getApplicationContext(),"폭탄"+bomb+"개"+
                        "스피드"+speed+"개"+"전체삭제"+all+"개"+"1000점"+thou+"개",Toast.LENGTH_SHORT).show();*/
                intent = new Intent(ShopActivity.this, InventoryActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //여기서 상점의 돈을 저장함
        //String coin = Integer.toString(my_coin);
        txt_coin.setText("" + my_coin);

        //---------------테마 선언부--------------------
        th_1 = findViewById(R.id.th_1);
        th_2 = findViewById(R.id.th_2);
        th_3 = findViewById(R.id.th_3);
        th_4 = findViewById(R.id.th_4);

        tv_1 = findViewById(R.id.tv_1);
        tv_2 = findViewById(R.id.tv_2);
        tv_3 = findViewById(R.id.tv_3);
        tv_4 = findViewById(R.id.tv_4);

        //--------------아이템 선언부--------------------
        it_1 = findViewById(R.id.it_1);
        it_2 = findViewById(R.id.it_2);
        it_3 = findViewById(R.id.it_3);
        it_4 = findViewById(R.id.it_4);

        tvi_1 = findViewById(R.id.tvi_1);
        tvi_2 = findViewById(R.id.tvi_2);
        tvi_3 = findViewById(R.id.tvi_3);
        tvi_4 = findViewById(R.id.tvi_4);

        //---------------캐릭터 선언부--------------------
        ch_1 = findViewById(R.id.ch_1);
        ch_2 = findViewById(R.id.ch_2);
        ch_3 = findViewById(R.id.ch_3);
        ch_4 = findViewById(R.id.ch_4);

        tvc_1 = findViewById(R.id.tvc_1);
        tvc_2 = findViewById(R.id.tvc_2);
        tvc_3 = findViewById(R.id.tvc_3);
        tvc_4 = findViewById(R.id.tvc_4);

        //메인화면 돌아가기 버튼 선언부
        btn_main = findViewById(R.id.btn_main);
        btn_main.setOnClickListener(main);

        //--------------테마버튼 선언부---------------------
        th_1.setOnClickListener(th_click);
        th_2.setOnClickListener(th_click);
        th_3.setOnClickListener(th_click);
        th_4.setOnClickListener(th_click);

        a5 = ((Button)findViewById(R.id.th_1)).getBackground();
        a6 = ((Button)findViewById(R.id.th_2)).getBackground();
        a7 = ((Button)findViewById(R.id.th_3)).getBackground();
        a8 = ((Button)findViewById(R.id.th_4)).getBackground();

        //--------------아이템버튼 선언부---------------------
        it_1.setOnClickListener(it_click);
        it_2.setOnClickListener(it_click);
        it_3.setOnClickListener(it_click);
        it_4.setOnClickListener(it_click);

        //--------------케릭터버튼 선언부---------------------
        ch_1.setOnClickListener(ch_click);
        ch_2.setOnClickListener(ch_click);
        ch_3.setOnClickListener(ch_click);
        ch_4.setOnClickListener(ch_click);

        a1 = ((Button)findViewById(R.id.ch_1)).getBackground();
        a2 = ((Button)findViewById(R.id.ch_2)).getBackground();
        a3 = ((Button)findViewById(R.id.ch_3)).getBackground();
        a4 = ((Button)findViewById(R.id.ch_4)).getBackground();

        //--------------다이얼로그 버튼 선언부---------------------
        //"예" 버튼
        btn_dia1.setOnClickListener(btn_dia);
        btn_dia2.setOnClickListener(btn_dia);

        if(pref.getBoolean("theme_1",false)==true){
            a5.setAlpha(80);
            th_1.setEnabled(false);
        }else{
            a5.setAlpha(255);
        }
        if(pref.getBoolean("theme_2",false)==true){
            a6.setAlpha(80);
            th_2.setEnabled(false);
        }else{
            a6.setAlpha(255);
        }
        if(pref.getBoolean("theme_3",false)==true){
            a7.setAlpha(80);
            th_3.setEnabled(false);
        }else{
            a7.setAlpha(255);
        }
        if(pref.getBoolean("theme_4",false)==true){
            a8.setAlpha(80);
            th_4.setEnabled(false);
        }else{
            a8.setAlpha(255);
        }

        if(pref.getBoolean("character_1",false)==true){
            a1.setAlpha(80);
            ch_1.setEnabled(false);
        }else{
            a1.setAlpha(255);
        }
        if(pref.getBoolean("character_2",false)==true){
            a2.setAlpha(80);
            ch_2.setEnabled(false);
        }else{
            a2.setAlpha(255);
        }
        if(pref.getBoolean("character_3",false)==true){
            a3.setAlpha(80);
            ch_3.setEnabled(false);
        }else{
            a3.setAlpha(255);
        }
        if(pref.getBoolean("character_4",false)==true){
            a4.setAlpha(80);
            ch_4.setEnabled(false);
        }else{
            a4.setAlpha(255);
        }

    }//onCreate

    //--------------테마 버튼 클릭 선언부---------------------
    View.OnClickListener th_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (SoundManager.effectSound == SoundManager.EFFECT_ON){
                StartActivity.soundManager.playSound(SoundManager.BTN_CLICK); // 버튼 클릭 효과음
                StartActivity.soundManager.playSound(SoundManager.BTN_CLICK2); // 버튼 클릭 효과음
            }

            switch (view.getId()){
                case R.id.th_1:
                    String str = tv_1.getText().toString();
                    txt_goods.setText(str);
                    txt_name.setText("테마 1");
                    dialog.show(); //다이얼로그 노출
                    break;
                case R.id.th_2:
                    String str2 = tv_2.getText().toString();
                    txt_goods.setText(str2);
                    txt_name.setText("테마 2");
                    dialog.show();
                    break;
                case R.id.th_3:
                    String str3 = tv_3.getText().toString();
                    txt_goods.setText(str3);
                    txt_name.setText("테마 3");
                    dialog.show();
                    break;
                case R.id.th_4:
                    String str4 = tv_4.getText().toString();
                    txt_goods.setText(str4);
                    txt_name.setText("테마 4");
                    dialog.show();
                    break;
            }
        }
    };

    //--------------아이템 버튼 클릭 선언부---------------------
    View.OnClickListener it_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (SoundManager.effectSound == SoundManager.EFFECT_ON){
                StartActivity.soundManager.playSound(SoundManager.BTN_CLICK); // 버튼 클릭 효과음
                StartActivity.soundManager.playSound(SoundManager.BTN_CLICK2); // 버튼 클릭 효과음
            }

            switch (view.getId()){
                case R.id.it_1:
                    String str = tvi_1.getText().toString();
                    txt_goods.setText(str);
                    txt_name.setText("폭탄");
                    dialog.show(); //다이얼로그 노출
                    break;
                case R.id.it_2:
                    String str2 = tvi_2.getText().toString();
                    txt_goods.setText(str2);
                    txt_name.setText("스피드 다운");
                    dialog.show(); //다이얼로그 노출
                    break;
                case R.id.it_3:
                    String str3 = tvi_3.getText().toString();
                    txt_goods.setText(str3);
                    txt_name.setText("전체 삭제");
                    dialog.show(); //다이얼로그 노출
                    break;
                case R.id.it_4:
                    String str4 = tvi_4.getText().toString();
                    txt_goods.setText(str4);
                    txt_name.setText("1000점부터 시작");
                    dialog.show(); //다이얼로그 노출
                    break;
            }
        }
    };
    //--------------캐릭터 버튼 클릭 선언부---------------------
    View.OnClickListener ch_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (SoundManager.effectSound == SoundManager.EFFECT_ON){
                StartActivity.soundManager.playSound(SoundManager.BTN_CLICK); // 버튼 클릭 효과음
                StartActivity.soundManager.playSound(SoundManager.BTN_CLICK2); // 버튼 클릭 효과음
            }

            switch (view.getId()){
                case R.id.ch_1:
                    String str = tvc_1.getText().toString();
                    txt_goods.setText(str);
                    txt_name.setText("원경 캐릭터");
                    dialog.show(); //다이얼로그 노출
                    break;
                case R.id.ch_2:
                    String str2 = tvc_2.getText().toString();
                    txt_goods.setText(str2);
                    txt_name.setText("미래 캐릭터");
                    dialog.show(); //다이얼로그 노출
                    break;
                case R.id.ch_3:
                    String str3 = tvc_3.getText().toString();
                    txt_goods.setText(str3);
                    txt_name.setText("승준 캐릭터");
                    dialog.show(); //다이얼로그 노출
                    break;
                case R.id.ch_4:
                    String str4 = tvc_4.getText().toString();
                    txt_goods.setText(str4);
                    txt_name.setText("서영 캐릭터");
                    dialog.show(); //다이얼로그 노출
                    break;
            }
        }
    };
    //---------------메인 돌아가는 버튼 인텐트----------------------------------
    View.OnClickListener main = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (SoundManager.effectSound == SoundManager.EFFECT_ON){
                StartActivity.soundManager.playSound(SoundManager.BTN_CLICK); // 버튼 클릭 효과음
                StartActivity.soundManager.playSound(SoundManager.BTN_CLICK2); // 버튼 클릭 효과음
            }

            intent = new Intent(ShopActivity.this, SelectPageActivity.class);
            startActivity(intent);
            finish();
        }
    };

    //--------------다이얼로그 "예,아니오" 버튼 클릭 선언부---------------------
    View.OnClickListener btn_dia = new View.OnClickListener() {
        @SuppressLint("ResourceAsColor")
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_dia1:
                    if(Integer.parseInt(txt_goods.getText().toString()) > my_coin){
                        Toast.makeText(ShopActivity.this,"코인이 부족합니다.",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else{
                        if (SoundManager.effectSound == SoundManager.EFFECT_ON){
                            StartActivity.soundManager.playSound(SoundManager.BTN_BUY); // 구매 클릭 효과음
                            StartActivity.soundManager.playSound(SoundManager.BTN_BUY2); // 구매 클릭 효과음
                        }

                        my_coin = my_coin - Integer.parseInt(txt_goods.getText().toString());

                        if(txt_name.getText().equals("폭탄")){
                            bomb += 1;
                        }else if(txt_name.getText().equals("스피드 다운")){
                            speed += 1;
                        }else if(txt_name.getText().equals("전체 삭제")){
                            all += 1;
                        }else if(txt_name.getText().equals("1000점부터 시작")){
                            thou += 1;
                        }else if(txt_name.getText().equals("테마 1")){
                            theme_1 = !theme_1;
                            a5.setAlpha(80);
                        }else if(txt_name.getText().equals("테마 2")){
                            theme_2 = !theme_2;
                            a6.setAlpha(80);
                        }else if(txt_name.getText().equals("테마 3")){
                            theme_3 = !theme_3;
                            a7.setAlpha(80);
                        }else if(txt_name.getText().equals("테마 4")){
                            theme_4 = !theme_4;
                            a8.setAlpha(80);
                        }else if(txt_name.getText().equals("원경 캐릭터")) {
                            character_1 = !character_1;

                            a1.setAlpha(80);

                        }else if(txt_name.getText().equals("미래 캐릭터")) {
                            character_2 = !character_2;

                            a2.setAlpha(80);

                        }else if(txt_name.getText().equals("승준 캐릭터")) {
                            character_3 = !character_3;

                            a3.setAlpha(80);

                        }else if(txt_name.getText().equals("서영 캐릭터")) {
                            character_4 = !character_4;
                            a4.setAlpha(80);

                        }
                    }
                    txt_coin.setText("" + my_coin);
                    dialog.dismiss();
                    break;
                case R.id.btn_dia2:
                    dialog.dismiss();
                    break;
            }
        }
    };
    //-------------------쉐어드프리퍼런스 온퍼즈 부분------------------------
    @Override
    protected void onPause() {
        super.onPause();//이거 지우면 100%에러 / 저얼대 지우면 안됨

        mediaPlayer2.pause();
        BgmManager.isPlaying = false;

        //프로그램이 일시정지될 때 현재 n값을 저장
        //onDestroy는 가끔 호출이 안되므로 onPause나 onStop쪽에 안전하게 저장
        //n을 SHARE라고 하는 임시저장소에 저장
        pref = getSharedPreferences("SHARE", MODE_PRIVATE);//그냥 모드는 다 PRIVATE쓴다 생각하면 된다
        SharedPreferences.Editor edit = pref.edit();//저장을 위한 에디터의 생성
        edit.putInt("my_coin", my_coin);//저장할 타입 제한되있음. / Char, Byte같은건 저장못함
        edit.putInt("bomb", bomb);
        edit.putInt("speed", speed);
        edit.putInt("all", all);
        edit.putInt("thou", thou);
        edit.putBoolean("theme_1", theme_1);
        edit.putBoolean("theme_2", theme_2);
        edit.putBoolean("theme_3", theme_3);
        edit.putBoolean("theme_4", theme_4);
        edit.putBoolean("character_1", character_1);
        edit.putBoolean("character_2", character_2);
        edit.putBoolean("character_3", character_3);
        edit.putBoolean("character_4", character_4);
        edit.commit();//n값을 SHARE에 물리적으로 저장하려면 반드시 commit()을 해야 한다.
        //저장된거 다시 불러오는 건 onCreate쪽에 해야 제일 효과적(홈버튼 네모버튼은 어차피 값이 안바뀌니까)
        //다른 activity에서 "my_coin"같이 같은 키값써도 전달된다. 즉, 프로젝트 전체에서 키값이 중복되면 안된다.
        //Intent나 Bundle에 값 안담아도 다른액티비티에 값 넘길 수 있다는 장점있다.
        //onCreate기

        if(pref.getBoolean("theme_1",false) == true){
            a5.setAlpha(80);
        }else{
            a5.setAlpha(255);
        }
        if(pref.getBoolean("theme_2",false) == true){
            a6.setAlpha(80);
        }else{
            a6.setAlpha(255);
        }
        if(pref.getBoolean("theme_3",false) == true){
            a7.setAlpha(80);
        }else{
            a7.setAlpha(255);
        }
        if(pref.getBoolean("theme_4",false) == true){
            a8.setAlpha(80);
        }else{
            a8.setAlpha(255);
        }
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
        intent = new Intent(ShopActivity.this, SelectPageActivity.class);
        startActivity(intent);
        finish();
    }
}