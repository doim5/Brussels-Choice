package com.example.brusselschoice;

import android.graphics.BlurMaskFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    Button playbtn, tutorialbtn, creditbtn, dtobtn, ftbtn, atgtbtn;

    //background
    ConstraintLayout level1, level2, level3;
    ArrayList<TextView> numbers1, numbers2, numbers3;

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

        //layout
        scrollview = binding.scrollview;
        bglevel1 = binding.bglevel1;
        bglevel2 = binding.bglevel2;
        bglevel3 = binding.bglevel3;
        pages = binding.pages;
        page1 = binding.page1;
        page2 = binding.page2;

        //background
        level1 = binding.level1;
        level2 = binding.level2;
        level3 = binding.level3;
        numbers1 = new ArrayList<>();
        numbers2 = new ArrayList<>();
        numbers3 = new ArrayList<>();
        background();


        scrollview.bringToFront();
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
        creditbtn = binding.creditbtn;
        dtobtn = binding.dtobtn;
        ftbtn = binding.ftbtn;
        atgtbtn = binding.atgtbtn;

        playbtn.setOnClickListener(this);

        atgtbtn.setHeight(dtobtn.getHeight());
    }

    @Override
    public void onClick(View v) {
        if(v==playbtn){
            scrollview.smoothScrollTo(screenwidth, 0);
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