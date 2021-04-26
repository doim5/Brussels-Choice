package com.example.brusselschoice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.brusselschoice.databinding.ActivityTutorialBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Stack;

import static java.lang.System.currentTimeMillis;

public class Tutorial extends AppCompatActivity implements View.OnClickListener{

    ActivityTutorialBinding binding;

    //tools
    Random rn;
    TextView selected;
    ArrayList<Integer> selection;
    int index1, index2;
    long max, min;
    long timestart, timeend, time;
    int stellen;
    int zahlenid;
    SharedPreferences prefs;
    String keymoves, keytime;
    TextView grayscreen, helper, whitestripe, whitestripe2;
    Button gobtn;
    ConstraintLayout grayscreenconstraint, highscoreconstraint;

    //attribute
    long zahl;
    int orange;
    int moves;

    //layout
    LinearLayout container;
    //LinearLayout oldnumbercontainer;
    ArrayList<Integer> ziffern;
    ArrayList<TextView> ziffernboxen;
    Button doublebtn, halfebtn;
    TextView currentbesttv, besttimetv, bestmovestv;
    View dvdr;

    //snackbar
    //divide odd number
    Snackbar dividemessage;

    //zahl zu groß
    Snackbar tobigmessage;

    //popups

    //start popup
    AlertDialog.Builder dialogbuilder1;
    AlertDialog startpopup;
    NumberPicker numberpicker;
    Button startbtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTutorialBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setTitle("Tutorial");

        //tools
        rn = new Random();
        selected = null;
        selection = new ArrayList<>();
        index1 = 0;
        index2 = 0;
        zahlenid = 0;
        prefs = this.getSharedPreferences("highscores", Context.MODE_PRIVATE);
        grayscreen = binding.grayscreen;
        grayscreen.setElevation(0);
        helper = binding.helper;
        helper.setText(R.string.t1);
        helper.setElevation(500f);
        gobtn = binding.gobtn;
        gobtn.setOnClickListener(this);
        whitestripe = binding.whitestripe;
        whitestripe.setVisibility(View.GONE);

        whitestripe2 = binding.whitestripe2;
        whitestripe.setVisibility(View.GONE);

        grayscreenconstraint = binding.grayscreenconstraint;
        grayscreenconstraint.setElevation(9f);

        highscoreconstraint = binding.highscoreconstraint;

        //attribute
        orange = 0x7DFFB200;
        stellen = 1;

        //layout
        container = binding.container;
        //oldnumbercontainer = binding.oldnumbercontainer;
        ziffern = new ArrayList<>();
        ziffernboxen = new ArrayList<>();

        doublebtn = binding.doublebtn;
        halfebtn = binding.halfebtn;

        doublebtn.setOnClickListener(this);
        halfebtn.setOnClickListener(this);

        doublebtn.setClickable(false);
        halfebtn.setClickable(false);

        currentbesttv = binding.currentbesttv;
        besttimetv = binding.besttimetv;
        bestmovestv = binding.bestmovestv;

        currentbesttv.setVisibility(View.INVISIBLE);
        besttimetv.setVisibility(View.INVISIBLE);
        bestmovestv.setVisibility(View.INVISIBLE);

        dvdr = binding.divider;

        //snackbars

        //divide odd number
        dividemessage = Snackbar.make(view, getString(R.string.dividemessagetwo), Snackbar.LENGTH_SHORT);

        //zu große zahl
        tobigmessage = Snackbar.make(view, getString(R.string.tobigmessage), Snackbar.LENGTH_SHORT);

        //popups
            //startpopup
            dialogbuilder1 = new AlertDialog.Builder(this);
            final View startpopupview = getLayoutInflater().inflate(R.layout.startpopup_dto, null);

            startbtn = startpopupview.findViewById(R.id.startbtn);
            numberpicker = startpopupview.findViewById(R.id.numberpicker);

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                numberpicker.setTextSize(80);
            }
            numberpicker.setMinValue(3);
            numberpicker.setMaxValue(10);
            numberpicker.setWrapSelectorWheel(false);

            startbtn.setOnClickListener(this);

            dialogbuilder1.setView(startpopupview);
            startpopup = dialogbuilder1.create();
    }

    public void onClick(View v) {
        if(v==doublebtn){
            if(!halfebtn.isClickable()){
                grayscreenconstraint.setElevation(200f);
                helper.setText(R.string.t4);
                doublebtn.setElevation(0f);
                doublebtn.setClickable(false);
                gobtn.setText("GOT IT!");
                gobtn.setVisibility(View.VISIBLE);
            }
            verdoppeln();
        }else if(v==halfebtn){

            halbieren();
        }
        else if(v==startbtn){
            container.setElevation(50f);
            whitestripe.setElevation(10f);
            whitestripe.setVisibility(View.VISIBLE);
            helper.setText(R.string.t2);
            helper.setVisibility(View.VISIBLE);
            moves = -1;

            stellen = numberpicker.getValue();
            keymoves = "moves" + stellen;
            keytime = "time" + stellen;

            //für winpopup
            String digitnumber = stellen + " Digit";

            if(stellen > 1){
                digitnumber += "s";
            }

            //für winpopup

            zahlfestlegen(stellen);
            zahlaktualisieren();
            startpopup.dismiss();
            timestart = currentTimeMillis();

            currentbesttv.setText(getString(R.string.highscoretext, stellen));
            besttimetv.setText(gettime(prefs.getLong(keytime, 0)));
            bestmovestv.setText(String.format(Locale.ENGLISH, "%d", prefs.getInt(keymoves, 0)));

        }else if(v==gobtn){
            if(gobtn.getText().equals("GO!")) {
                startpopup.show();
                helper.setVisibility(View.GONE);
                gobtn.setVisibility(View.GONE);
            }else if(gobtn.getText().equals("GOT IT!")){
                grayscreenconstraint.setElevation(9f);
                gobtn.setVisibility(View.GONE);
                helper.setText(R.string.t5);
                halfebtn.setElevation(100f);
                halfebtn.setClickable(true);
            }else if(gobtn.getText().equals("ALRIGHT!")){
                helper.setText(R.string.t7);

                gobtn.setText("BACK");
            }else{
                finish();
            }
        }else {
            //check: ob eine ziffer geklickt wurde
            for (TextView tv1 : ziffernboxen) {
                if (v == tv1) {
                    selection.clear();
                    if (selected == null) {
                        //alle ziffernboxen weiß machen
                        for (TextView tv2 : ziffernboxen) {
                            tv2.setBackgroundColor(0xFFFFFFFF);
                        }
                        //ausgewählte ziffernbox orange und auswahl speichern
                        tv1.setBackgroundColor(orange);
                        index1 = ziffernboxen.indexOf(tv1);
                        selected = tv1;
                        selection.add(Integer.parseInt(tv1.getText().toString()));
                    } else {
                        if(!doublebtn.isClickable()&&!halfebtn.isClickable()){
                            doublebtn.setClickable(true);
                            doublebtn.setElevation(100f);
                            helper.setText(R.string.t3);
                        }
                        index1 = ziffernboxen.indexOf(tv1);
                        index2 = ziffernboxen.indexOf(selected);
                        //ausgewählte box wieder weiß machen bei zweitem klick ansonsten rauf bzw runter alle orange
                        if (index1 == index2) {
                            tv1.setBackgroundColor(0x00FFFFFF);
                        } else if (index1 > index2) {
                            for (int i = index1; i >= index2; i--) {
                                ziffernboxen.get(i).setBackgroundColor(orange);
                                selection.add(Integer.parseInt(ziffernboxen.get(i).getText().toString()));
                            }
                        } else {
                            for (int i = index2; i >= index1; i--) {
                                ziffernboxen.get(i).setBackgroundColor(orange);
                                selection.add(Integer.parseInt(ziffernboxen.get(i).getText().toString()));
                            }
                        }
                        selected = null;
                    /*optionaler code je nachdem wie mit nullen am anfang der auswahl verfahren werden soll
                    if(!selection.isEmpty()) {
                        while (selection.get(selection.size() - 1) == 0) {
                            selection.remove(selection.size() - 1);
                            if(index1>index2){
                                index2++;
                            }else if(index2>index1){
                                index1++;
                            }
                        }
                    }*/
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed(){
        if(startpopup.isShowing()){
            startpopup.dismiss();
        }
        finish();
    }

    private void zahlaktualisieren(){
        ziffernboxen.clear();                                              //alle textviews mit ziffern löschen
        container.removeAllViewsInLayout();

        ziffern.clear();

        long temp = zahl;
        int counter = 0;

        //layoutparams für die ziffernboxen
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        do{
            //rechnung
            long oom = (long) Math.pow(10, counter);                        //order of magnitude der aktuellen ziffer: einer = 1, zehner = 10, ...
            int ziffer = (int) ((temp%(oom*10))/oom);                       //get: aktuelle ziffer: "temp%(oom*10": ganze zahl module oom*10 -> x000...    "/oom": -> x
            ziffern.add(counter, ziffer);                                   //add ziffer zur liste
            temp -= (long) ziffer*oom;                                      //remove: ziffer aus temp: xyz -> xy0 ->x00 -> 000

            //layout
            TextView ziffernbox = new TextView(this);               //temporäre referenz zu neuer ziffernbox
            ziffernbox.setId(counter);
            ziffernbox.setText(String.valueOf(ziffer));                     //add: ziffer in neue ziffernbox
            //ziffernbox.setLayoutParams(lp);                               //layoutparams festlegen
            ziffernbox.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 50);   //set: textgröße
            ziffernbox.setClickable(true);                                  //ziffernboxen anklickbar machen
            ziffernbox.setBackgroundColor(0xFFFFFFFF);
            ziffernbox.setElevation(10f);
            ziffernboxen.add(counter, ziffernbox);                          //add: neue zifferbox zur liste

            counter++;

            //add: onclicklistener zu jeder ziffernbox
            ziffernbox.setOnClickListener(this);
        }while(temp>0);

        reihenfolgeumkehren();

        //add all: ziffernboxen zum container
        for(TextView tv : ziffernboxen){
            container.addView(tv);
        }
        moves++;

        selection.clear();
    }

    private void verdoppeln(){
        long subzahl = getselection();
        ArrayList<Integer> neueziffern;

        if (selection.isEmpty()) {
            zahl = zahl*2;
            neueziffern = zahlzuziffern(zahl);
            if(neueziffern.size()>13){
                tobigmessage.show();
                zahl = zahl/2;
            }else {
                zahlaktualisieren();
            }
        } else {
            subzahl = subzahl*2;
            neueziffern = zahlzuziffern(subzahl);
            if(ziffern.size()-selection.size()+neueziffern.size()>13){
                tobigmessage.show();
                //subzahl = subzahl/2;
            }else {
                zahleinsetzen(subzahl);
            }
        }
    }

    private void halbieren(){
        long subzahl = getselection();
        if(selection.isEmpty()){
            if(zahl%2==1){
                dividemessage.show();
            }else{
                zahl = zahl / 2;
                zahlaktualisieren();

                tutorialend();
            }
        }else {
            if(subzahl%2==1){
                dividemessage.show();
            }else{
                subzahl = subzahl / 2;
                zahleinsetzen(subzahl);

                tutorialend();
            }
        }
    }

    private void tutorialend(){
        grayscreenconstraint.setElevation(500f);
        helper.setText(R.string.t6);
        helper.setElevation(501f);
        halfebtn.setClickable(false);
        gobtn.setText("ALRIGHT!");
        gobtn.setVisibility(View.VISIBLE);
        whitestripe2.setElevation(501f);
        whitestripe.setVisibility(View.VISIBLE);
        currentbesttv.setElevation(502f);

        whitestripe.setVisibility(View.GONE);
        container.setElevation(0f);

        timeend = currentTimeMillis();
        time = (timeend-timestart)/1000;

        String zeit = gettime(time);

        besttimetv.setText(zeit);
        bestmovestv.setText(String.format(Locale.ENGLISH, "%d", moves));
        besttimetv.setVisibility(View.VISIBLE);
        bestmovestv.setVisibility(View.VISIBLE);
        highscoreconstraint.setElevation(502f);
        currentbesttv.setVisibility(View.VISIBLE);

        for(TextView tv : ziffernboxen){
            tv.setClickable(false);
        }
    }

    private long getselection(){
        long selectednumber = 0;

        for (int i = 0; i < selection.size(); i++) {
            selectednumber += selection.get(i) * Math.pow(10, i);
        }

        return selectednumber;
    }

    private void zahleinsetzen(long subzahl){
        ArrayList<Integer> neueziffern = zahlzuziffern(subzahl);
        int i1 = ziffern.size() - index1 - selection.size();
        int i2 = ziffern.size() - index2 - selection.size();

        if(selected!=null){
            ziffern.remove(i1);
            ziffern.addAll(i1, neueziffern);
            selected = null;
        }else if(index1<index2){
            for(int i = 0; i<selection.size(); i++){
                ziffern.remove(i1);
            }
            ziffern.addAll(i1, neueziffern);
        }else if(index2<index1){
            for(int i = 0; i<selection.size(); i++){
                ziffern.remove(i2);
            }
            ziffern.addAll(i2, neueziffern);
        }

        zahl = 0;
        for(int i = 0; i<ziffern.size(); i++){
            zahl += ziffern.get(i) * Math.pow(10, i);
        }
        zahlaktualisieren();
    }

    private void zahlfestlegen(int i){

        min = (long) Math.pow(10, i-1);
        max = min*10;

        do{
            zahl = (long) (Math.random() * (max-min) + min);
        }while(zahl%5==0||zahl==1);

    }

    private ArrayList<Integer> zahlzuziffern(long z){
        long temp = z;
        int counter = 0;
        ArrayList<Integer> selectedziffern = new ArrayList<>();

        do {
            long oom = (long) Math.pow(10, counter);
            int ziffer = (int) ((temp % (oom * 10)) / oom);
            selectedziffern.add(counter, ziffer);
            temp -= (long) ziffer * oom;
            counter++;
        }while(temp>0);

        return selectedziffern;
    }

    private void reihenfolgeumkehren(){
        Stack<TextView> temp = new Stack<>();

        //add all: ziffernboxen zu stack
        for(TextView tv : ziffernboxen){
            temp.push(tv);
        }
        ziffernboxen.clear();

        //add all: ziffernboxen aus stack zu liste
        while(!temp.isEmpty()){
            ziffernboxen.add(temp.pop());
        }
    }

    private String gettime(long t){
        int sec = (int) t%60;
        t -= sec;
        t = t/60;

        int min = (int) t%60;
        t -= min;
        t = t/60;

        int h = (int) t%24;
        t -= h;
        t = t/24;

        int d = (int) t;

        String rueckgabe = Integer.toString(sec);

        if(sec<10){
            rueckgabe = "0" + rueckgabe;
        }

        if(min>0){
            rueckgabe = min + ":" + rueckgabe;
        }else{
            rueckgabe = "00:" + rueckgabe;
        }
        if(h>0){
            rueckgabe = h + ":" + rueckgabe;
        }
        if(d>0){
            rueckgabe = d + " Days " + rueckgabe;
        }

        return rueckgabe;
    }
}