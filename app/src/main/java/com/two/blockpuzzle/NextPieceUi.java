package com.two.blockpuzzle;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

public class NextPieceUi extends View {

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

    private Bitmap[] mBitmapGroup = {
            mBmp00,
            mBmp01,
            mBmp02,
            mBmp03,
            mBmp04,
            mBmp05,
            mBmp06,
            mBmp07
    };

    private int mBitmapWidth = -1;
    private int mBitmapHeight = -1;

    private int mOffsetX = 0;
    private int mOffsetY = 0;

    public NextPieceUi(Context context) {
        super(context);
        this.CreateInit( context );
    }

    public void CreateInit(Context context) {
        if( mContext != null )
            return;

        mContext = context;
        for( int i = 0; i < mImageIdGroup.length; i++ ) {
            mBitmapGroup[i] = getResBitmap(mImageIdGroup[i]);
        }
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
            //d.setBounds(0, 0, width - 1, height - 1);
            d.draw(c);
        }

        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("tt", "" + canvas);

        Paint p = new Paint();
        p.setColor(Color.RED);
        //canvas.drawRect(30, 30, 50, 50, p);

        if((mBitmapWidth < 0) && (mBitmapHeight < 0)) {
            mBitmapWidth = mBitmapGroup[0].getScaledWidth(canvas);
            mOffsetY = (canvas.getWidth() - (mBitmapWidth * PublicDefine.PIECE_SIZE))/2;

            mBitmapHeight = mBitmapGroup[0].getScaledHeight(canvas);
            mOffsetX = (canvas.getHeight() - (mBitmapHeight * PublicDefine.PIECE_SIZE))/2;
        }

        for(int i = 0; i < PublicDefine.PIECE_SIZE; i++) {
            for(int j = 0; j < PublicDefine.PIECE_SIZE; j++) {
                if(PublicDefine.nextPiece[i][j] == PublicDefine.PIECE_NO) {
                    canvas.drawBitmap( mBitmapGroup[PublicDefine.PIECE_BL], j * mBitmapWidth + mOffsetY, i * mBitmapHeight + mOffsetX, null );
                } else {
                    canvas.drawBitmap( mBitmapGroup[ PublicDefine.nextPiece[i][j] ], j * mBitmapWidth + mOffsetY, i * mBitmapHeight + mOffsetX, null );
                }
                invalidate();
            }
        }//for
    }
}
