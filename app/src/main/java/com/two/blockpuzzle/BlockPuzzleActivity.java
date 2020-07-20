package com.two.blockpuzzle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

public class BlockPuzzleActivity extends Activity {

    public FrameUi mFrameUi;
    public FrameLayout mNextPieceUi;
    public LinearLayout linear_gamebg;

    public TextView game_tv_level, game_tv_score;
    public Button game_btn_settings, game_btn_item1, game_btn_item2, game_btn_item3;
    public Button game_tv_item1_num, game_tv_item2_num, game_tv_item3_num; //TextView처럼 쓰임

    KakaoShare kakaoShare;
    MediaPlayer mediaPlayer3;

    SweetAlertDialog sweetAlertDialog;
    SharedPreferences pref, pref_score_easy, pref_score_normal, pref_score_hard, pref_sound;
    boolean clickSetting = false;

    private PlayGround mPlayGround = null;
    private MotionEventAction mMotionAction = null;

    ImageView game_char;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_puzzle);

        kakaoShare = new KakaoShare(this);

        // BGM & 효과음 저장 상태 가져오기
        pref_sound = getSharedPreferences("SOUND", MODE_PRIVATE);
        SoundManager.effectSound = pref_sound.getInt("sound_status", 0);
        BgmManager.bgmSound = pref_sound.getInt("bgm_status", 0);

        // BGM 세팅
        mediaPlayer3 = MediaPlayer.create(this, R.raw.bgm_play);
        mediaPlayer3.setLooping(true);

        mFrameUi = findViewById(R.id.imgFrameUi);
        mNextPieceUi = findViewById(R.id.nextPieceUi);
        NextPieceUi npu = new NextPieceUi(this);
        mNextPieceUi.addView( npu );
        linear_gamebg = findViewById(R.id.linear_gamebg);

        game_tv_level = findViewById(R.id.game_tv_level);
        game_tv_score = findViewById(R.id.game_tv_score);
        game_tv_item1_num = findViewById(R.id.game_tv_item1_num);
        game_tv_item2_num = findViewById(R.id.game_tv_item2_num);
        game_tv_item3_num = findViewById(R.id.game_tv_item3_num);
        game_btn_settings = findViewById(R.id.game_btn_settings);
        game_btn_item1 = findViewById(R.id.game_btn_item1);
        game_btn_item2 = findViewById(R.id.game_btn_item2);
        game_btn_item3 = findViewById(R.id.game_btn_item3);
        game_char = findViewById(R.id.game_char);

        pref = getSharedPreferences("SHARE", MODE_PRIVATE);
        game_tv_item1_num.setText("" + pref.getInt("bomb", 0));
        game_tv_item2_num.setText("" + pref.getInt("speed", 0));
        game_tv_item3_num.setText("" + pref.getInt("all", 0));

        mMotionAction = new MotionEventAction();
        mPlayGround = new PlayGround(mFrameUi, mNextPieceUi, pref_sound);
        mPlayGround.setActivity(this);
        mPlayGround.setGame_tv_score(game_tv_score); //PlayGround 클래스로 점수 TextView 보냄

        if(PublicDefine.gameLevel.equals("easy")) {
            game_tv_level.setText("EASY");
            linear_gamebg.setBackground(ContextCompat.getDrawable(this, R.drawable.gamebg_easy));
        } else if(PublicDefine.gameLevel.equals("normal")) {
            game_tv_level.setText("NORMAL");
            linear_gamebg.setBackground(ContextCompat.getDrawable(this, R.drawable.gamebg_normal));
        } else if(PublicDefine.gameLevel.equals("hard")) {
            game_tv_level.setText("HARD");
            linear_gamebg.setBackground(ContextCompat.getDrawable(this, R.drawable.gamebg_hard));
        }

        if( savedInstanceState == null ) {
            //게임 시작
            mPlayGround.PlayStart();
            mPlayGround.PlayGoGoGo();
        }

        //캐릭터 체인지
        if(pref.getInt("invenCh",1)==1){
            game_char.setImageResource(R.drawable.wk);
        }
        if(pref.getInt("invenCh",1)==2){
            game_char.setImageResource(R.drawable.mr);
        }
        if(pref.getInt("invenCh",1)==3){
            game_char.setImageResource(R.drawable.sj);
        }
        if(pref.getInt("invenCh",1)==4){
            game_char.setImageResource(R.drawable.sy);
        }

        //일시정지 버튼 눌렀을 때
        game_btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStop();
                createSweetAlertDialog();
                clickSetting = true;
            }
        });

        //게임 아이템 버튼 눌렀을 때
        game_btn_item1.setOnClickListener(gameItemClick);
        game_tv_item1_num.setOnClickListener(gameItemClick);
        game_btn_item2.setOnClickListener(gameItemClick);
        game_tv_item2_num.setOnClickListener(gameItemClick);
        game_btn_item3.setOnClickListener(gameItemClick);
        game_tv_item3_num.setOnClickListener(gameItemClick);
    }//onCreate()

    //게임 아이템 버튼 이벤트
    View.OnClickListener gameItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences.Editor edit = pref.edit();

            switch(v.getId()) {
                case R.id.game_btn_item1:
                case R.id.game_tv_item1_num:
                    if(pref.getInt("bomb", 0) > 0) {
                        mPlayGround.click3LineDeleteItem();
                        int bomb = pref.getInt("bomb", 0) - 1;
                        edit.putInt("bomb", bomb);
                        game_tv_item1_num.setText("" + pref.getInt("bomb", 0));
                    } else {
                        Toast.makeText(getApplicationContext(), "폭탄 없쪙", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.game_btn_item2:
                case R.id.game_tv_item2_num:
                    if(pref.getInt("speed", 0) > 0) {
                        mPlayGround.clickSpeedDownItem();
                        int speed = pref.getInt("speed", 0) - 1;
                        edit.putInt("speed", speed);
                        game_tv_item2_num.setText("" + pref.getInt("speed", 0));
                    } else {
                        Toast.makeText(getApplicationContext(), "스피드 없쪙", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.game_btn_item3:
                case R.id.game_tv_item3_num:
                    if(pref.getInt("all", 0) > 0) {
                        mPlayGround.clickAllDeleteItem();
                        int all = pref.getInt("all", 0) - 1;
                        edit.putInt("all", all);
                        game_tv_item3_num.setText("" + pref.getInt("all", 0));
                    } else {
                        Toast.makeText(getApplicationContext(), "다 지우기 없쪙", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }//switch

            edit.commit();
        }
    };

    //일시정지 다이얼로그
    public void createSweetAlertDialog() {
        mPlayGround.PlayStop();

        sweetAlertDialog = new SweetAlertDialog(BlockPuzzleActivity.this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText("PAUSE");
        sweetAlertDialog.setCancelText("EXIT");
        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                //메인 화면으로 돌아가야지.. ^^ 나중에 코드 추가
                createExitAlertDialog();
                bestScore();
                //일시정지 다이얼로그가 꺼져 있음
                clickSetting = false;
            }
        });
        sweetAlertDialog.setConfirmText("BACK");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                mPlayGround.PlayRestart();
                clickSetting = false;
            }
        });
        sweetAlertDialog.show();
    }

    //이전 기록과 비교하는 메서드. 더 높으면 기록 경신 *^^*
    protected void bestScore() {
        //Easy Mode
        if(PublicDefine.gameLevel.equals("easy")) {
            pref_score_easy = getSharedPreferences("easyscore", MODE_PRIVATE);
            SharedPreferences.Editor edit = pref_score_easy.edit();

            if(PublicDefine.easyscore > pref_score_easy.getInt("easyscore", 0)) {
                Log.i("MY", "Easy Mode Best score : " + PublicDefine.easyscore + "!!");
                //기록 경신 시 다이얼로그 띄우기
                sweetAlertDialog = new SweetAlertDialog(BlockPuzzleActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("Easy Mode Best Score!\n" + PublicDefine.easyscore);
                sweetAlertDialog.setConfirmText("OK");
                sweetAlertDialog.setCancelText("SHARE");

                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });

                sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        kakaoShare.share_kakao(pref_score_easy.getInt("easyscore", 0));
                    }
                });
                sweetAlertDialog.show();
                edit.putInt("easyscore", PublicDefine.easyscore);
                edit.commit();
            }
        }

        //Normal Mode
        if(PublicDefine.gameLevel.equals("normal")) {
            pref_score_normal = getSharedPreferences("normalscore", MODE_PRIVATE);
            SharedPreferences.Editor edit = pref_score_normal.edit();

            if(PublicDefine.normalscore > pref_score_normal.getInt("normalscore", 0)) {
                Log.i("MY", "Normal Mode Best score : " + PublicDefine.normalscore + "!!");
                //기록 경신 시 다이얼로그 띄우기
                sweetAlertDialog = new SweetAlertDialog(BlockPuzzleActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("Normal Mode Best Score!\n" + PublicDefine.normalscore);
                sweetAlertDialog.setConfirmText("OK");
                sweetAlertDialog.setCancelText("SHARE");

                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });

                sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        kakaoShare.share_kakao(pref_score_normal.getInt("normalscore", 0));
                    }
                });
                sweetAlertDialog.show();
                edit.putInt("normalscore", PublicDefine.normalscore);
                edit.commit();
            }
        }

        //Hard Mode
        if(PublicDefine.gameLevel.equals("hard")) {
            pref_score_hard = getSharedPreferences("hardscore", MODE_PRIVATE);
            SharedPreferences.Editor edit = pref_score_hard.edit();

            if(PublicDefine.hardscore > pref_score_hard.getInt("hardscore", 0)) {
                Log.i("MY", "Hard Mode Best score : " + PublicDefine.hardscore + "!!");
                //기록 경신 시 다이얼로그 띄우기
                sweetAlertDialog = new SweetAlertDialog(BlockPuzzleActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("Hard Mode Best Score!\n" + PublicDefine.hardscore);
                sweetAlertDialog.setConfirmText("OK");
                sweetAlertDialog.setCancelText("SHARE");

                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });

                sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        kakaoShare.share_kakao(pref_score_hard.getInt("hardscore", 0));
                    }
                });
                sweetAlertDialog.show();
                edit.putInt("hardscore", PublicDefine.hardscore);
                edit.commit();
            }
        }
    }

    //게임 종료 다이얼로그
    public void createExitAlertDialog() {
        sweetAlertDialog = new SweetAlertDialog(BlockPuzzleActivity.this, SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText("GAME OVER");
        sweetAlertDialog.setCancelText("EXIT");
        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                //메인 화면으로 돌아가야지.. ^^ 나중에 코드 추가
                //메인 화면으로 돌아가는 코드 자리
                Intent i = new Intent(BlockPuzzleActivity.this, SelectPageActivity.class);
                startActivity(i);
                finish();

                //점수 초기화
                PublicDefine.easyscore = -15;
                PublicDefine.normalscore = -30;
                PublicDefine.hardscore = -45;

                //점수 코인으로 환산
                pref = getSharedPreferences("SHARE", MODE_PRIVATE);//그냥 모드는 다 PRIVATE쓴다 생각하면 된다
                SharedPreferences.Editor edit = pref.edit();//저장을 위한 에디터의 생성

                int coin = pref.getInt("my_coin", 0) + Integer.parseInt((String)game_tv_score.getText());
                edit.putInt("my_coin", coin);
                edit.commit();
                //점수 초기화
                game_tv_score.setText("0");
            }
        });
        sweetAlertDialog.setConfirmText("REPLAY");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                //난이도 선택 창으로 넘어가야 해서 나중에 수정함
                Intent i = new Intent(BlockPuzzleActivity.this, SelectLevel.class);
                startActivity(i);
                finish();

                //점수 초기화
                PublicDefine.easyscore = -15;
                PublicDefine.normalscore = -30;
                PublicDefine.hardscore = -45;

                //점수 코인으로 환산
                pref = getSharedPreferences("SHARE", MODE_PRIVATE);//그냥 모드는 다 PRIVATE쓴다 생각하면 된다
                SharedPreferences.Editor edit = pref.edit();//저장을 위한 에디터의 생성

                int coin = pref.getInt("my_coin", 0) + Integer.parseInt((String)game_tv_score.getText());
                edit.putInt("my_coin", coin);
                edit.commit();
                //점수 초기화
                game_tv_score.setText("0");
            }
        });
        sweetAlertDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer3.pause();
        BgmManager.isPlaying = false;
    }

    @Override
    protected void onRestart() {
        if(!clickSetting) {
            createSweetAlertDialog();
        }
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("bgmSound", ""+BgmManager.bgmSound);
        if (BgmManager.bgmSound == BgmManager.BGM_ON){
            mediaPlayer3.start();
            BgmManager.isPlaying = true;
        } else if(BgmManager.bgmSound == BgmManager.BGM_OFF){
            mediaPlayer3.pause();
            BgmManager.isPlaying = false;
        }
    }

    @Override
    protected void onStop() {
        mPlayGround.PlayStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BgmManager.pausePosition = 0;
    }

    @Override
    public void onBackPressed() {
        //일시정지 다이얼로그
        createSweetAlertDialog();
    }

    private class MotionEventAction {
        private float mDownX = -1;
        private float mDownY = -1;
        private float mUpX = -1;
        private float mUpY = -1;

        public float IgnoreValue = 10;

        public MotionEventAction() {
            // TODO Auto-generated constructor stub
        }

        protected void ActionLeftMoveEvent(float fGapValue) {
            mPlayGround.ActionLeftMoveEvent(fGapValue);
        }

        protected void ActionRightMoveEvent(float fGapValue) {
            mPlayGround.ActionRightMoveEvent(fGapValue);
        }

        protected void ActionUpMoveEvent(float fGapValue) {
            mPlayGround.ActionUpMoveEvent(fGapValue);
        }

        protected void ActionDownMoveEvent(float fGapValue) {
            mPlayGround.ActionDownMoveEvent(fGapValue);
        }

        //TouchListener
        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        };

        private void ActionCheck() {
            float GapX = mDownX - mUpX;
            float GapY = mDownY - mUpY;
            float AbsGapX = Math.abs(GapX);
            float AbsGapY = Math.abs(GapY);

            if( (AbsGapX< IgnoreValue) && (AbsGapY<IgnoreValue)) {
                // 이 간격은 무시.
                return;
            }

            if( AbsGapX < AbsGapY ) {
                if( mDownY < mUpY )
                    ActionDownMoveEvent(AbsGapY);
                else
                    ActionUpMoveEvent(AbsGapY);
            } else {
                if( mDownX < mUpX )
                    ActionRightMoveEvent(AbsGapX);
                else
                    ActionLeftMoveEvent(AbsGapX);
            }
        }

        public void ActionMotionEvent( MotionEvent event ) {
            switch( event.getActionMasked() ) {
                case MotionEvent.ACTION_DOWN :
                    mDownX = event.getX();
                    mDownY = event.getY();
                    break;

                case MotionEvent.ACTION_UP :
                    mUpX = event.getX();
                    mUpY = event.getY();

                    ActionCheck();
                    break;
            }//switch
        }
    } // MotionEventAction class end


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mMotionAction.ActionMotionEvent(event);
        return super.onTouchEvent(event);
    }
}