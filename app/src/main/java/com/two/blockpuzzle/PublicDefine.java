package com.two.blockpuzzle;

public class PublicDefine {
    //게임 점수
    public static int easyscore = -15; //5 + 10
    public static int normalscore = -30; //10 + 20
    public static int hardscore = -45; //15 + 30

    //난이도 저장
    public static String gameLevel = "A";

    //현재 블록 / 다음 블록
    public static int[][] currentPiece = null;
    public static int[][] nextPiece = null;

    //블록 크기
    public static final int PIECE_SIZE = 4;

    //블록 색
    public static final int PIECE_NO = -1; //NONE
    public static final int PIECE_BL = 0;  //BLACK
    public static final int PIECE_RD = 1;  //RED
    public static final int PIECE_OR = 2;  //ORANGE
    public static final int PIECE_YW = 3;  //YELLOW
    public static final int PIECE_GR = 4;  //GREEN
    public static final int PIECE_SB = 5;  //SKYBLUE
    public static final int PIECE_BU = 6;  //BLUE
    public static final int PIECE_PP = 7;  //PURPLE

    //배경 크기
    //상하 X축 / 좌우 Y축(X, Y)
    public static final int MATRIX_X = 23;
    public static final int MATRIX_Y = 12;

    public static final int MATRIX_BETWEEN = PIECE_SIZE - 1; //3 = 4 -1
    private static final int MATRIX_GAP = MATRIX_BETWEEN * 2; //6 = 3 * 2

    public static final int MATRIX_BR_X = MATRIX_X + MATRIX_GAP; //29 = 23 + 6
    public static final int MATRIX_BR_Y = MATRIX_Y + MATRIX_GAP; //18 = 12 + 6

    public static final int MATRIX_WORK_X = MATRIX_BR_X - MATRIX_BETWEEN; //26 = 29 - 3
    public static final int MATRIX_WORK_Y = MATRIX_BR_Y - MATRIX_BETWEEN; //15 = 18 - 3

}
