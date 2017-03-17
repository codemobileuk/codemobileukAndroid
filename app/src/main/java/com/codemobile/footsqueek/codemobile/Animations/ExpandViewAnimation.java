package com.codemobile.footsqueek.codemobile.Animations;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by greg on 16/03/2017.
 */

public class ExpandViewAnimation extends Animation{

    final int targetHeight;
    View view;
    int startHeight;
    boolean increaseSize;

    public ExpandViewAnimation(View view, int targetHeight, int starHeight, boolean increaseSize){
        this.view = view;
        this.targetHeight = targetHeight;
        this.startHeight = starHeight;
        this.increaseSize = increaseSize;

        Log.d("testingha", "height:== " + startHeight);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {

        float newHeight;
        if(increaseSize){
            newHeight = (int) (startHeight + targetHeight * interpolatedTime);
        }else{
            newHeight = (targetHeight - startHeight) * interpolatedTime + startHeight;

        }

        //to support decent animation, change new heigt as Nico S. recommended in comments
        //int newHeight = (int) (startHeight+(targetHeight - startHeight) * interpolatedTime);
        view.getLayoutParams().height =(int) newHeight;
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
