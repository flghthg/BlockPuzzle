package com.two.blockpuzzle;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class FrameUi extends View {

    private Context mContext = null;

    private int[] mImageIdGroup = {
            R.drawable.block00_black,
            R.drawable.block01_red,
            R.drawable.block02_orange,
            R.drawable.block03_yellow,
            R.drawable.block04_green,
            R.drawable.block05_skyblue,
            R.drawable.block06_blue,
            R.drawable.block07_purple
    };

    private Bitmap mBmp00 = null;
    private Bitmap mBmp01 = null;
    private Bitmap mBmp02 = null;
    private Bitmap mBmp03 = null;
    private Bitmap mBmp04 = null;
    private Bitmap mBmp05 = null;
    private Bitmap mBmp06 = null;
    private Bitmap mBmp07 = null;

    private Bitmap [] mBitmapGroup = {
            mBmp00,
            mBmp01,
            mBmp02,
            mBmp03,
            mBmp04,
            mBmp05,
            mBmp06,
            mBmp07
    };

    Point mPoint = null;
    private int mBitmapWidth = -1;
    private int mBitmapHeight = -1;

    private int mOffsetX = 0;
    private int mOffsetY = 0;

    private BoardGround mBoardGround = null;

    private void CreateInit(Context context) {
        if( mContext != null )
            return;

        mContext = context;
        mPoint = new Point();

        for( int i = 0; i < mImageIdGroup.length; i++ )
            mBitmapGroup[i] = getResBitmap( mImageIdGroup[i] );
    }

    public FrameUi(Context context) {
        super(context);
        this.CreateInit( context );
    }

    public FrameUi(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.CreateInit(context);
    }

    public FrameUi(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.CreateInit(context);
    }

    // BoardGround 클래스 설정
    public void setBoard(BoardGround br) {
        mBoardGround = br;
    }

    // 화면 초기화
    public void setBoardInit() {
    }

    // 리소스로부터 bitmap 얻어오기
    private Bitmap getResBitmap(int bmpResId) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Resources res = getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(res, bmpResId, opts);

        if (bitmap == null && isInEditMode()) {
            Drawable d = res.getDrawable(bmpResId, null);
            int width = d.getIntrinsicWidth();
            int height = d.getIntrinsicHeight();
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bitmap);
            d.setBounds(0, 0, width - 1, height - 1);
            d.draw(c);
        }

        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if( mBoardGround == null )
            return;

        if( (mBitmapWidth < 0) && (mBitmapHeight < 0) ) {
            mBitmapWidth = mBitmapGroup[0].getScaledWidth( canvas );
            mOffsetY = (canvas.getWidth() - (mBitmapWidth * PublicDefine.MATRIX_Y))/2;

            mBitmapHeight = mBitmapGroup[0].getScaledHeight( canvas );
            mOffsetX = (canvas.getHeight() - (mBitmapHeight * PublicDefine.MATRIX_X))/2;
        }

        for( int i = PublicDefine.MATRIX_BETWEEN, i1 = 0; i < PublicDefine.MATRIX_WORK_X; i++, i1++ )
            for( int j = PublicDefine.MATRIX_BETWEEN, j1 = 0; j < PublicDefine.MATRIX_WORK_Y; j++, j1++ )
                canvas.drawBitmap( mBitmapGroup[ mBoardGround.mBoard[i][j] ], j1*mBitmapWidth+mOffsetY, i1*mBitmapHeight+mOffsetX, null );

        super.onDraw(canvas);
    }
}
