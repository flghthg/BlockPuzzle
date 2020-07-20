package com.two.blockpuzzle;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Random;

public class PlayGround {
    //액티비티 불러오기
    BlockPuzzleActivity activity;

    public void setActivity(BlockPuzzleActivity activity) {
        this.activity = activity;
    }

    //게임이 중단됐을 때 임시 점수 저장
    private int tempScore = 0;
    private int tempScorenormal = 0;
    private int tempScorehard = 0;

    int count = 0;
    private static final String LOGTAG = "BlockPuzzle";

    //게임 속도
    public int gameSpeed;

    //점수 TextView 불러오기 & 게임 속도 설정
    //기본 점수 0점 시작
    TextView game_tv_score;
    public void setGame_tv_score(TextView game_tv_score) {
        this.game_tv_score = game_tv_score;
        if(PublicDefine.gameLevel.equals("easy")) {
            this.game_tv_score.setText("" + PublicDefine.easyscore);
            gameSpeed = 1000;
        } else if(PublicDefine.gameLevel.equals("normal")) {
            this.game_tv_score.setText("" + PublicDefine.normalscore);
            gameSpeed = 600;
        } else if(PublicDefine.gameLevel.equals("hard")) {
            this.game_tv_score.setText("" + PublicDefine.hardscore);
            gameSpeed = 200;
        }
    }

    // 게임 진행 지연 시간
    private int mDelayTime = gameSpeed;

    //게임 진행/중지 여부
    private boolean mPlayGo = true;

    //배경 정보
    public BoardGround mBoardGround = null;
    public BoardGround mBoardInforGround = null;

    //블록 정보
    private PieceList mPieceList = null;

    //핸들러 같지만 타이머 역할 ^^
    private RefreshHandler mRedrawHandler = null;

    //게임 진행 화면
    private FrameUi mFrameUi = null;
    //다음 블록 화면
    private FrameLayout mNextPieceUi = null;

    //떨어지는 블록
    private PieceItem mPieceItem = null;
    private PieceItem mPieceItem2 = null;

    //떨어지는 블록 위치
    private int mPosX = 0;
    private int mPosY = 0;

    //난수
    private Random rnd = new Random();

    public PlayGround(final FrameUi fu, final FrameLayout npu, SharedPreferences pref_sound) {
        mFrameUi = fu;
        mNextPieceUi = npu;
        mBoardGround = new BoardGround();
        mBoardInforGround = new BoardGround();
        mFrameUi.setBoard(mBoardGround);

        mPieceList = new PieceList();
        mRedrawHandler = new RefreshHandler();
        mRedrawHandler.sleep(mDelayTime);

        // BGM & 효과음 저장 상태 가져오기
        SoundManager.effectSound = pref_sound.getInt("sound_status", 0);
        BgmManager.bgmSound = pref_sound.getInt("bgm_status", 0);
    }

    //블록을 랜덤으로 얻어온다.
    private static final int NONE_INDEX = 0;

    private void SelectRandomItem() {
        mPosX = NONE_INDEX;
        mPosY = PublicDefine.MATRIX_Y / 2 + 1; //처음 블록이 등장할 때 중앙에서 나옴

        //현재 블록과 다음 블록을 정한다.
        if(count == 0) {
            mPieceItem = mPieceList.getRandomItem();
            mPieceItem2 = mPieceList.getRandomItem();

            PublicDefine.currentPiece = mPieceItem.Piece;
            PublicDefine.nextPiece = mPieceItem2.Piece;
        } else {
            PublicDefine.currentPiece = PublicDefine.nextPiece;
            mPieceItem = mPieceItem2;
            mPieceItem2 = mPieceList.getRandomItem();
            PublicDefine.nextPiece = mPieceItem2.Piece;
        }

        for(int i = 0; (i < PublicDefine.PIECE_SIZE) && (mPosX == NONE_INDEX); i++) {
            for(int j = 0; j < PublicDefine.PIECE_SIZE; j++) {
                if(PublicDefine.currentPiece[i][j] != PublicDefine.PIECE_NO) {
                    mPosX = i;
                    break;
                }
            }
        }//for

        //점수 올리기
        PublicDefine.easyscore += 5;
        PublicDefine.normalscore += 10;
        PublicDefine.hardscore += 15;

        if(PublicDefine.gameLevel.equals("easy")) {
            game_tv_score.setText("" + PublicDefine.easyscore);
        } else if(PublicDefine.gameLevel.equals("normal")) {
            game_tv_score.setText("" + PublicDefine.normalscore);
        } else if(PublicDefine.gameLevel.equals("hard")) {
            game_tv_score.setText("" + PublicDefine.hardscore);
        }

        this.mFrameUi.invalidate();
        //this.mNextPieceUi.invalidate();
        count++;
    }

    //한 줄이 다 채워졌다면 삭제한다.
    private void RemoveFullLineBoard() {
        //mBoardGround에서 한 줄 다 채워진 것을 찾는다.
        int iSum = 0;
        int k = PublicDefine.MATRIX_WORK_X;
        int delLine = k;

        for(int i = PublicDefine.MATRIX_WORK_X; i >= PublicDefine.MATRIX_BETWEEN; i--) {
            iSum = 0;
            for(int j = PublicDefine.MATRIX_BETWEEN; j < PublicDefine.MATRIX_WORK_Y; j++)
                //가로로 한 줄씩 따라가면서 블록이 채워져 있지 않으면 0, 채워져 있으면 iSum에 1씩 추가
                iSum += mBoardGround.mBoard[i][j] == PublicDefine.PIECE_BL ? 0 : 1;

            //한 줄이 다 블록으로 채워져 있다면 해당 줄 삭제
            if( iSum == PublicDefine.MATRIX_Y ) {
                if (SoundManager.effectSound == SoundManager.EFFECT_ON){
                    StartActivity.soundManager.playSound(SoundManager.LINE_CLEAR); // 줄 삭제 효과음
                    StartActivity.soundManager.playSound(SoundManager.LINE_CLEAR2);
                }

                delLine--;
                //한 칸 지워질 때마다 점수 올리기
                PublicDefine.easyscore += 10;
                PublicDefine.normalscore += 20;
                PublicDefine.hardscore += 30;

                String str = "" + PublicDefine.easyscore;
                String noramlstr = ""+PublicDefine.normalscore;
                String hardstr = ""+PublicDefine.hardscore;

                if(PublicDefine.gameLevel.equals("easy")) {
                    game_tv_score.setText(str);
                } else if(PublicDefine.gameLevel.equals("normal")) {
                    game_tv_score.setText(noramlstr);
                } else if(PublicDefine.gameLevel.equals("hard")) {
                    game_tv_score.setText(hardstr);
                }

                gameSpeed *= 0.8; //한 줄 없앨 때마다 속도가 조금씩 빨라짐 *^^*
                mDelayTime = gameSpeed;
                continue;
            }
            //이 for문이 있어야 점수와 속도가 잘 돌아감..? 왜지
            for( int j = PublicDefine.MATRIX_BETWEEN; j < PublicDefine.MATRIX_WORK_Y; j++ ) {
                mBoardGround.mBoard[k][j] = mBoardGround.mBoard[i][j];
            }
            k--;
        }

        k = PublicDefine.MATRIX_WORK_X - delLine;

        //이 for문은 없어도 잘 되는데 우선 냅둠
        for(int i = PublicDefine.MATRIX_BETWEEN; i < k; i++) {
            for(int j = PublicDefine.MATRIX_BETWEEN; j < PublicDefine.MATRIX_WORK_Y; j++) {
                mBoardGround.mBoard[i][j] = PublicDefine.PIECE_BL;
            }
        }//for

        for(int i = PublicDefine.MATRIX_BETWEEN; i < PublicDefine.MATRIX_WORK_X; i++) {
            for(int j = PublicDefine.MATRIX_BETWEEN; j < PublicDefine.MATRIX_WORK_Y; j++) {
                mBoardInforGround.mBoard[i][j] = mBoardGround.mBoard[i][j];
            }
        }//for
    }

    protected void CheckEndGame() {
        boolean bCheck = true;
        int i = PublicDefine.MATRIX_BETWEEN ;

        //맨 위 가로줄에 블록이 닿으면 게임 오버
        for(int j = PublicDefine.MATRIX_BETWEEN; j < PublicDefine.MATRIX_WORK_Y && bCheck; j++)
            bCheck = mBoardInforGround.mBoard[i][j] == PublicDefine.PIECE_BL;

        if( !bCheck ) {
            Log.i(LOGTAG, "Game Over");
            PlayStop();
            count = 0;
            tempScore = 0;
            gameSpeed = 1000;

            activity.createExitAlertDialog();
            activity.bestScore();

            if (SoundManager.effectSound == SoundManager.EFFECT_ON){
                StartActivity.soundManager.playSound(SoundManager.GAME_OVER); // 게임 종료 효과음
            }
            PublicDefine.gameLevel = "A";
        }

    }

    public void PlayGoGoGo() {
        SelectRandomItem();
        RemoveFullLineBoard();
        CheckEndGame();
    }

    //게임 시작
    public void PlayStart() {
        //SelectRandomItem();
        mPlayGo = true;

        if(PublicDefine.gameLevel.equals("easy")) {
            gameSpeed = 1000;
        } else if(PublicDefine.gameLevel.equals("normal")) {
            gameSpeed = 600;
        } else if(PublicDefine.gameLevel.equals("hard")) {
            gameSpeed = 200;
        }

        mDelayTime = gameSpeed;
        mRedrawHandler.sleep( mDelayTime );
    }

    //게임 일시정지
    public void PlayStop() {
        mPlayGo = false;
        //임시 저장된 점수 기록
        tempScore = PublicDefine.easyscore;
        tempScorenormal = PublicDefine.normalscore;
        tempScorehard = PublicDefine.hardscore;

        if(PublicDefine.gameLevel.equals("easy")) {
            game_tv_score.setText("" + tempScore);
        } else if(PublicDefine.gameLevel.equals("normal")) {
            game_tv_score.setText("" + tempScorenormal);
        } else if(PublicDefine.gameLevel.equals("hard")) {
            game_tv_score.setText("" + tempScorehard);
        }

        mDelayTime = gameSpeed;
        mRedrawHandler.removeMessages(0);
    }

    //게임 재시작
    public void PlayRestart() {
        mPlayGo = true;

        //임시 저장된 점수 기록
        PublicDefine.easyscore = tempScore;
        PublicDefine.normalscore = tempScorenormal;
        PublicDefine.hardscore = tempScorehard;

        if(PublicDefine.gameLevel.equals("easy")) {
            game_tv_score.setText("" + PublicDefine.easyscore);
        } else if(PublicDefine.gameLevel.equals("normal")) {
            game_tv_score.setText("" + PublicDefine.normalscore);
        } else if(PublicDefine.gameLevel.equals("hard")) {
            game_tv_score.setText("" + PublicDefine.hardscore);
        }

        tempScore = 0;
        tempScorenormal = 0;
        tempScorehard = 0;

        mDelayTime = gameSpeed;
        mRedrawHandler.sleep( mDelayTime );
    }

    //mRedrawHandler
    class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            //반복해서 이벤트가 호출된다.
            Log.i(LOGTAG, "~ Playing the Block Puzzle Game ~" );
            PlayGround.this.update();
        }

        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    };

    private int getMoveGapValue( float fGapValue ) {
        //왼쪽 또는 오른쪽으로 1칸 이동, 많이 끌면 2칸 이동함
        return ( fGapValue < 100 )? 1:2;
    }

    public void ActionLeftMoveEvent(float fGapValue) {
        //왼쪽으로 이동 : Y축을 감소시킨다.
        int iGap = getMoveGapValue(fGapValue);
        for( int i = iGap; i >= 1; i-- )
            if( PossibleMove( mPosX, mPosY-i ) ) {
                MergeBoardPiece( mPosX, mPosY-i );
                break;
            }

        this.mFrameUi.invalidate();
    }

    public void ActionRightMoveEvent(float fGapValue) {
        //오른쪽으로 이동 : Y축을 증가시킨다.
        int iGap = getMoveGapValue(fGapValue);
        for(int i = iGap; i >= 1; i--)
            if( PossibleMove( mPosX, mPosY+i ) ) {
                MergeBoardPiece( mPosX, mPosY+i );
                break;
            }

        this.mFrameUi.invalidate();
    }

    public void ActionUpMoveEvent(float fGapValue) {
        // 기존 보드에 있는 것은 삭제하자.
        ShadowRemove();
        //블록 뒤집기
        mPieceItem.getRotateItem();
        int[][] tmpPiece = PublicDefine.currentPiece;

        if(PossibleMove(mPosX, mPosY)) {
            MergeBoardPiece(mPosX, mPosY);
        } else {
            PossibleRotate(mPosX, mPosY);
            RemoveFullLineBoard();
            CheckEndGame();
        }//if

        this.mFrameUi.invalidate();
    }

    public void ActionDownMoveEvent(float fGapValue) {
        for( int i = mPosX + 1; i < PublicDefine.MATRIX_WORK_X; i++ ) {
            if(!PossibleMove(i, mPosY)) {
                MergeBoardPiece(i-1, mPosY);
                PlayGoGoGo();
                break;
            }
        }
        if (SoundManager.effectSound == SoundManager.EFFECT_ON){
            StartActivity.soundManager.playSound(SoundManager.BLOCK_DOWN); // 블럭 내리는 효과음
            StartActivity.soundManager.playSound(SoundManager.BLOCK_DOWN2); // 블럭 내리는 효과음
        }
    }

    //이동이 가능한지 아닌지 판단. true면 이동 가능
    private boolean PossibleMove(final int x, final int y) {
        boolean Result = true;

        //Piece 색이 지정돼 있는 영역에 Board에 Black이 아닌 다른 색이 들어가 있을 경우
        for(int i = 0; i < PublicDefine.PIECE_SIZE && Result; i++)
            for(int j = 0; j < PublicDefine.PIECE_SIZE && Result; j++)
                if(PublicDefine.currentPiece[i][j] != PublicDefine.PIECE_NO)
                    Result = mBoardInforGround.mBoard[i+x][j+y] == PublicDefine.PIECE_BL;
        return Result;
    }

    //이동이 가능한지 아닌지 판단. true면 이동 가능
    private void PossibleRotate(final int x, final int y) {
        boolean Result = true;
        Log.i("MY", "하하하 : " + x + "/" + y);

        for(int a = 0; a < PublicDefine.MATRIX_BR_X; a++) { //29
            for(int b = 0; b < PublicDefine.MATRIX_BR_Y; b++) { //18
                if(b < PublicDefine.MATRIX_BETWEEN || b >= PublicDefine.MATRIX_Y + PublicDefine.MATRIX_BETWEEN) {
                    if(mBoardGround.mBoard[a][0] != PublicDefine.PIECE_NO) {
                        for(int i = 0; i < PublicDefine.PIECE_SIZE && Result; i++) {
                            for(int j = 0; j < PublicDefine.PIECE_SIZE && Result; j++) {
                                if(PublicDefine.currentPiece[i][j] != PublicDefine.PIECE_NO) {
                                    int temp = 0 - y + 1; //긴 블록 1~13 또는 2~12 / 나머지 2~12

                                    if(y == 2) {
                                        mPosY += temp + 2;
                                    }
                                    if(y == 1) {
                                        mPosY += temp + 1;
                                    }

                                    if (y >= PublicDefine.MATRIX_Y) {
                                        mPosY += temp + 2;
                                    }
                                    if (y >= PublicDefine.MATRIX_Y + 1) {
                                        mPosY += temp + 1;
                                    }
                                }
                            }
                        }//outer

                        //MergeBoardPiece(mPosX, mPosY + 3);
                    } else if(mBoardGround.mBoard[a][1] != PublicDefine.PIECE_NO) {
                        MergeBoardPiece(mPosX, mPosY + 2);
                    } else if(mBoardGround.mBoard[a][2] != PublicDefine.PIECE_NO) {
                        MergeBoardPiece(mPosX, mPosY + 1);
                    }
                    mPieceItem.getRotateItem();
                } else {

                }
            }
        }

        for(int i = 0; i < PublicDefine.PIECE_SIZE && Result; i++) {
            for(int j = 0; j < PublicDefine.PIECE_SIZE && Result; j++) {
                if(PublicDefine.currentPiece[i][j] != PublicDefine.PIECE_NO) {
                    int temp = 0 - y;

                    if (y <= 0) {
                        mPosY += temp;
                    }
                    if (y > PublicDefine.MATRIX_Y) {
                        mPosY += temp;
                    }
                }
            }
        }//outer
    }

    //현재 mPieceItem 영역 Black으로 교체한다.
    private void ShadowRemove() {
        for( int i = 0; i < PublicDefine.PIECE_SIZE ; i++ )
            for( int j = 0; j < PublicDefine.PIECE_SIZE; j++ )
                if( (mBoardGround.mBoard[i+mPosX][j+mPosY] == PublicDefine.currentPiece[i][j]) &&
                        (mBoardGround.mBoard[i+mPosX][j+mPosY] != PublicDefine.PIECE_BL) &&
                        (mBoardGround.mBoard[i+mPosX][j+mPosY] != PublicDefine.PIECE_NO) )
                    mBoardGround.mBoard[i+mPosX][j+mPosY] = PublicDefine.PIECE_BL;
    }

    //Piece가 한 칸씩 내려올 때, 터치로 이동할 때 쓰이는 메서드
    private void MergeBoardPiece(final int newX, final int newY) {
        //기존의 블록 지우기
        ShadowRemove();

        //새로운 위치 잡기
        for(int i = 0; i < PublicDefine.PIECE_SIZE; i++)
            for(int j = 0; j < PublicDefine.PIECE_SIZE; j++)
                if(PublicDefine.currentPiece[i][j] != PublicDefine.PIECE_NO) {
                    mBoardGround.mBoard[i+newX][j+newY] = PublicDefine.currentPiece[i][j];
                } else if(PublicDefine.currentPiece[i][j] == PublicDefine.PIECE_NO && newX <= 0) {
                    mBoardGround.mBoard[i+newX][j+newY] = PublicDefine.currentPiece[i][j];
                }

        mPosX = newX;
        mPosY = newY;
    }

    //한 칸씩 내려온다.
    private void PieceOneStepDown() {
        Log.i(LOGTAG, "Piece one step down~");
        //X축을 증가한다.

        if(PossibleMove( mPosX+1, mPosY ))
            MergeBoardPiece(mPosX+1, mPosY);
        else {
            PlayGoGoGo();

            if (mPosX <= 0) {
                RemoveFullLineBoard();
                CheckEndGame();
            }
        }
    }

    //게임 아이템 사용 시 호출될 메서드
    //아이템 사용으로 줄이 없어질 경우 속도 증가는 하지 않을게염

    //아이템 : 3줄 없애기
    public void click3LineDeleteItem() {
        if (SoundManager.effectSound == SoundManager.EFFECT_ON){
            StartActivity.soundManager.playSound(SoundManager.ITEM_3ROWS_CLEAR); // 3줄 삭제 효과음
            StartActivity.soundManager.playSound(SoundManager.ITEM_3ROWS_CLEAR2); // 3줄 삭제 효과음
        }

        //100점 추가
        if(PublicDefine.gameLevel.equals("easy")) {
            PublicDefine.easyscore += 100;
            game_tv_score.setText("" + PublicDefine.easyscore);
        } else if(PublicDefine.gameLevel.equals("normal")) {
            PublicDefine.normalscore += 100;
            game_tv_score.setText("" + PublicDefine.normalscore);
        } else if(PublicDefine.gameLevel.equals("hard")) {
            PublicDefine.hardscore += 100;
            game_tv_score.setText("" + PublicDefine.hardscore);
        }

        int k = PublicDefine.MATRIX_WORK_X; //= MATRIX_WORK_X;

        for(int i = PublicDefine.MATRIX_WORK_X - 3; i >= PublicDefine.MATRIX_BETWEEN; i--) {
            for( int j = PublicDefine.MATRIX_BETWEEN; j < PublicDefine.MATRIX_WORK_Y; j++ ) {
                mBoardGround.mBoard[k][j] = mBoardGround.mBoard[i][j];
            }
            k--;
        }

        k = PublicDefine.MATRIX_WORK_X - 3;

        for( int i = PublicDefine.MATRIX_BETWEEN; i < k; i++ ) {
            for( int j = PublicDefine.MATRIX_BETWEEN; j < PublicDefine.MATRIX_WORK_Y; j++ ) {
                mBoardGround.mBoard[i][j] = PublicDefine.PIECE_BL;
            }
        }//for

        for( int i = PublicDefine.MATRIX_BETWEEN; i < PublicDefine.MATRIX_WORK_X; i++ ) {
            for( int j = PublicDefine.MATRIX_BETWEEN; j < PublicDefine.MATRIX_WORK_Y; j++ ) {
                //빈 공간은 검은 블록으로 채우기
                if(mBoardGround.mBoard[i][j] == PublicDefine.PIECE_NO) {
                    mBoardGround.mBoard[i][j] = PublicDefine.PIECE_BL;
                }
                mBoardInforGround.mBoard[i][j] = mBoardGround.mBoard[i][j];
            }
        }//for
    }

    //아이템 : 속도 낮추기
    public void clickSpeedDownItem() {
        if (SoundManager.effectSound == SoundManager.EFFECT_ON){
            StartActivity.soundManager.playSound(SoundManager.ITEM_SPEED_DOWN); // 속도 줄이는 효과음
            StartActivity.soundManager.playSound(SoundManager.ITEM_SPEED_DOWN2); // 속도 줄이는 효과음
        }

        //100점 추가
        //속도는 맨 처음 속도로 설정됨
        if(PublicDefine.gameLevel.equals("easy")) {
            PublicDefine.easyscore += 100;
            game_tv_score.setText("" + PublicDefine.easyscore);
            gameSpeed = 1000;
        } else if(PublicDefine.gameLevel.equals("normal")) {
            PublicDefine.normalscore += 100;
            game_tv_score.setText("" + PublicDefine.normalscore);
            gameSpeed = 600;
        } else if(PublicDefine.gameLevel.equals("hard")) {
            PublicDefine.hardscore += 100;
            game_tv_score.setText("" + PublicDefine.hardscore);
            gameSpeed = 200;
        }
        mDelayTime = gameSpeed;
    }

    //아이템 : 모든 블록 없애기
    public void clickAllDeleteItem() {
        if (SoundManager.effectSound == SoundManager.EFFECT_ON){
            StartActivity.soundManager.playSound(SoundManager.ITEM_ALLCLEAR); // 모든 블럭삭제 효과음
            StartActivity.soundManager.playSound(SoundManager.ITEM_ALLCLEAR2); // 모든 블럭삭제 효과음
        }

        //200점 추가
        if(PublicDefine.gameLevel.equals("easy")) {
            PublicDefine.easyscore += 200;
            game_tv_score.setText("" + PublicDefine.easyscore);
        } else if(PublicDefine.gameLevel.equals("normal")) {
            PublicDefine.normalscore += 200;
            game_tv_score.setText("" + PublicDefine.normalscore);
        } else if(PublicDefine.gameLevel.equals("hard")) {
            PublicDefine.hardscore += 200;
            game_tv_score.setText("" + PublicDefine.hardscore);
        }

        //다 지워버려~~
        for( int i = PublicDefine.MATRIX_BETWEEN; i < PublicDefine.MATRIX_WORK_X; i++ ) {
            for( int j = PublicDefine.MATRIX_BETWEEN; j < (PublicDefine.MATRIX_WORK_Y); j++ ) {
                mBoardGround.mBoard[i][j] = PublicDefine.PIECE_BL;
            }
        }//for

        for( int i = PublicDefine.MATRIX_BETWEEN; i < PublicDefine.MATRIX_WORK_X; i++ ) {
            for( int j = PublicDefine.MATRIX_BETWEEN; j < PublicDefine.MATRIX_WORK_Y; j++ ) {
                mBoardInforGround.mBoard[i][j] = mBoardGround.mBoard[i][j];
            }
        }//for
    }

    //핸들러에서 반복 호출
    private void update() {
        this.PieceOneStepDown();
        this.mFrameUi.invalidate();

        if( mPlayGo )
            mRedrawHandler.sleep( mDelayTime );
    }
}
