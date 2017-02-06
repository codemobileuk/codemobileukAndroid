package com.codemobile.footsqueek.codemobile.services;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.squareup.picasso.Transformation;

/**
 * Created by greg on 03/02/2017.
 */

public class RoundedCornersTransform implements Transformation {

    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        Bitmap bitmap = Bitmap.createBitmap(source.getWidth(), source.getHeight(), source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size /10f;
        canvas.drawRoundRect(new RectF(0, 0, source.getWidth(),source.getHeight()), r, r, paint);
        source.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "rounded_corners";
    }
}
