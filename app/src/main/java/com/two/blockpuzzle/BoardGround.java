package com.two.blockpuzzle;

public class BoardGround {
    public int [][] mBoard = null;

    //배경을 그린다.
    public BoardGround() {
        mBoard = new int[PublicDefine.MATRIX_BR_X][PublicDefine.MATRIX_BR_Y];

        for(int i = 0; i < PublicDefine.MATRIX_BR_X; i++)
            for(int j = 0; j < PublicDefine.MATRIX_BR_Y; j++) {
                if((i < PublicDefine.MATRIX_BETWEEN) || (i >= (PublicDefine.MATRIX_X + PublicDefine.MATRIX_BETWEEN)) ||
                        (j < PublicDefine.MATRIX_BETWEEN ) || (j >= (PublicDefine.MATRIX_Y + PublicDefine.MATRIX_BETWEEN)) ) {
                    mBoard[i][j] = PublicDefine.PIECE_NO;
                } else
                    mBoard[i][j] = PublicDefine.PIECE_BL;
            }
    }
}
