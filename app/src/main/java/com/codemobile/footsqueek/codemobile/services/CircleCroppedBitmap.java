package com.codemobile.footsqueek.codemobile.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

/**
 * Created by greg on 25/01/2017.
 */

public class CircleCroppedBitmap {

    RoundedBitmapDrawable imageDrawable;

    public CircleCroppedBitmap(Bitmap bitmap, Context context) {

        Bitmap croppedBitmap = createCircleImage(bitmap);
        imageDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), croppedBitmap);
        imageDrawable.setCircular(true);
        imageDrawable.setCornerRadius(Math.max(bitmap.getHeight(), bitmap.getWidth()) / 2.0f);


    }

    public void setToImageViewView(ImageView view){
        view.setImageDrawable(imageDrawable);
    }

    private Bitmap createCircleImage(Bitmap srcBmp){
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
