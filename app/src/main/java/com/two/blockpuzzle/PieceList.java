package com.two.blockpuzzle;

import java.util.ArrayList;
import java.util.Random;

public class PieceList {

    private Random rnd = null;
    private ArrayList<PieceItem> PieceItems = null;

    //빨간색 ㅣ
    private final int[][] Piece01 = {
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO},
            {PublicDefine.PIECE_RD,PublicDefine.PIECE_RD,PublicDefine.PIECE_RD,PublicDefine.PIECE_RD},
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO},
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO} };

    //주황색 ㅁ
    private final int[][] Piece02 = {
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO},
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_OR,PublicDefine.PIECE_OR,PublicDefine.PIECE_NO},
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_OR,PublicDefine.PIECE_OR,PublicDefine.PIECE_NO},
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO} };

    //노란색 ㄴ
    private final int[][] Piece03 = {
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO},
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_YW,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO},
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_YW,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO},
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_YW,PublicDefine.PIECE_YW,PublicDefine.PIECE_NO} };

    //초록색 ㄴ 뒤집은 모양
    private final int[][] Piece04 = {
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO},
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_GR,PublicDefine.PIECE_NO},
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_GR,PublicDefine.PIECE_NO},
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_GR,PublicDefine.PIECE_GR,PublicDefine.PIECE_NO} };

    //하늘색 ㄴ
    //      ㅣ
    private final int[][] Piece05 = {
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO},
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_SB,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO},
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_SB,PublicDefine.PIECE_SB,PublicDefine.PIECE_NO},
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_SB,PublicDefine.PIECE_NO} };

    //파란색 ㅗ
    private final int[][] Piece06 = {
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO},
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_BU,PublicDefine.PIECE_NO},
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_BU,PublicDefine.PIECE_BU,PublicDefine.PIECE_BU},
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO} };

    //보라색 ..하늘색 뒤집은 모양 ^^
    private final int[][] Piece07 = {
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO},
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_NO,PublicDefine.PIECE_PP,PublicDefine.PIECE_NO},
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_PP,PublicDefine.PIECE_PP,PublicDefine.PIECE_NO},
            {PublicDefine.PIECE_NO,PublicDefine.PIECE_PP,PublicDefine.PIECE_NO,PublicDefine.PIECE_NO} };

    public PieceList() {
        PieceItems = new ArrayList<PieceItem>();
        rnd = new Random();
        InitList();
    }

    //블록 기본 생성
    private void InitList() {
        PieceItems.add( new PieceItem(Piece01) );
        PieceItems.add( new PieceItem(Piece02) );
        PieceItems.add( new PieceItem(Piece03) );
        PieceItems.add( new PieceItem(Piece04) );
        PieceItems.add( new PieceItem(Piece05) );
        PieceItems.add( new PieceItem(Piece06) );
        PieceItems.add( new PieceItem(Piece07) );
    }

    //블록 개수
    public int Length() {
        return PieceItems.size();
    }

    //index에 해당하는 블록 전달
    public PieceItem getItems( final int index ) {
        PieceItem Result = PieceItems.get(index);
        return Result.MakeClone();
    }

    //무작위 블록 전달
    public PieceItem getRandomItem() {
        return this.getItems( this.rnd.nextInt( this.Length() ));
    }
}
