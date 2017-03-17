package com.codemobile.footsqueek.codemobile.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

/**
 * Created by greg on 25/01/2017.
 */

public class CircleCroppedBitmap {

    RoundedBitmapDrawable imageDrawable;
    Context context;
    Bitmap bitmap;

    public CircleCroppedBitmap(Bitmap bitmap, Context context) {
        this.context = context;
        this.bitmap = bitmap;

    }
    public void createCircleImage(){
        Bitmap croppedBitmap = cropBitmap(bitmap);
        imageDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), croppedBitmap);
        imageDrawable.setCircular(true);
        imageDrawable.setCornerRadius(Math.max(bitmap.getHeight(), bitmap.getWidth()) / 2.0f);

    }

    public void createRoundImage(ImageView view){
        createCircleImage();
        view.setImageDrawable(imageDrawable);


    }


    private Bitmap cropBitmap(Bitmap srcBmp){
        Bitmap dstBmp;
        if (srcBmp.getWidth() >= srcBmp.getHeight()){

            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    srcBmp.getWidth()/2 - srcBmp.getHeight()/2,
                    0,
                    srcBmp.getHeight(),
                    srcBmp.getHeight()
            );

        }else{

            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    0,
                    srcBmp.getHeight()/2 - srcBmp.getWidth()/2,
                    srcBmp.getWidth(),
                    srcBmp.getWidth()
            );
        }

        return dstBmp;

    }

}
