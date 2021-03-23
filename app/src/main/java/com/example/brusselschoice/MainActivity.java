package com.example.brusselschoice;

import android.content.Intent;
import android.graphics.BlurMaskFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.brusselschoice.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ActivityMainBinding binding;

    //tools
    Display display;
    Point size;
    int screenheight, screenwidth;

    //layout
    HorizontalScrollView scrollview, bglevel1, bglevel2, bglevel3;
    LinearLayout pages, page1, page2;
    Button playbtn, tutorialbtn, creditsbtn, dtobtn, ftbtn, atgtbtn;


    //background
    ConstraintLayout level1, level2, level3;
    ArrayList<TextView> numbers1, numbers2, numbers3;

    //popup
    AlertDialog.Builder dialogbuilder;
    AlertDialog objectivepopup;
    Button closebtn;
    TextView titletv, objectivetv;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //tools
        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        screenheight = size.y;
        screenwidth = size.x;

        //popup
        dialogbuilder = new AlertDialog.Builder(this);
        final View objectivepopupview = getLayoutInflater().inflate(R.layout.objectivepopup, null);

        closebtn = objectivepopupview.findViewById(R.id.closeobjectivepopupbtn);
        closebtn.setOnClickListener(this);

        titletv = objectivepopupview.findViewById(R.id.titletv);
        objectivetv = objectivepopupview.findViewById(R.id.objectivetv);

        dialogbuilder.setView(objectivepopupview);
        objectivepopup = dialogbuilder.create();


        //background
        bglevel1 = binding.bglevel1;
        bglevel2 = binding.bglevel2;
        bglevel3 = binding.bglevel3;
        level1 = binding.level1;
        level2 = binding.level2;
        level3 = binding.level3;
        numbers1 = new ArrayList<>();
        numbers2 = new ArrayList<>();
        numbers3 = new ArrayList<>();
        background();


        //layout
        pages = binding.pages;
        page1 = binding.page1;
        page2 = binding.page2;

        scrollview = binding.scrollview;
        scrollview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                bglevel1.scrollTo(scrollX/2, scrollY);
                bglevel2.scrollTo(scrollX/4, scrollY);
                bglevel3.scrollTo(scrollX/6, scrollY);
            }
        });

        page1.setMinimumWidth(screenwidth);
        page2.setMinimumWidth(screenwidth);

        playbtn = binding.playbtn;
        tutorialbtn = binding.tutorialbtn;
        creditsbtn = binding.creditbtn;
        dtobtn = binding.dtobtn;
        ftbtn = binding.ftbtn;
        atgtbtn = binding.atgtbtn;

        playbtn.setOnClickListener(this);
        tutorialbtn.setOnClickListener(this);
        creditsbtn.setOnClickListener(this);
        dtobtn.setOnClickListener(this);
        ftbtn.setOnClickListener(this);
        atgtbtn.setOnClickListener(this);

        dtobtn.setOnLongClickListener(v -> {
            if(scrollview.getScrollX()>0) {
                titletv.setText(dtobtn.getText());
                objectivetv.setText(getString(R.string.objectivedto));
                objectivepopup.show();
            }
            return false;
        });
        ftbtn.setOnLongClickListener(v -> {
            if(scrollview.getScrollX()>0) {
                titletv.setText(ftbtn.getText());
                objectivetv.setText(getString(R.string.objectiveft));
                objectivepopup.show();
            }
            return false;
        });
        atgtbtn.setOnLongClickListener(v -> {
            if(scrollview.getScrollX()>0) {
                titletv.setText(atgtbtn.getText());
                objectivetv.setText(getString(R.string.objectiveatgt));
                objectivepopup.show();
            }
            return false;
        });

        atgtbtn.setHeight(dtobtn.getHeight());
    }

    @Override
    public void onClick(View v) {
        if(v==playbtn){
            scrollview.smoothScrollTo(screenwidth, 0);
        }else if(v==tutorialbtn){

        }else if(v==creditsbtn){
            startActivity(new Intent(this, Credits.class));
        }else if(v==dtobtn){
            startActivity(new Intent(this, DownToOne.class));
        }else if(v==ftbtn){

        }else if(v==atgtbtn){
            startActivity(new Intent(this, TheGoodThings.class));
        }else if(v==closebtn){
            objectivepopup.dismiss();
        }
    }

    @Override
    public void onBackPressed(){
        if(scrollview.getScrollX()>5){
            scrollview.smoothScrollTo(0,0);
        }else{
            finish();
        }
    }

    private void background(){
        for(int i = 0; i<level1.getChildCount(); i++){
            numbers1.add(i, (TextView) level1.getChildAt(i));

            numbers1.get(i).setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            float radius = numbers1.get(i).getTextSize() / 30;
            BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
            numbers1.get(i).getPaint().setMaskFilter(filter);
        }
        for(int i = 0; i<level2.getChildCount(); i++){
            numbers2.add(i, (TextView) level2.getChildAt(i));

            numbers2.get(i).setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            float radius = numbers2.get(i).getTextSize() / 15;
            BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
            numbers2.get(i).getPaint().setMaskFilter(filter);
        }
        for(int i = 0; i<level3.getChildCount(); i++){
            numbers3.add(i, (TextView) level3.getChildAt(i));

            numbers3.get(i).setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            float radius = numbers3.get(i).getTextSize() / 5;
            BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
            numbers3.get(i).getPaint().setMaskFilter(filter);
        }
    }
}