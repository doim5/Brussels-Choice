package de.doim.brusselschoice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import de.doim.brusselschoice.databinding.ActivityCreditsBinding;

public class Credits extends AppCompatActivity {

    ActivityCreditsBinding binding;

    //layout
    TextView credits;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreditsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setTitle("Credits");

        //layout
        credits = binding.creditstv;

        credits.bringToFront();
    }
}