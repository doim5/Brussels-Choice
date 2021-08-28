package de.doim.brusselschoice;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import de.doim.brusselschoice.databinding.ActivityFromToBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Stack;

public class FromTo extends AppCompatActivity implements View.OnClickListener{

    ActivityFromToBinding binding;

    //tools
    ArrayList<String> ids;
    CharSequence btntext;
    String text;
    String[] numbers;
    Long x, y;
    int levelcount;

    TextView selected;
    ArrayList<Integer> selection;
    int index1, index2;
    long max, min;
    int zahlenid;
    SharedPreferences prefs;
    String keymoves_ft;

    ViewFlipper flipper;

    //attribute
    long zahl, letztezahl;
    int orange;
    int moves;

    //layout
    LinkedList<Button> btns;

    LinearLayout container;
    ArrayList<Integer> ziffern;
    ArrayList<TextView> ziffernboxen;
    Button doublebtn, halfebtn, restartcurrentbtn, undobtn;
    TextView currentbesttv, bestmovestv;

    //snackbar
    //divide odd number
    Snackbar dividemessage;

    //zahl zu groß
    Snackbar tobigmessage;

    //win popup
    AlertDialog.Builder dialogbuilder2;
    AlertDialog winpopup;
    Button restartbtn, closebtn;
    TextView ftnumberstv, movestv;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFromToBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);
        setTitle("Levels");

        //Tools
        levelcount = 19;
        ids = new ArrayList<>();
        for(int i = 1; i<=levelcount; i++){
            ids.add("btn"+i);
            ids.add("btnspace1");
        }


        selected = null;
        selection = new ArrayList<>();
        index1 = 0;
        index2 = 0;
        zahlenid = 0;
        prefs = this.getSharedPreferences("highscores", Context.MODE_PRIVATE);

        flipper = binding.flipper;
        flipper.setInAnimation(this, android.R.anim.slide_in_left);
        flipper.setOutAnimation(this, android.R.anim.slide_out_right);

        //attribute
        orange = 0x7DFFB200;

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
        btns.getFirst().setText("1-3");
        btns.getFirst().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        boolean first = true;
        for(Button b : btns){
            if(first){
                first = false;
            }else{
                b.setWidth(btns.getFirst().getMeasuredWidth());
                b.setHeight(btns.getFirst().getMeasuredHeight());
            }
        }


        container = binding.container;
        ziffern = new ArrayList<>();
        ziffernboxen = new ArrayList<>();

        doublebtn = binding.doublebtn;
        halfebtn = binding.halfebtn;
        restartcurrentbtn = binding.restartcurrentbtn;
        undobtn = binding.undobtn;

        doublebtn.setOnClickListener(this);
        halfebtn.setOnClickListener(this);
        restartcurrentbtn.setOnClickListener(this);
        undobtn.setOnClickListener(this);

        currentbesttv = binding.currentbesttv;
        bestmovestv = binding.bestmovestv;

        currentbesttv.setVisibility(View.INVISIBLE);
        bestmovestv.setVisibility(View.INVISIBLE);

        //snackbars

        //divide odd number
        dividemessage = Snackbar.make(view, getString(R.string.dividemessagetwo), Snackbar.LENGTH_SHORT);

        //zu große zahl
        tobigmessage = Snackbar.make(view, getString(R.string.tobigmessage), Snackbar.LENGTH_SHORT);

        //winpopup
        dialogbuilder2 = new AlertDialog.Builder(this);
        final View winpopupview = getLayoutInflater().inflate(R.layout.winpopup_ft, null);

        ftnumberstv = winpopupview.findViewById(R.id.ftnumbers);
        //digitnumber in onclick start

        movestv = winpopupview.findViewById(R.id.movestv_ft);

        restartbtn = winpopupview.findViewById(R.id.restartbtn_ft);
        closebtn = winpopupview.findViewById(R.id.closebtn_ft);

        restartbtn.setOnClickListener(this);
        closebtn.setOnClickListener(this);

        dialogbuilder2.setView(winpopupview);
        winpopup = dialogbuilder2.create();
        winpopup.setCanceledOnTouchOutside(false);
        winpopup.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        for(Button b : btns){
            if(v == b){
                btntext = b.getText();
                text = btntext.toString().replaceAll("\n", "");
                numbers = text.split("-");
                x = Long.parseLong(numbers[0]);
                y = Long.parseLong(numbers[1]);

                flipper.showNext();
                setTitle("From " + x + " to " + y);

                start();
            }
        }
        if(v==doublebtn){
            verdoppeln();
        }else if(v==halfebtn){
            halbieren();
        }
        else if(v==undobtn){
            undo();
        }
        else if(v==restartcurrentbtn){
            restart();
        }
        else if(v==restartbtn){
            winpopup.dismiss();
            start();
        }else if(v==closebtn){
            winpopup.dismiss();
            flipper.showPrevious();
            setTitle("Levels");
        }else {
            //check: ob eine ziffer geklickt wurde
            for (TextView tv1 : ziffernboxen) {
                if (v == tv1) {
                    selection.clear();
                    if (selected == null) {
                        //alle ziffernboxen weiß machen
                        for (TextView tv2 : ziffernboxen) {
                            tv2.setBackgroundColor(0x00FFFFFF);
                        }
                        //ausgewählte ziffernbox orange und auswahl speichern
                        tv1.setBackgroundColor(orange);
                        index1 = ziffernboxen.indexOf(tv1);
                        selected = tv1;
                        selection.add(Integer.parseInt(tv1.getText().toString()));
                    } else {
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

    public void onBackPressed(){
        if(getTitle().equals("Levels")){
            finish();
        }else{
            flipper.showPrevious();
            setTitle("Levels");
        }
    }

    private void start(){
        moves = -1;

        keymoves_ft = "moves" + x + y + "ft";

        //für winpopup
        String numbers = x + " - " + y;

        ftnumberstv.setText(numbers);
        //für winpopup


        zahl = x;
        System.out.println(x);
        zahlaktualisieren();


        currentbesttv.setText("Your highscore:");
        bestmovestv.setText(String.format(Locale.ENGLISH, "%d", prefs.getInt(keymoves_ft, 0)));

        currentbesttv.setVisibility(View.VISIBLE);
        bestmovestv.setVisibility(View.VISIBLE);
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

        if(zahl==y){
            win();
        }
    }

    private void verdoppeln(){
        letztezahl = zahl;
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
        letztezahl = zahl;
        long subzahl = getselection();
        if(selection.isEmpty()){
            if(zahl%2==1){
                dividemessage.show();
            }else{
                zahl = zahl / 2;
                zahlaktualisieren();
            }
        }else {
            if(subzahl%2==1){
                dividemessage.show();
            }else{
                subzahl = subzahl / 2;
                zahleinsetzen(subzahl);
            }
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

    private void restart(){
        zahl = x;
        zahlaktualisieren();
        selection.clear();
        moves = 0;
    }

    private void undo(){
        zahl = letztezahl;
        zahlaktualisieren();
        selection.clear();
        moves-=2;
    }

    private void win(){
        movestv.setText(String.format(Locale.ENGLISH, "%d", moves));

        if(prefs.getInt(keymoves_ft, 0)>moves||prefs.getInt(keymoves_ft, 0)==0){
            prefs.edit().putInt(keymoves_ft, moves).apply();
            bestmovestv.setText(String.format(Locale.ENGLISH, "%d", moves));
        }

        winpopup.show();
    }
}