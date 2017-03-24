package com.codemobile.footsqueek.codemobile.customUi;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.codemobile.footsqueek.codemobile.R;

import java.util.List;

/**
 * Created by greg on 14/02/2017.
 */

public class LineButton extends LinearLayout {

    LinearLayout lineButton;
    Button button;
    View line;
    String text = "";
    AttributeSet attrs;
    boolean hasClickFocus, hasPartners = false;
    List<LineButton> togglePartners;


    public LineButton(Context context) {
        super(context);
        setup(context);
    }

    public LineButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
        setup(context);
    }

    public LineButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.attrs = attrs;
        setup(context);
    }

    public LineButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.attrs = attrs;
        setup(context);
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }



    private void setup(Context context){
        View view = inflate(context, R.layout.line_button, null);
        addView(view);
        addLayoutViews();
        getPresets(context);
      //  onClickListener();
        setAttributes();
        updateViews();

    }



    private void setAttributes(){
        button.setText(text);
    }

    public void setTogglePartners(List<LineButton> togglePartners, boolean hasClickFocus){
        this.hasClickFocus = hasClickFocus;
        this.togglePartners = togglePartners;
        updateViews();
        hasPartners = true;
    }

    private void updatePartners(){

        for (int i = 0; i < togglePartners.size(); i++) {

            if(togglePartners.get(i) != this){
                togglePartners.get(i).hasClickFocus = false;
                togglePartners.get(i).updateViews();
            }
        }
    }

    public void updateViews(){
        showLine();
    }

    private void addLayoutViews(){
        button = (Button)findViewById(R.id.button);
        line = findViewById(R.id.line);
        lineButton = (LinearLayout)findViewById(R.id.lineButton);
    }

    private void showLine(){
        if (hasClickFocus){
            line.setVisibility(VISIBLE);
        }else {
            line.setVisibility(INVISIBLE);
        }
    }

    public void customClick(){
        //since i override the click here this is a workaround to have the buttons do their jobs.
        if(hasClickFocus){
            if(hasPartners){
                hasClickFocus = true;
                updatePartners();
            }else{
                hasClickFocus = false;
            }

        }else{
            hasClickFocus = true;
            if(hasPartners){
                updatePartners();
            }
        }
        showLine();
    }


    private void getPresets(Context context){

        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.LineButton,0,0);
        try{
            text = ta.getString(R.styleable.LineButton_Text);
        }finally {
            ta.recycle();
        }

    }

}
