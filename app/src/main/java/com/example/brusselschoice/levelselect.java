package com.example.brusselschoice;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.brusselschoice.databinding.ActivityLevelselectBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class levelselect extends AppCompatActivity implements View.OnClickListener {

    ActivityLevelselectBinding binding;

    //tools
    ArrayList<String> ids;

    //layout
    LinkedList<Button> btns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLevelselectBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setTitle("From X To Y");

        //Tools
        ids = new ArrayList<>();
        for(int i = 1; i<=7; i++){
            ids.add("btn"+i);
        }

        //layout
        btns = new LinkedList<>();

        for(String s : ids){
            int resid = getResources().getIdentifier(s, "id", getPackageName());
            btns.add(findViewById(resid));
            btns.getLast().setOnClickListener(this);
        }

        btns.getFirst().measure(0,0);
        btns.getFirst().setHeight(btns.getFirst().getMeasuredWidth());
        btns.getFirst().measure(0,0);
        boolean first = true;
        for(Button b : btns){
            if(first){
                first = false;
            }else{
                b.setWidth(btns.getFirst().getMeasuredWidth());
                b.setHeight(btns.getFirst().getMeasuredHeight());
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v==btns.get(0)){

        }
    }
}